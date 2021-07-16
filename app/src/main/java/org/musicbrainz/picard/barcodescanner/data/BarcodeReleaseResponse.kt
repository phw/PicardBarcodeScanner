package org.musicbrainz.picard.barcodescanner.data

import java.util.*

class BarcodeReleaseResponse {
    var created: String? = null
    var count = 0
    var offset = 0
    var releases: List<ReleaseInfo> = ArrayList()
}