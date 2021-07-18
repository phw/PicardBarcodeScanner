package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.data.ReleaseSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    @GET("release/")
    suspend fun lookupReleaseWithQuery(@Query("query") query: String): ReleaseSearchResponse

    @GET
    suspend fun sendToPicard(@Url fullUrl: String)
}