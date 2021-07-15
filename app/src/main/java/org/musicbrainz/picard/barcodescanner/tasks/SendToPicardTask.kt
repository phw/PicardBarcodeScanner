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
package org.musicbrainz.picard.barcodescanner.tasks

import android.util.Log
import org.musicbrainz.picard.barcodescanner.data.ReleaseInfo
import org.musicbrainz.picard.barcodescanner.webservice.PicardClient
import org.musicbrainz.picard.barcodescanner.util.Preferences
import java.io.IOException

class SendToPicardTask(private val mPreferences: Preferences) : AsyncCallbackTask<ReleaseInfo, Int?, Iterator<ReleaseInfo?>?>() {

    override fun doInBackground(vararg params: ReleaseInfo?): Iterator<ReleaseInfo?> {
        val client = PicardClient(mPreferences.ipAddress!!, mPreferences.port)
        try {
            for (release in params) {
                client.openRelease(release!!.releaseMbid!!)
            }
        } catch (e: IOException) {
            Log.e(this.javaClass.name, e.message, e)
            onError(e)
        }
        return params.iterator()
    }
}