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

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewStub;

public abstract class BaseActivity extends AppCompatActivity {

	private Preferences mPreferences = null;

	protected void setSubView(int subView) {
		setContentView(R.layout.main);
		ViewStub content = (ViewStub) findViewById(R.id.view_content);
		content.setLayoutResource(subView);
		content.inflate();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleIntents();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent preferencesIntent = new Intent(this,
                        PreferencesActivity.class);
                startActivity(preferencesIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
			("google_sdk".equals(Build.PRODUCT)
					|| "sdk".equals(Build.PRODUCT)
					|| "sdk_x86".equals(Build.PRODUCT)
					|| "sdk_gphone".equals(Build.PRODUCT)
					|| "sdk_gphone_x86".equals(Build.PRODUCT));
	}
}