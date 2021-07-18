package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.data.ReleaseSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    @GET("release/")
    suspend fun lookupReleaseWithBarcode(@Query("query") barcode: String): ReleaseSearchResponse

    @GET
    suspend fun sendToPicard(@Url fullUrl: String)
}