package org.musicbrainz.picard.barcodescanner.handler

import org.musicbrainz.picard.barcodescanner.data.ReleaseArtist
import org.musicbrainz.picard.barcodescanner.data.ReleaseInfo
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import java.util.*

class ReleaseInfoHandler : MBHandler() {
    val results = LinkedList<ReleaseInfo>()
    private var release: ReleaseInfo? = null
    private var releaseArtist: ReleaseArtist? = null
    private var inArtist = false
    private var inLabel = false
    private var inMedium = false

    @Throws(SAXException::class)
    override fun startElement(namespaceURI: String, localName: String, qName: String, atts: Attributes) {
        when (localName) {
            "release" -> {
                release = ReleaseInfo()
                release!!.releaseMbid = atts.getValue("id")
            }
            "title" -> {
                buildString()
            }
            "artist" -> {
                inArtist = true
                releaseArtist = ReleaseArtist()
                releaseArtist!!.mbid = atts.getValue("id")
            }
            "name" -> {
                buildString()
            }
            "date" -> {
                buildString()
            }
            "country" -> {
                buildString()
            }
            "track-list" -> {
                val num = atts.getValue("count")
                val tracks = num.toInt()
                release!!.tracksNum = release!!.tracksNum + tracks
            }
            "label" -> {
                inLabel = true
            }
            "format" -> {
                buildString()
            }
            "medium" -> {
                inMedium = true
            }
            "sort-name" -> {
                buildString()
            }
        }
    }

    @Throws(SAXException::class)
    override fun endElement(namespaceURI: String, localName: String, qName: String) {
        when {
            localName == "release" -> {
                results.add(release!!)
            }
            localName == "title" && !inMedium -> {
                release!!.title = getString()
            }
            localName == "artist" -> {
                inArtist = false
            }
            localName == "name" && inArtist -> {
                releaseArtist!!.name = getString()
                release!!.addArtist(releaseArtist!!)
            }
            localName == "name" && inLabel -> {
                release!!.addLabel(getString())
            }
            localName == "date" -> {
                release!!.date = getString()
            }
            localName == "country" -> {
                release!!.countryCode = getString().uppercase(Locale.getDefault())
            }
            localName == "label" -> {
                inLabel = false
            }
            localName == "format" -> {
                release!!.addFormat(getString())
            }
            localName == "medium" -> {
                inMedium = false
            }
            localName == "sort-name" && inArtist -> {
                releaseArtist!!.sortName = getString()
            }
        }
    }
}