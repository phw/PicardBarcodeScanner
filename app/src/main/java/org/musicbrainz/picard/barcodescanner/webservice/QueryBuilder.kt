/*
 * Copyright (C) 2011-2012 Jamie McDonald
 * Copyright (C) 2021 Akshat Tiwari
 * Copyright (C) 2021 Philipp Wolfer <ph.wolfer@gmail.com>
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
package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.util.WebServiceUtils.sanitise

object QueryBuilder {
    private const val WEB_SERVICE = "https://musicbrainz.org/ws/2/"
    private const val SEARCH_RELEASE = "release?query="

    fun releaseSearch(searchTerm: String?): String {
        return buildQuery(
            SEARCH_RELEASE + sanitise(
                searchTerm!!
            )
        )
    }

    private fun buildQuery(path: String): String {
        return WEB_SERVICE + path
    }
}