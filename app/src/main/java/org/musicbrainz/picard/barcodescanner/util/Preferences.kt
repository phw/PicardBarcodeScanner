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
package org.musicbrainz.picard.barcodescanner.util

import android.content.Context
import android.content.SharedPreferences
import org.musicbrainz.picard.barcodescanner.R

class Preferences(private val mContext: Context) {
    private val mSettings: SharedPreferences = mContext.getSharedPreferences(
        Constants.PREFERENCES_NAME, Context.MODE_PRIVATE
    )
    private val defaultPort: Int
        get() {
            val port = mContext.getString(R.string.picard_default_port)
            return try {
                port.trim().toInt()
            } catch (nfe: NumberFormatException) {
                0
            }
        }
    val ipAddress = mSettings.getString(Constants.PREFERENCE_PICARD_IP_ADDRESS, "")
    val port = mSettings.getInt(Constants.PREFERENCE_PICARD_PORT, defaultPort)

    fun setIpAddressAndPort(ipAddress: String?, port: Int) {
        val editor = mSettings.edit()
        editor.putString(Constants.PREFERENCE_PICARD_IP_ADDRESS, ipAddress)
        editor.putInt(Constants.PREFERENCE_PICARD_PORT, port)
        editor.apply()
    }

    val connectionConfigured: Boolean
        get() {
            return !ipAddress.isNullOrBlank() && port > 0
        }
}