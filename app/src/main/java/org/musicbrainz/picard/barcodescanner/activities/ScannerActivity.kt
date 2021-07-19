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
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.integration.android.IntentIntegrator
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.util.Constants
import org.musicbrainz.picard.barcodescanner.views.ConnectionStatusView
import java.util.*

class ScannerActivity : BaseActivity() {
    private var mAutoStart = false

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSubView(R.layout.activity_scanner)
        val connectBtn = findViewById<View>(R.id.btn_scan_barcode) as Button
        connectBtn.setOnClickListener { startScanner() }
        handleIntents()

        if (!preferences.connectionConfigured) {
            val configurePicard = Intent(
                this@ScannerActivity,
                PreferencesActivity::class.java
            )
            startActivity(configurePicard)
        } else if (mAutoStart) {
            startScanner()
        } else {
            val connectionBox = findViewById<View>(R.id.connection_status_box) as ConnectionStatusView
            connectionBox.updateStatus(preferences.ipAddress, preferences.port)
        }
    }

    override fun handleIntents() {
        super.handleIntents()
        val extras = intent.extras
        if (extras != null) {
            mAutoStart = extras.getBoolean(Constants.INTENT_EXTRA_AUTOSTART_SCANNER, false)
        }
    }

    private val zxingActivityResultLauncher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val scanResult = IntentIntegrator.parseActivityResult(it.resultCode, it.data)
        if (scanResult != null) {
            val barcode = scanResult.contents
            if (barcode != null) {
                startSearchActivity(barcode)
            }
        }
    }

    private fun startSearchActivity(barcode: String) {
        val resultIntent = Intent(
            this@ScannerActivity,
            PerformSearchActivity::class.java
        )
        resultIntent.putExtra(Constants.INTENT_EXTRA_BARCODE, barcode)
        startActivity(resultIntent)
    }

    private fun startScanner() {
        val integrator = IntentIntegrator(this@ScannerActivity)
        integrator.setOrientationLocked(false)
        zxingActivityResultLauncher.launch(integrator.createScanIntent())
    }
}
