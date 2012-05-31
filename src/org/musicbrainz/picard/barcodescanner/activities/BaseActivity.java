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
import org.musicbrainz.picard.barcodescanner.util.Preferences;

import com.markupartist.android.widget.ActionBar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.view.ViewStub;

public abstract class BaseActivity extends Activity {

	private Preferences mPreferences = null;

	protected void setSubView(int subView) {
		setContentView(R.layout.main);
		ViewStub content = (ViewStub) findViewById(R.id.view_content);
		content.setLayoutResource(subView);
		content.inflate();

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		Intent homeIntent = new Intent(this, ScannerActivity.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		actionBar.setHomeAction(new ActionBar.IntentAction(this, homeIntent, R.drawable.ic_menu_tags));
		actionBar.addAction(new ActionBar.IntentAction(this, new Intent(
				this, PreferencesActivity.class), R.drawable.ic_menu_settings));
		
		handleIntents();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntents();
	}
	
	protected void handleIntents() {
	}

	protected Preferences getPreferences() {
		if (mPreferences == null)
			mPreferences = new Preferences(this);

		return mPreferences;
	}

	protected boolean isRunningInEmulator() {
		return ApplicationInfo.FLAG_DEBUGGABLE != 0 &&
			("google_sdk".equals(Build.PRODUCT) || "sdk".equals(Build.PRODUCT));
	}
}