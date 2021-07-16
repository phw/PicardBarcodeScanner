package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.data.BarcodeReleaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("release/")
    suspend fun lookupReleaseWithBarcode(@Query("query") barcode: String): BarcodeReleaseResponse
}