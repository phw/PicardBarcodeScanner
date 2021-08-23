/*
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
package org.musicbrainz.picard.barcodescanner.activities

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity() {
    private lateinit var binding: ActivityAboutBinding

    private val contentMappings = mapOf(
        R.string.about_copyright to R.id.about_copyright,
        R.string.about_license_1 to R.id.about_license_1,
        R.string.about_license_2 to R.id.about_license_2,
        R.string.about_picard_icon to R.id.about_picard_icon,
        R.string.about_icons to R.id.about_icons,
        R.string.about_lottie to R.id.about_lottie,
    )

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        binding.applicationVersion.text = getString(R.string.app_version, versionName)
        for ((rText, rView) in contentMappings) {
            val infoTextView = findViewById<View>(rView) as TextView
            infoTextView.text = Html.fromHtml(getString(rText), Html.FROM_HTML_MODE_LEGACY)
            infoTextView.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}