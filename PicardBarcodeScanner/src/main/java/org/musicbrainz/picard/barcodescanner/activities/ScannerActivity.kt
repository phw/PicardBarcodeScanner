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
import android.view.*
import android.widget.Button
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.Companion.parseActivityResult
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.util.Constants
import java.util.*

class ScannerActivity : BaseActivity() {
    var mAutoStart = false

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSubView(R.layout.activity_scanner)
        handleIntents()
        if (!checkIfSettingsAreComplete()) {
            val configurePicard = Intent(
                this@ScannerActivity,
                PreferencesActivity::class.java
            )
            startActivity(configurePicard)
        } else if (mAutoStart) {
            startScanner()
        } else {
            val connectBtn = findViewById<View>(R.id.btn_scan_barcode) as Button
            connectBtn.setOnClickListener { startScanner() }
        }
    }

    override fun handleIntents() {
        super.handleIntents()
        val extras = intent.extras
        if (extras != null) {
            mAutoStart = extras.getBoolean(Constants.INTENT_EXTRA_AUTOSTART_SCANNER, false)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val scanResult = parseActivityResult(
            requestCode, resultCode, intent!!
        )
        if (scanResult != null) {
            var barcode = scanResult.contents
            if (isRunningInEmulator) barcode = "766929908628" // DEBUG
            if (barcode != null) {
                val resultIntent = Intent(
                    this@ScannerActivity,
                    PerformSearchActivity::class.java
                )
                resultIntent.putExtra(Constants.INTENT_EXTRA_BARCODE, barcode)
                startActivity(resultIntent)
            }
        }
    }

    private fun startScanner() {
        val integrator = IntentIntegrator(
            this@ScannerActivity
        )
        // Make sure the free barcode scanner app is the first in the list.
        val targetApplications: MutableList<String> = ArrayList(IntentIntegrator.TARGET_ALL_KNOWN)
        targetApplications.removeAll(IntentIntegrator.TARGET_BARCODE_SCANNER_ONLY)
        targetApplications.addAll(0, IntentIntegrator.TARGET_BARCODE_SCANNER_ONLY)
        integrator.setTargetApplications(targetApplications)
        integrator.initiateScan()
    }

    private fun checkIfSettingsAreComplete(): Boolean {
        return (preferences.ipAddress != ""
                && preferences.port > 0)
    }
}