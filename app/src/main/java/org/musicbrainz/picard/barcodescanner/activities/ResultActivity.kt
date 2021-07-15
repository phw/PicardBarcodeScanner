/*
 * Copyright (C) 2012, 2021 Philipp Wolfer <ph.wolfer@gmail.com>
 * Copyright (C) 2021 Akshat Tiwari
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
package org.musicbrainz.picard.barcodescanner.activities

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.Button
import android.widget.TextView
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.util.Constants
import kotlin.math.min

class ResultActivity : BaseActivity() {
    private var descriptionTextView: TextView? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSubView(R.layout.activity_result)
        descriptionTextView = findViewById<View>(R.id.description_search_result) as TextView
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        val connectBtn = findViewById<View>(R.id.btn_scan_barcode) as Button
        connectBtn.setOnClickListener {
            val resultIntent = Intent(
                this@ResultActivity,
                ScannerActivity::class.java
            )
            resultIntent.putExtra(Constants.INTENT_EXTRA_AUTOSTART_SCANNER, true)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(resultIntent)
        }
        handleIntents()
    }

    override fun handleIntents() {
        super.handleIntents()
        var numberOfReleases = 0
        var barcode: String? = ""
        val extras = intent.extras
        if (extras != null) {
            barcode = extras.getString(Constants.INTENT_EXTRA_BARCODE)
            val errorMessage = extras.getString(Constants.INTENT_EXTRA_ERROR)
            val errorMsg = findViewById<View>(R.id.label_error) as TextView
            if (errorMessage != null) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = errorMessage
            } else {
                errorMsg.visibility = View.GONE
                val releaseTitles = extras.getStringArray(Constants.INTENT_EXTRA_RELEASE_TITLES)
                val releaseArtists = extras.getStringArray(Constants.INTENT_EXTRA_RELEASE_ARTISTS)
                val releaseDates = extras.getStringArray(Constants.INTENT_EXTRA_RELEASE_DATES)
                numberOfReleases = min(
                    min(releaseTitles!!.size, releaseArtists!!.size),
                    releaseDates!!.size
                )
                val resultList = findViewById<View>(R.id.result_list) as ViewGroup
                resultList.removeAllViews()

                val inflater =
                    applicationContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                for (i in 0 until numberOfReleases) {
                    addReleaseToView(
                        resultList, inflater, releaseTitles[i],
                        releaseArtists[i], releaseDates[i]
                    )
                }
            }
        }
        val description: String = when (numberOfReleases) {
            0 -> getString(R.string.description_no_result, barcode)
            else -> getString(R.string.description_result, barcode)
        }
        descriptionTextView!!.text = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun setViewText(view: View, fieldId: Int, text: String) {
        val textView = view.findViewById<View>(fieldId) as TextView
        textView.text = text
    }

    private fun addReleaseToView(parent: ViewGroup, inflater: LayoutInflater, releaseTitle: String, releaseArtist: String, releaseYear: String?) {
        val resultItem = inflater.inflate(
            R.layout.widget_release_item,
            parent, false
        )
        setViewText(resultItem, R.id.release_title, releaseTitle)
        setViewText(resultItem, R.id.release_artist, releaseArtist)
        if (releaseYear != null) {
            setViewText(resultItem, R.id.release_date, releaseYear)
        }
        parent.addView(resultItem)
    }
}