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
package org.musicbrainz.picard.barcodescanner.tasks

import android.util.Log
import org.musicbrainz.picard.barcodescanner.data.ReleaseInfo
import org.musicbrainz.picard.barcodescanner.webservice.MusicBrainzWebClient
import java.io.IOException
import java.util.*

class ReleaseLookupTask() : AsyncCallbackTask<String?, Int?, Array<ReleaseInfo>>() {
    override fun doInBackground(vararg params: String?): Array<ReleaseInfo> {
        val mbClient = MusicBrainzWebClient()
        return try {
            val barcode = params[0]
            val searchTerm = String.format("barcode:%s", barcode)
            val releases: LinkedList<ReleaseInfo> = mbClient.searchRelease(searchTerm)
            val releaseArray: Array<ReleaseInfo?> = arrayOfNulls(releases.size)
            releases.toArray(releaseArray)
        } catch (e: IOException) {
            Log.e(this.javaClass.name, e.message, e)
            onError(e)
            arrayOf()
        }
    }
}