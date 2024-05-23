package org.musicbrainz.picard.barcodescanner.data

import java.util.*

/**
 * Data class representing a barcode scan.
 * Includes the barcode value, timestamp of the scan, and transmission status.
 */
data class Barcode(
    val value: String,
    val timestamp: Date,
    var transmitted: Boolean = false
)
