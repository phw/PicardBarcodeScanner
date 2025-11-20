/*
 * Copyright (C) 2021 Philipp Wolfer <ph.wolfer@gmail.com>
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

import androidx.core.net.toUri
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import org.musicbrainz.picard.barcodescanner.data.ReleaseSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicBrainzClient(private val mMusicBrainzServerUrl: String) {

    suspend fun queryReleasesByBarcode(barcode: String?): ReleaseSearchResponse {
        val query = "barcode:%s".format(barcode)
        return instance.queryReleases(query)
    }

    private val baseUrl: String
        get() {
            val uri = mMusicBrainzServerUrl.toUri()
            val uriBuilder = uri.buildUpon()
            uriBuilder.path("/ws/2/")
            if (uri.scheme == null) {
                uriBuilder.scheme("https")
            }
            return uriBuilder.toString()
        }

    private val instance: MusicBrainzApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder().setStrictness(Strictness.LENIENT).create()))
            .client(okHttpClient)
            .build()

        retrofit.create(MusicBrainzApi::class.java)
    }

    companion object {
        private const val USER_AGENT = "picard-android-barcodescanner/1.5"
        private val okHttpClient = HttpClient.newBuilder()
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .header("User-Agent", USER_AGENT)
                    .addHeader("Accept", "application/json")
                    .method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()
    }
}
