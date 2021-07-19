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
import android.view.*
import android.widget.Button
import android.widget.EditText
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.util.Constants
import org.musicbrainz.picard.barcodescanner.views.ConnectionStatusView

class PreferencesActivity : BaseActivity() {
    private var ipAddressInput: EditText? = null
    private var portInput: EditText? = null
    private var connectBtn: Button? = null
    private var barcode: String? = null
    private var connectionBox: ConnectionStatusView? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSubView(R.layout.activity_preferences)
        ipAddressInput = findViewById<View>(R.id.picard_ip_address) as EditText
        portInput = findViewById<View>(R.id.picard_port) as EditText
        connectBtn = findViewById<View>(R.id.btn_picard_connect) as Button
        connectionBox = findViewById<View>(R.id.connection_status_box) as ConnectionStatusView
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        handleIntents()
        loadFormDataFromPreferences()
        checkConnectButtonEnabled()
        registerEventListeners()
        checkConnectionStatus()
        if (barcode != null) {
            connectBtn!!.setText(R.string.btn_picard_connect)
        }
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
        ipAddressInput!!.addTextChangedListener(textWatcher)
        portInput!!.addTextChangedListener(textWatcher)
        connectBtn!!.setOnClickListener {
            preferences.setIpAddressAndPort(
                readIpAddressFromInput(),
                readPortFromInput()
            )
            startNextActivity()
        }
    }

    private fun loadFormDataFromPreferences() {
        ipAddressInput!!.setText(preferences.ipAddress)
        portInput!!.setText(java.lang.String.valueOf(preferences.port))
    }

    private fun checkConnectButtonEnabled() {
        connectBtn!!.isEnabled = readIpAddressFromInput() != ""
    }

    private fun readIpAddressFromInput(): String {
        return ipAddressInput!!.text.toString()
    }

    private fun readPortFromInput(): Int {
        val port = portInput!!.text.toString()
        return try {
            port.trim { it <= ' ' }.toInt()
        } catch (nfe: NumberFormatException) {
            0
        }
    }

    private fun checkConnectionStatus() {
        connectionBox!!.updateStatus(readIpAddressFromInput(), readPortFromInput())
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