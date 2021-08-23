package org.musicbrainz.picard.barcodescanner.data

import java.util.*
import com.google.gson.annotations.SerializedName

/**
 * Partial release data to differentiate between similar releases (e.g. part of
 * the same release group).
 */
class Release : Comparable<Release> {
    var id: String? = null
    var title: String? = null
    var date: String? = null

    @SerializedName("artist-credit")
    val artistCredit: ArrayList<ArtistCredit> = ArrayList<ArtistCredit>()

    val releaseArtist: String
        get() {
            val result = StringBuilder()
            for (artist in artistCredit) {
                result.append(artist.name)
                if (!artist.joinphrase.isNullOrBlank()) {
                    result.append((artist.joinphrase))
                }
            }
            return result.toString()
        }

    override fun compareTo(other: Release): Int {
        val artistNameComparison = releaseArtist.compareTo(other.releaseArtist)
        return when {
            artistNameComparison != 0 -> {
                artistNameComparison
            }
            date == null && other.date == null -> {
                0
            }
            date == null -> {
                1
            }
            other.date == null -> {
                -1
            }
            else -> {
                date!!.compareTo(other.date!!)
            }
        }
    }
}