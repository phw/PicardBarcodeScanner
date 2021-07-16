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
package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.util.WebServiceUtils
import java.io.IOException

class PicardClient(private val mIpAddress: String, private val mPort: Int) {
    private val httpClient = HttpClient

    suspend fun openRelease(releaseId: String): Boolean {
        val url = String.format(
            PICARD_OPENALBUM_URL, mIpAddress,
            mPort, WebServiceUtils.sanitise(releaseId)
        )
        httpClient.get(url).use { response ->
            return response.isSuccessful
        }
    }

    companion object {
        private const val PICARD_OPENALBUM_URL = "http://%s:%d/openalbum?id=%s"
    }
}
