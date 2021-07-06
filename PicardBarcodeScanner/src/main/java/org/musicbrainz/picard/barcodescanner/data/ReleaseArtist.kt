package org.musicbrainz.picard.barcodescanner.data

/**
 * Artist name and MBID pair for release.
 */
class ReleaseArtist : Comparable<ReleaseArtist> {
    var mbid: String? = null
    var name: String? = null
    var sortName: String? = null
    override fun compareTo(another: ReleaseArtist): Int {
        return sortName!!.compareTo(another.sortName!!)
    }
}