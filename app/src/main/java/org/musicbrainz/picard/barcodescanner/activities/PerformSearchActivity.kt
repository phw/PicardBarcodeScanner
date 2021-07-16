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
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.data.BarcodeReleaseResponse
import org.musicbrainz.picard.barcodescanner.data.ReleaseInfo
import org.musicbrainz.picard.barcodescanner.util.Constants
import org.musicbrainz.picard.barcodescanner.webservice.MusicBrainzClient
import org.musicbrainz.picard.barcodescanner.webservice.PicardClient
import java.util.*

class PerformSearchActivity : BaseActivity() {
    private var mBarcode: String? = null
    private var mLoadingTextView: TextView? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSubView(R.layout.activity_perform_search)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        mLoadingTextView = findViewById<View>(R.id.loading_text) as TextView
        mLoadingTextView!!.setText(R.string.loading_musicbrainz_text)
        handleIntents()
        uiScope.launch {
            search()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        uiScope.launch {
            search()
        }
    }

    override fun handleIntents() {
        super.handleIntents()
        val extras: Bundle? = intent.extras
        if (extras != null) {
            mBarcode = extras.getString(Constants.INTENT_EXTRA_BARCODE)
        }
    }

    private suspend fun search() {
        val result: BarcodeReleaseResponse
        try {
            result = MusicBrainzClient().instance.lookupReleaseWithBarcode(getString(R.string.barcode)+mBarcode!!)
        }
        catch (e: Exception){
            Log.e(this.javaClass.name, e.message, e)
            showResultActivityWithError(e.message)
            return
        }
        if(sendToPicard(result.releases)){
            showResultActivity(result.releases)
        }
        else{
            configurePicard()
        }
    }

    private suspend fun sendToPicard(releases: List<ReleaseInfo>) : Boolean {
        mLoadingTextView!!.setText(R.string.loading_picard_text)
        val client = PicardClient(preferences.ipAddress!!, preferences.port)
        var status = false
        for (release in releases) {
            val result = client.openRelease(release.id!!)
            if(result){
                status = true
            }
        }
        return status
    }

    private fun showResultActivityWithError(errorMessage: String?) {
        val resultIntent = Intent(
            this@PerformSearchActivity,
            ResultActivity::class.java
        )
        resultIntent.putExtra(
            Constants.INTENT_EXTRA_ERROR,
            errorMessage
        )
        startActivity(resultIntent)
        finish()
    }

    private fun showResultActivity(releases: List<ReleaseInfo>) {
        val resultIntent = Intent(
            this@PerformSearchActivity,
            ResultActivity::class.java
        )
        val numberOfReleases = releases.size
        val releaseTitles = arrayOfNulls<String>(numberOfReleases)
        val releaseArtists = arrayOfNulls<String>(numberOfReleases)
        val releaseDates = arrayOfNulls<String>(numberOfReleases)
        for (i in 0 until numberOfReleases) {
            val release: ReleaseInfo = releases[i]
            releaseTitles[i] = release.title
            releaseArtists[i] = getArtistName(release)
            releaseDates[i] = release.date
        }
        resultIntent.putExtra(
            Constants.INTENT_EXTRA_BARCODE,
            mBarcode
        )
        resultIntent.putExtra(
            Constants.INTENT_EXTRA_RELEASE_TITLES,
            releaseTitles
        )
        resultIntent.putExtra(
            Constants.INTENT_EXTRA_RELEASE_ARTISTS,
            releaseArtists
        )
        resultIntent.putExtra(
            Constants.INTENT_EXTRA_RELEASE_DATES,
            releaseDates
        )
        startActivity(resultIntent)
        finish()
    }

    private fun configurePicard() {
        val configurePicard = Intent(
            this@PerformSearchActivity,
            PreferencesActivity::class.java
        )
        configurePicard.putExtra(
            Constants.INTENT_EXTRA_BARCODE,
            mBarcode
        )
        startActivity(configurePicard)
    }

    private fun getArtistName(release: ReleaseInfo): String {
        val artistNames = ArrayList<String>()
        for (artist in release.artists) {
            artistNames.add(artist.name!!)
        }
        return TextUtils.join(", ", artistNames)
    }
}