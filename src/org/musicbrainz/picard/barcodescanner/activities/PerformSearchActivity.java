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
import org.musicbrainz.picard.barcodescanner.util.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class PerformSearchActivity extends BaseActivity {
	private String mBarcode;

	private TextView mLoadingTextView;

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

		mLoadingTextView = (TextView) findViewById(R.id.loading_text);
		mLoadingTextView.setText(R.string.loading_musicbrainz_text);

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
			mBarcode = extras.getString(Constants.INTENT_EXTRA_BARCODE);
		}
	}

	private void search() {
		TaskCallback<ReleaseStub[]> lookupCallback = new TaskCallback<ReleaseStub[]>() {

			@Override
			public void onResult(ReleaseStub[] releases) {
				sendToPicard(releases);
			}
		};

		mLoadingTextView.setText(R.string.loading_musicbrainz_text);
		new ReleaseLookupTask(this, lookupCallback).execute(mBarcode);
	}

	protected void sendToPicard(ReleaseStub[] releases) {
		TaskCallback<ReleaseStub[]> sendToPicardCallback = new TaskCallback<ReleaseStub[]>() {

			@Override
			public void onResult(ReleaseStub[] releases) {
				Intent resultIntent = new Intent(PerformSearchActivity.this,
						ResultActivity.class);

				int numberOfReleases = releases.length;
				String[] releaseTitles = new String[numberOfReleases];
				String[] releaseArtists = new String[numberOfReleases];
				String[] releaseDates = new String[numberOfReleases];

				for (int i = 0; i < numberOfReleases; ++i) {
					ReleaseStub release = releases[i];
					releaseTitles[i] = release.getTitle();
					releaseArtists[i] = release.getArtistName();
					releaseDates[i] = release.getDate();
				}

				resultIntent.putExtra(Constants.INTENT_EXTRA_RELEASE_TITLES,
						releaseTitles);
				resultIntent.putExtra(Constants.INTENT_EXTRA_RELEASE_ARTISTS,
						releaseArtists);
				resultIntent.putExtra(Constants.INTENT_EXTRA_RELEASE_DATES,
						releaseDates);

				startActivity(resultIntent);
			}
		};

		mLoadingTextView.setText(R.string.loading_picard_text);
		new SendToPicardTask(getPreferences(), sendToPicardCallback)
				.execute(releases);
	}
}