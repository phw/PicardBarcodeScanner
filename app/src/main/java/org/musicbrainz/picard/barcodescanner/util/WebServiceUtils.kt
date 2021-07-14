package org.musicbrainz.picard.barcodescanner.util

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

object WebServiceUtils {

    fun sanitise(input: String): String {
        return try {
            URLEncoder.encode(input, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            input
        }
    }
}