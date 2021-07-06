package org.musicbrainz.picard.barcodescanner

import org.musicbrainz.picard.barcodescanner.data.*
import java.io.IOException

interface MusicBrainz {
    /*
     * Authentication
     */
    fun setCredentials(username: String?, password: String?)

    @Throws(IOException::class)
    fun autenticateCredentials(): Boolean

    @Throws(IOException::class)
    fun searchRelease(searchTerm: String?): List<ReleaseInfo?>?

}