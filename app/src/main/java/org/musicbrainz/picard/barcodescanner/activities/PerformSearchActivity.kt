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
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.data.Release
import org.musicbrainz.picard.barcodescanner.data.ReleaseSearchResponse
import org.musicbrainz.picard.barcodescanner.databinding.ActivityPerformSearchBinding
import org.musicbrainz.picard.barcodescanner.util.Constants
import org.musicbrainz.picard.barcodescanner.util.Preferences
import org.musicbrainz.picard.barcodescanner.webservice.MusicBrainzClient
import org.musicbrainz.picard.barcodescanner.webservice.PicardClient
import java.util.*

class PerformSearchActivity : AppCompatActivity() {
    private var mBarcode: String? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var binding: ActivityPerformSearchBinding
    private var mPreferences: Preferences? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBarcode = intent.extras?.getString(Constants.INTENT_EXTRA_BARCODE)

        binding.loadingText.setText(R.string.loading_musicbrainz_text)
        uiScope.launch {
            search()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu items for use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        uiScope.launch {
            search()
        }
    }

    private suspend fun search() {
        val result: ReleaseSearchResponse
        try {
            result = musicBrainzClient.queryReleasesByBarcode(mBarcode)
            if (result.releases.isEmpty()) {
                showResultActivity(result.releases)
                return
            }
        }
        catch (e: Exception){
            Log.e(this.javaClass.name, e.message, e)
            showResultActivityWithError(e.message)
            return
        }
        if (sendToPicard(result.releases)) {
            showResultActivity(result.releases)
        } else {
            configurePicard()
        }
    }

    private suspend fun sendToPicard(releases: List<Release>) : Boolean {
        binding.loadingText.setText(R.string.loading_picard_text)
        val picardClient = PicardClient(preferences.ipAddress!!, preferences.port)
        var status = false
        for (release in releases) {
            val result = picardClient.openRelease(release.id!!)
            if (result) {
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

    private fun showResultActivity(releases: List<Release>) {
        val resultIntent = Intent(
            this@PerformSearchActivity,
            ResultActivity::class.java
        )
        val numberOfReleases = releases.size
        val releaseTitles = arrayOfNulls<String>(numberOfReleases)
        val releaseArtists = arrayOfNulls<String>(numberOfReleases)
        val releaseDates = arrayOfNulls<String>(numberOfReleases)
        for (i in 0 until numberOfReleases) {
            val release: Release = releases[i]
            releaseTitles[i] = release.title
            releaseArtists[i] = release.releaseArtist
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

    private val musicBrainzClient: MusicBrainzClient by lazy {
        MusicBrainzClient()
    }
    private val preferences: Preferences
        get() {
            if (mPreferences == null) mPreferences = Preferences(this)
            return mPreferences!!
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        return when (item.itemId) {
            R.id.action_about -> {
                val aboutIntent = Intent(
                    this,
                    AboutActivity::class.java
                )
                startActivity(aboutIntent)
                true
            }
            R.id.action_sponsor -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SPONSOR_URL))
                startActivity(browserIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
