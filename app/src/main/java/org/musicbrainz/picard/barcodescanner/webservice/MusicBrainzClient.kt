package org.musicbrainz.picard.barcodescanner.webservice

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MusicBrainzClient {

    private val baseUrl = "https://musicbrainz.org/ws/2/"
    var userAgent = "picard-android-barcodescanner/1.5"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .header("User-Agent", userAgent)
                .addHeader("Accept", "application/json")
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    val instance: MusicBrainzApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()

        retrofit.create(MusicBrainzApi::class.java)
    }

}