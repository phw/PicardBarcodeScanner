package org.musicbrainz.picard.barcodescanner.data

import org.musicbrainz.picard.barcodescanner.util.StringFormat

/**
 * Link URL and type description pair.
 */
class WebLink : Comparable<WebLink> {
    var url: String? = null
    var type: String? = null

    // Remove http:// and trailing /
    val prettyUrl: String
        get() {
            // Remove http:// and trailing /
            var url = url!!.replace("http://", "")
            url = url.replace("https://", "")
            if (url.endsWith("/")) {
                url = url.substring(0, url.length - 1)
            }
            return url
        }
    private val prettyType: String
        get() {
            type = type!!.replace('_', ' ')
            return StringFormat.initialCaps(type)
        }

    override fun compareTo(another: WebLink): Int {
        return prettyType.compareTo(another.prettyType)
    }
}