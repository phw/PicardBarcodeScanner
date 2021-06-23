package org.musicbrainz.picard.barcodescanner.data

import org.musicbrainz.picard.barcodescanner.util.StringFormat

class Track {
    var title: String? = null
    var recordingMbid: String? = null
    var position = 0
    var duration = 0
    val formattedDuration = StringFormat.formatDuration(duration)
}