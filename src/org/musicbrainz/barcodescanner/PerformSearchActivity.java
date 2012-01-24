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

package org.musicbrainz.barcodescanner;

import org.musicbrainz.android.api.data.Release;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PerformSearchActivity extends BaseActivity {
	private String barcode;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSubView(R.layout.activity_perform_search);
		handleIntents();

		// Start animation
		View spinner = findViewById(R.id.spinner);
		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.spinner);
		spinner.startAnimation(rotation);

		search();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntents();
		search();
	}

	private void handleIntents() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.barcode = extras.getString("org.musicbrainz.android.barcode");
		}
	}

	private void search() {
		TaskCallback<Release> lookupCallback = new TaskCallback<Release>() {

			@Override
			public void onResult(Release release) {
				// TODO: Send Result to Picard
				Intent resultIntent = new Intent(PerformSearchActivity.this,
						ResultActivity.class);
				if (release != null) {
					resultIntent.putExtra(
							"org.musicbrainz.android.releaseTitle",
							release.getTitle());
					resultIntent.putExtra(
							"org.musicbrainz.android.releaseArtist", release
									.getArtists().get(0).getName());
					resultIntent.putExtra(
							"org.musicbrainz.android.releaseYear",
							release.getDate());
				}
				startActivity(resultIntent);
			}
		};

		new ReleaseLookupTask(this, lookupCallback).execute(this.barcode);
	}
}