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
package org.musicbrainz.picard.barcodescanner.activities

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.util.Preferences

abstract class BaseActivity : AppCompatActivity() {
    private var mPreferences: Preferences? = null
    protected fun setSubView(subView: Int) {
        setContentView(R.layout.main)
        val content = findViewById<View>(R.id.view_content) as ViewStub
        content.layoutResource = subView
        content.inflate()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntents()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu items for use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        return when (item.itemId) {
            R.id.action_settings -> {
                val preferencesIntent = Intent(
                    this,
                    PreferencesActivity::class.java
                )
                startActivity(preferencesIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected open fun handleIntents() {}
    protected val preferences: Preferences
        get() {
            if (mPreferences == null) mPreferences = Preferences(this)
            return mPreferences!!
        }
    protected val isRunningInEmulator: Boolean
        get() = ApplicationInfo.FLAG_DEBUGGABLE != 0 &&
                ("google_sdk" == Build.PRODUCT || "sdk" == Build.PRODUCT || "sdk_x86" == Build.PRODUCT || "sdk_gphone" == Build.PRODUCT || "sdk_gphone_x86" == Build.PRODUCT)
}