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
import android.view.View
import android.widget.TextView
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.data.ReleaseInfo
import org.musicbrainz.picard.barcodescanner.tasks.ReleaseLookupTask
import org.musicbrainz.picard.barcodescanner.tasks.SendToPicardTask
import org.musicbrainz.picard.barcodescanner.tasks.TaskCallback
import org.musicbrainz.picard.barcodescanner.util.Constants
import java.util.*

class PerformSearchActivity : BaseActivity() {
    private var mBarcode: String? = null
    private var mLoadingTextView: TextView? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSubView(R.layout.activity_perform_search)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        mLoadingTextView = findViewById<View>(R.id.loading_text) as TextView
        mLoadingTextView!!.setText(R.string.loading_musicbrainz_text)
        handleIntents()
        search()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        search()
    }

    override fun handleIntents() {
        super.handleIntents()
        val extras: Bundle? = intent.extras
        if (extras != null) {
            mBarcode = extras.getString(Constants.INTENT_EXTRA_BARCODE)
        }
    }

    private fun search() {
        val lookupCallback: TaskCallback<Array<ReleaseInfo>> =
            object : TaskCallback<Array<ReleaseInfo>> {
                override fun onResult(result: Array<ReleaseInfo>) {
                    sendToPicard(result)
                }
            }
        mLoadingTextView!!.setText(R.string.loading_musicbrainz_text)
        val task = ReleaseLookupTask()
        task.callback = lookupCallback
        // TODO: Handle error
        // task.setErrorCallback(errorCallback);
        task.execute(mBarcode)
    }

    private fun sendToPicard(releases: Array<ReleaseInfo>) {
        val sendToPicardCallback: TaskCallback<Iterator<ReleaseInfo?>?> =
            object : TaskCallback<Iterator<ReleaseInfo?>?> {
                override fun onResult(result: Iterator<ReleaseInfo?>?) {
                    val resultIntent = Intent(
                        this@PerformSearchActivity,
                        ResultActivity::class.java
                    )
                    val numberOfReleases = releases.size
                    val releaseTitles = arrayOfNulls<String>(numberOfReleases)
                    val releaseArtists = arrayOfNulls<String>(numberOfReleases)
                    val releaseDates = arrayOfNulls<String>(numberOfReleases)
                    for (i in 0 until numberOfReleases) {
                        val release: ReleaseInfo? = releases.get(i)
                        releaseTitles[i] = release!!.title
                        releaseArtists[i] = getArtistName(release)
                        releaseDates[i] = release.date
                    }
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
            }
        val errorCallback: TaskCallback<Exception> = object : TaskCallback<Exception> {
            override fun onResult(result: Exception) {
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
        }
        mLoadingTextView!!.setText(R.string.loading_picard_text)
        val task = SendToPicardTask(preferences)
        task.callback = sendToPicardCallback
        task.errorCallback = errorCallback
        task.execute(*releases)
    }

    private fun getArtistName(release: ReleaseInfo): String {
        val artistNames = ArrayList<String>()
        for (artist in release.artists) {
            artistNames.add(artist.name!!)
        }
        return TextUtils.join(", ", artistNames)
    }
}