package org.musicbrainz.picard.barcodescanner.data

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Partial release group data.
 */
open class ReleaseGroupInfo : Comparable<ReleaseGroupInfo> {
    var mbid: String? = null
    var title: String? = null
    var type: String? = null
    var firstRelease = Calendar.getInstance()
    val artists = LinkedList<ReleaseArtist>()
    val releaseMbids = LinkedList<String>()
    fun setFirstRelease(date: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            firstRelease.time = dateFormat.parse(date)
        } catch (e: ParseException) {
            formatWithoutDay(date)
        }
    }

    private fun formatWithoutDay(date: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM")
        try {
            firstRelease.time = dateFormat.parse(date)
        } catch (e: ParseException) {
            formatWithoutMonth(date)
        }
    }

    private fun formatWithoutMonth(date: String) {
        val dateFormat = SimpleDateFormat("yyyy")
        try {
            firstRelease.time = dateFormat.parse(date)
        } catch (e: ParseException) {
            firstRelease = null
        }
    }

    val releaseYear: String
        get() = if (firstRelease == null) {
            "--"
        } else {
            "" + firstRelease[Calendar.YEAR]
        }

    fun addArtist(artist: ReleaseArtist) {
        artists.add(artist)
    }

    val numberOfReleases: Int
        get() = releaseMbids.size

    fun addReleaseMbid(mbid: String) {
        releaseMbids.add(mbid)
    }

    override fun compareTo(another: ReleaseGroupInfo): Int {
        return if (firstRelease == null && another.firstRelease == null) {
            0
        } else if (firstRelease == null) {
            1
        } else if (another.firstRelease == null) {
            -1
        } else {
            firstRelease.compareTo(another.firstRelease)
        }
    }
}