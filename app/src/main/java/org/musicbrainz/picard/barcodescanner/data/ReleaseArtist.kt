package org.musicbrainz.picard.barcodescanner.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Artist name and MBID pair for release.
 */
@Parcelize
data class ReleaseArtist( var mbid: String? = null,
                           var name: String? = null,
                           var sortName: String? = null) : Comparable<ReleaseArtist>, Parcelable {

    override fun compareTo(other: ReleaseArtist): Int {
        return sortName!!.compareTo(other.sortName!!)
    }
}