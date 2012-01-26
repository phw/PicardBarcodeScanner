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

package org.musicbrainz.picard.barcodescanner.activities;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.picard.barcodescanner.R;
import org.musicbrainz.picard.barcodescanner.tasks.ReleaseLookupTask;
import org.musicbrainz.picard.barcodescanner.tasks.SendToPicardTask;
import org.musicbrainz.picard.barcodescanner.tasks.TaskCallback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class PerformSearchActivity extends BaseActivity {
	private String barcode;
	
	private TextView loadingTextView;

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
		
		loadingTextView = (TextView) findViewById(R.id.loading_text);
		loadingTextView.setText(R.string.loading_musicbrainz_text);
		
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
			this.barcode = extras.getString("org.musicbrainz.picard.barcode");
		}
	}

	private void search() {
		TaskCallback<ReleaseStub[]> lookupCallback = new TaskCallback<ReleaseStub[]>() {

			@Override
			public void onResult(ReleaseStub[] releases) {
				sendToPicard(releases);
			}
		};

		loadingTextView.setText(R.string.loading_musicbrainz_text);
		new ReleaseLookupTask(this, lookupCallback).execute(this.barcode);
	}

	protected void sendToPicard(ReleaseStub[] releases) {
		TaskCallback<ReleaseStub[]> sendToPicardCallback = new TaskCallback<ReleaseStub[]>() {

			@Override
			public void onResult(ReleaseStub[] releases) {
				Intent resultIntent = new Intent(PerformSearchActivity.this,
						ResultActivity.class);

				// FIXME: Handle multiple results
				ReleaseStub release = releases[0];

				if (release != null) {
					resultIntent.putExtra(
							"org.musicbrainz.picard.releaseTitle",
							release.getTitle());
					resultIntent.putExtra(
							"org.musicbrainz.picard.releaseArtist", release
									.getArtistName());
					resultIntent.putExtra(
							"org.musicbrainz.picard.releaseYear",
							release.getDate());
				}
				startActivity(resultIntent);
			}
		};

		loadingTextView.setText(R.string.loading_picard_text);
		new SendToPicardTask(getPreferences(), sendToPicardCallback)
				.execute(releases);
	}
}