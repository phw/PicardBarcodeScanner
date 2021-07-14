package org.musicbrainz.picard.barcodescanner.handler

import org.xml.sax.helpers.DefaultHandler

open class MBHandler : DefaultHandler() {
    private var sb: StringBuilder? = null
    override fun characters(ch: CharArray, start: Int, length: Int) {
        if (sb != null) {
            for (i in start until start + length) {
                sb!!.append(ch[i])
            }
        }
    }

    protected fun buildString() {
        sb = StringBuilder()
    }

    protected fun getString() : String {
        return sb.toString()
    }
}