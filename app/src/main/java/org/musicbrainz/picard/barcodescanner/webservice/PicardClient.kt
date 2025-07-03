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

import android.annotation.SuppressLint
import org.musicbrainz.picard.barcodescanner.data.PicardPingResult
import org.musicbrainz.picard.barcodescanner.util.WebServiceUtils
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

@SuppressLint("DefaultLocale")
class PicardClient(private val mIpAddress: String, private val mPort: Int) {

    suspend fun openRelease(releaseId: String): Boolean {
        return try {
            instance.openAlbum(WebServiceUtils.sanitise(releaseId))
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun ping(): PicardPingResult {
        return try {
            val result = instance.ping()
            val match = PING_RESPONSE_REGEX.matchEntire((result))
            when {
                match != null -> PicardPingResult(true, getAppName(match.groupValues[1]))
                result == LEGACY_PING_RESPONSE -> PicardPingResult(true, getAppName())
                else -> PicardPingResult(false, "")
            }
        } catch (e: Exception) {
            PicardPingResult(false, "")
        }
    }

    private fun getAppName(version: String? = null): String {
        return PICARD_APP_NAME.format(version).trimEnd()
    }

    private val instance: PicardApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(String.format(BASE_URL, mIpAddress, mPort))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(PicardApi::class.java)
    }

    companion object {
        private const val BASE_URL = "http://%s:%d/"
        private val PING_RESPONSE_REGEX = Regex("MusicBrainz-Picard/(.*)")
        private const val LEGACY_PING_RESPONSE = "Nothing to see here"
        private const val PICARD_APP_NAME = "MusicBrainz Picard %s"
        private val okHttpClient = HttpClient.newBuilder()
            .connectTimeout(1000, TimeUnit.MILLISECONDS)
            .build()
    }
}
