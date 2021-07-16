package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.data.BarcodeReleaseResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {

    @GET("release/")
    suspend fun lookupReleaseWithBarcode(@Query("query") barcode: String): BarcodeReleaseResponse

    @GET
    suspend fun sendToPicard(@Url fullUrl: String)
}