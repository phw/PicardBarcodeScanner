/*
 * Copyright (C) 2012, 2021 Philipp Wolfer <ph.wolfer@gmail.com>
 * Copyright (C) 2021 Akshat Tiwari
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
import android.view.*
import org.musicbrainz.picard.barcodescanner.databinding.ActivityScannerBinding
import org.musicbrainz.picard.barcodescanner.util.Constants
import java.util.*

import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class ScannerActivity : BaseActivity() {
    private var mAutoStart = false
    private lateinit var binding: ActivityScannerBinding

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanBarcode.setOnClickListener { startScanner() }
        binding.connectionStatusBox.setOnClickListener { startPreferencesActivity() }
        handleIntents()

        when {
            !preferences.connectionConfigured -> {
                startPreferencesActivity()
            }
            mAutoStart -> {
                startScanner()
            }
        }
    }

    override fun handleIntents() {
        super.handleIntents()
        val extras = intent.extras
        if (extras != null) {
            mAutoStart = extras.getBoolean(Constants.INTENT_EXTRA_AUTOSTART_SCANNER, false)
        }

        binding.connectionStatusBox.updateStatus(preferences.ipAddress, preferences.port)
    }

    private fun startPreferencesActivity() {
        val configurePicard = Intent(
            this@ScannerActivity,
            PreferencesActivity::class.java
        )
        startActivity(configurePicard)
    }

    private fun startSearchActivity(barcode: String) {
        val resultIntent = Intent(
            this@ScannerActivity,
            PerformSearchActivity::class.java
        )
        resultIntent.putExtra(Constants.INTENT_EXTRA_BARCODE, barcode)
        startActivity(resultIntent)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        val barcode = result.contents
        if (barcode != null) {
            startSearchActivity(barcode)
        }
    }

    private fun startScanner() {
        val options = ScanOptions()
        options.setOrientationLocked(false)
        options.setDesiredBarcodeFormats(
            ScanOptions.EAN_13,
            ScanOptions.EAN_8,
            ScanOptions.UPC_A,
            ScanOptions.UPC_E,
        )
        barcodeLauncher.launch(options)
    }
}
