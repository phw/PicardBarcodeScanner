package org.musicbrainz.picard.barcodescanner.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Partial release data to differentiate between similar releases (e.g. part of
 * the same release group).
 */

@Parcelize
data class ReleaseInfo(var id: String? = null,
                       var title: String? = null,
                       val artists: ArrayList<ReleaseArtist> = ArrayList<ReleaseArtist>(),
                       var date: String? = null) : Comparable<ReleaseInfo>, Parcelable {


    override fun compareTo(other: ReleaseInfo): Int {
        val artistNameComparison = artists[0].compareTo(other.artists[0])
        if (artistNameComparison != 0) {
            return artistNameComparison
        }
        return if (date == null && other.date == null) {
            0
        } else if (date == null) {
            1
        } else if (other.date == null) {
            -1
        } else {
            date!!.compareTo(other.date!!)
        }
    }
}