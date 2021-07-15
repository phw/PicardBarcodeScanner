/*
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

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

object HttpClient {
    private const val TIMEOUT : Long = 20000
    private val client = OkHttpClient.Builder()
        .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        .build()
    var userAgent = "picard-android-barcodescanner/1.5"

    @Throws(IOException::class)
    fun get(url: String): Response {
        val request = Request.Builder()
            .header("User-Agent", userAgent)
            .url(url)
            .build()
        return client.newCall(request).execute()
    }
}
