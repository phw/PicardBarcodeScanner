package org.musicbrainz.picard.barcodescanner

interface User {
    val username: String?
    val password: String?
}