package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.util.WebServiceUtils.sanitise

object QueryBuilder {
    private const val WEB_SERVICE = "http://musicbrainz.org/ws/2/"
    private const val SEARCH_RELEASE = "release?query="

    // MBID for Various Artists always exists.
    private const val AUTH_TEST = "artist/89ad4ac3-39f7-470e-963a-56509c546377?inc=user-tags"

    fun releaseSearch(searchTerm: String?): String {
        return buildQuery(
            SEARCH_RELEASE + sanitise(
                searchTerm!!
            )
        )
    }


    fun authenticationCheck(): String {
        return buildQuery(AUTH_TEST)
    }

    private fun buildQuery(path: String): String {
        return WEB_SERVICE + path
    }
}