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

import org.musicbrainz.picard.barcodescanner.R;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSubView(R.layout.activity_result);
		handleIntents();

		Button connectBtn = (Button) findViewById(R.id.btn_scan_barcode);
		connectBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentIntegrator integrator = new IntentIntegrator(
						ResultActivity.this);
				integrator.initiateScan();
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntents();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanResult != null) {
			String barcode = scanResult.getContents();
			if (isRunningInEmulator())
				barcode = "766929908628"; // DEBUG

			if (barcode != null) {
				Intent resultIntent = new Intent(ResultActivity.this,
						PerformSearchActivity.class);
				resultIntent.putExtra("org.musicbrainz.picard.barcode",
						barcode);
				startActivity(resultIntent);
			}
		}
	}

	private void handleIntents() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String releaseTitle = extras
					.getString("org.musicbrainz.picard.releaseTitle");
			String releaseArtist = extras
					.getString("org.musicbrainz.picard.releaseArtist");
			String releaseYear = extras
					.getString("org.musicbrainz.picard.releaseYear");

			LayoutInflater inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewGroup resultList = (ViewGroup) findViewById(R.id.result_list);
			View resultView = inflater.inflate(R.layout.widget_release_item,
					resultList);

			setViewText(resultView, R.id.release_title, releaseTitle);
			setViewText(resultView, R.id.release_artist, releaseArtist);
			setViewText(resultView, R.id.release_year, releaseYear);
		}
	}

	private void setViewText(View view, int fieldId, String text) {
		TextView textView = (TextView) view.findViewById(fieldId);
		textView.setText(text);
	}
}