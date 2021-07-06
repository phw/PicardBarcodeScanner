package org.musicbrainz.picard.barcodescanner.util

import java.util.*

object StringFormat {
    fun initialCaps(text: String?): String {
        val tokenizer = StringTokenizer(text, " ", true)
        val sb = StringBuilder()
        while (tokenizer.hasMoreTokens()) {
            var token = tokenizer.nextToken()
            token = String.format("%s%s", Character.toUpperCase(token[0]), token.substring(1))
            sb.append(token)
        }
        return sb.toString()
    }

    fun formatDuration(durationSeconds: Int): String {

        // TODO: Would be much cleaner using String.format().
        if (durationSeconds == 0) {
            return ""
        }
        val s = durationSeconds / 1000
        val secs = s % 60
        val mins = s % 3600 / 60
        val hrs = s / 3600
        var mS = "" + mins
        var sS = "" + secs
        if (secs < 10) sS = "0$secs"
        return if (hrs == 0) {
            "$mS:$sS"
        } else {
            if (mins < 10) mS = "0$mins"
            "$hrs:$mS:$sS"
        }
    }
}