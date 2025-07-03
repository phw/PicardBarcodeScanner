/*
 * Copyright (C) 2021 Philipp Wolfer <ph.wolfer@gmail.com>
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
package org.musicbrainz.picard.barcodescanner.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.data.PicardPingResult
import org.musicbrainz.picard.barcodescanner.webservice.PicardClient

class ConnectionStatusView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var appLabel: TextView
    private var hostLabel: TextView
    private var infoLabel: TextView
    private var lastUpdate = 0

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.connection_status, this, true)
        setBackgroundResource(R.drawable.infobox)
        appLabel = findViewById<View>(R.id.status_application_name) as TextView
        hostLabel = findViewById<View>(R.id.status_connection_host) as TextView
        infoLabel = findViewById<View>(R.id.status_info) as TextView
    }

    fun updateStatus(host: String?, port: Int) {
        if (!host.isNullOrBlank() && port > 0) {
            uiScope.launch {
                setInfoMessage(R.string.label_status_update)
                lastUpdate += 1
                checkConnectionStatus(host, port, lastUpdate)
            }
        } else {
            setInfoMessage(R.string.label_connection_no_config)
        }
    }

    private suspend fun checkConnectionStatus(host: String, port: Int, updateCounter: Int) {
        val client = PicardClient(host, port)
        val status = client.ping()
        when {
            // Ensure for parallel updates we apply only the last one
            updateCounter < lastUpdate -> return
            status.active -> setStatus(status, host, port)
            else -> setInfoMessage(R.string.label_connection_error)
        }
    }

    private fun setStatus(status: PicardPingResult, host: String, port: Int) {
        appLabel.visibility = VISIBLE
        hostLabel.visibility = VISIBLE
        infoLabel.visibility = GONE
        appLabel.text = status.application
        hostLabel.text = "%s:%d".format(host, port)
    }

    private fun setInfoMessage(@StringRes resId: Int) {
        appLabel.visibility = GONE
        hostLabel.visibility = GONE
        infoLabel.setText(resId)
        infoLabel.visibility = VISIBLE
    }
}
