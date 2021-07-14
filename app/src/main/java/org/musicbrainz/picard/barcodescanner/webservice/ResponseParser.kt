package org.musicbrainz.picard.barcodescanner.webservice

import org.musicbrainz.picard.barcodescanner.data.*
import org.musicbrainz.picard.barcodescanner.handler.*
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

class ResponseParser {
    private var factory: SAXParserFactory = SAXParserFactory.newInstance()

    init {
        factory.isNamespaceAware = true
    }

    @Throws(IOException::class)
    fun parseReleaseSearch(stream: InputStream?): LinkedList<ReleaseInfo> {
        val handler = ReleaseInfoHandler()
        parse(stream, handler)
        return handler.results
    }

    @Throws(IOException::class)
    fun parse(stream: InputStream?, handler: DefaultHandler?) {
        try {
            val parser = factory.newSAXParser()
            val reader = parser.xmlReader
            val source = InputSource(stream)
            reader.contentHandler = handler
            reader.parse(source)
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        }
    }
}