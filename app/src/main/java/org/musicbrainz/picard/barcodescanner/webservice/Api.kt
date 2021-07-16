package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.data.ReleaseInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("release/")
    fun lookupReleaseWithBarcode(@Query("query") barcode: String): Call<ReleaseInfo>
}