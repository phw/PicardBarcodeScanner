package org.musicbrainz.picard.barcodescanner.data

/**
 * Tag name and count pair. Count can be used for weighting.
 */
class Tag : Comparable<Tag> {
    var text: String? = null
    var count = 0
    override fun compareTo(another: Tag): Int {
        return count.compareTo(another.count) * -1
    }
}