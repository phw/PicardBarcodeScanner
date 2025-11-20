/*
 * Copyright (C) 2012 Philipp Wolfer <ph.wolfer@googlemail.com>
 * 
 * This file is part of MusicBrainz Picard Barcode Scanner.
 * 
 * MusicBrainz Picard Barcode Scanner is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * MusicBrainz Picard Barcode Scanner is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * MusicBrainz Picard Barcode Scanner. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.musicbrainz.picard.barcodescanner.util

object Constants {
    const val PREFERENCES_NAME = "org.musicbrainz.picard.barcodescanner.preferences"
    const val PREFERENCE_MUSICBRAINZ_SERVER_URL = "musicbrainz_server_url"
    const val PREFERENCE_PICARD_IP_ADDRESS = "picard_ip_address"
    const val PREFERENCE_PICARD_PORT = "picard_port"
    const val INTENT_EXTRA_ERROR = "org.musicbrainz.picard.error"
    const val INTENT_EXTRA_BARCODE = "org.musicbrainz.picard.barcode"
    const val INTENT_EXTRA_RELEASE_TITLES = "org.musicbrainz.picard.releaseTitles"
    const val INTENT_EXTRA_RELEASE_ARTISTS = "org.musicbrainz.picard.releaseArtists"
    const val INTENT_EXTRA_RELEASE_DATES = "org.musicbrainz.picard.releaseDates"
    const val INTENT_EXTRA_AUTOSTART_SCANNER = "org.musicbrainz.picard.startScanner"
    const val SPONSOR_URL = "https://github.com/sponsors/phw/"
}