/*
 * Copyright (C) 2012 Philipp Wolfer <ph.wolfer@googlemail.com>
 * 
 * This file is part of MusicBrainz Picard Barcode Scanner.
 * 
 * MusicBrainz Picard Barcode Scanner is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * MusicBrainz Picard Barcode Scanner is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * MusicBrainz Picard Barcode Scanner. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.musicbrainz.picard.barcodescanner.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.databinding.ActivityPreferencesBinding
import org.musicbrainz.picard.barcodescanner.util.Constants
import org.musicbrainz.picard.barcodescanner.views.ConnectionStatusView
import org.musicbrainz.picard.barcodescanner.webservice.PicardClient

class PreferencesActivity : BaseActivity() {
    private var barcode: String? = null
    private var connectionBox: ConnectionStatusView? = null
    private lateinit var binding: ActivityPreferencesBinding

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectionBox = findViewById<View>(R.id.connection_status_box) as ConnectionStatusView
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        handleIntents()
        loadFormDataFromPreferences()
        checkConnectButtonEnabled()
        registerEventListeners()
        checkConnectionStatus()
        if (barcode != null) {
            binding.btnPicardConnect.setText(R.string.btn_picard_connect)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        menu.findItem(R.id.action_settings).isVisible = false
        return result
    }

    override fun handleIntents() {
        super.handleIntents()
        val extras = intent.extras
        barcode = extras?.getString(Constants.INTENT_EXTRA_BARCODE)
    }

    private fun registerEventListeners() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                checkConnectButtonEnabled()
                checkConnectionStatus()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        }
        binding.picardIpAddress.addTextChangedListener(textWatcher)
        binding.picardPort.addTextChangedListener(textWatcher)
        binding.btnPortDetect.setOnClickListener {
            val ipAddress = readIpAddressFromInput()
            if (ipAddress != "") {
                lifecycleScope.launch {
                    detectPort(ipAddress)
              }
            }
        }
        binding.btnPicardConnect.setOnClickListener {
            preferences.setAllPreferences(
                readMusicBrainzServerUrlFromInput().trim(),
                readIpAddressFromInput().trim(),
                readPortFromInput()
            )
            startNextActivity()
        }
    }

    private fun loadFormDataFromPreferences() {
        binding.musicbrainzServerUrl.setText(preferences.musicBrainzServerUrl)
        binding.picardIpAddress.setText(preferences.ipAddress)
        binding.picardPort.setText(java.lang.String.valueOf(preferences.port))
    }

    private fun checkConnectButtonEnabled() {
        val enabled = readIpAddressFromInput() != ""
        binding.btnPortDetect.isEnabled = enabled
        binding.btnPicardConnect.isEnabled = enabled
    }

    private fun readMusicBrainzServerUrlFromInput(): String {
        return binding.musicbrainzServerUrl.text.toString()
    }

    private fun readIpAddressFromInput(): String {
        return binding.picardIpAddress.text.toString()
    }

    private fun readPortFromInput(): Int {
        val port = binding.picardPort.text.toString()
        return try {
            port.trim().toInt()
        } catch (nfe: NumberFormatException) {
            0
        }
    }

    private fun checkConnectionStatus() {
        connectionBox!!.updateStatus(readIpAddressFromInput(), readPortFromInput())
    }

    private suspend fun detectPort(ipAddress: String) {
        val startPort = preferences.defaultPort
        val endPort = startPort + 10
        for (port in startPort..endPort) {
            val client = PicardClient(ipAddress, port)
            val status = client.ping()
            if (status.active) {
                binding.picardPort.setText(port.toString())
                return
            }
        }
    }

    private fun startNextActivity() {
        val intent: Intent
        if (barcode == null) {
            intent = Intent(this@PreferencesActivity, ScannerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        } else {
            intent = Intent(
                this@PreferencesActivity,
                PerformSearchActivity::class.java
            )
            intent.putExtra(Constants.INTENT_EXTRA_BARCODE, barcode)
        }
        startActivity(intent)
        finish()
    }
}
