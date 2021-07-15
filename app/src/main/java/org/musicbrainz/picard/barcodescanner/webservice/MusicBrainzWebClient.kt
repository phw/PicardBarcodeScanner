/*
 * Copyright (C) 2011-2012 Jamie McDonald
 * Copyright (C) 2021 Akshat Tiwari
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
package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.data.*
import java.io.IOException
import java.util.*

/**
 * Makes the web service available for Activity classes. Calls are blocking and
 * should be made asynchronously. The XML returned is parsed into pojos with
 * SAX handlers.
 */
class MusicBrainzWebClient {
    private var httpClient = HttpClient
    private var responseParser = ResponseParser()

    @Throws(IOException::class)
    fun searchRelease(searchTerm: String?): LinkedList<ReleaseInfo>{
        val url = QueryBuilder.releaseSearch(searchTerm)
        httpClient.get(url).use { response ->
            if (response.isSuccessful) {
                response.body!!.byteStream().use { input ->
                    return responseParser.parseReleaseSearch(input)
                }
            }
        }

        return LinkedList<ReleaseInfo>()
    }
}
