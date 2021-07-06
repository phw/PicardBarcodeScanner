package org.musicbrainz.picard.barcodescanner.data

import java.util.*

class Release {
    var mbid: String? = null
    var releaseGroupMbid: String? = null
    var barcode: String? = null
    var asin: String? = null
    val artists = ArrayList<ReleaseArtist>()
    var title: String? = null
    var status: String? = null
    var date: String? = null
    var releaseGroupRating = 0f
    var releaseGroupRatingCount = 0
    private var releaseGroupTags: MutableList<Tag> = LinkedList()
    private var labels: MutableCollection<String> = LinkedList()
    var trackList = ArrayList<Track>()
    fun setReleaseMbid(releaseMbid: String?) {
        mbid = releaseMbid
    }

    fun addArtist(artist: ReleaseArtist) {
        artists.add(artist)
    }

    fun getReleaseGroupTags(): List<Tag> {
        Collections.sort(releaseGroupTags)
        return releaseGroupTags
    }

    fun setReleaseGroupTags(releaseGroupTags: MutableList<Tag>) {
        this.releaseGroupTags = releaseGroupTags
    }

    fun addReleaseGroupTag(tag: Tag) {
        releaseGroupTags.add(tag)
    }

    fun getLabels(): Collection<String> {
        return labels
    }

    fun setLabels(labels: LinkedList<String>) {
        this.labels = labels
    }

    fun addLabel(label: String) {
        labels.add(label)
    }

    fun addTrack(track: Track) {
        trackList.add(track)
    }
}