package org.musicbrainz.picard.barcodescanner.data

import java.util.*

/**
 * Partial release data to differentiate between similar releases (e.g. part of
 * the same release group).
 */
class ReleaseInfo : Comparable<ReleaseInfo> {
    var id: String? = null
    var title: String? = null
    val artists = ArrayList<ReleaseArtist>()
    var date: String? = null
    var tracksNum = 0
    var countryCode: String? = null
    private val labels: MutableCollection<String> = LinkedList()
    private val formats: MutableCollection<String> = LinkedList()
    fun addArtist(artist: ReleaseArtist) {
        artists.add(artist)
    }

    fun getLabels(): Collection<String> {
        return labels
    }

    fun addLabel(label: String) {
        labels.add(label)
    }

    fun getFormats(): Collection<String> {
        return formats
    }

    fun addFormat(format: String) {
        formats.add(format)
    }

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