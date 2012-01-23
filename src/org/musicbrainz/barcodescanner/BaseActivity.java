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

import com.markupartist.android.widget.ActionBar;

import android.app.Activity;
import android.content.Intent;
import android.view.ViewStub;

public abstract class BaseActivity extends Activity {

	protected void setSubView(int subView) {
		setContentView(R.layout.main);
		ViewStub content = (ViewStub) findViewById(R.id.view_content);
		content.setLayoutResource(subView);
		content.inflate();

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new ActionBar.IntentAction(this, new Intent(
				this, ConnectActivity.class), R.drawable.ic_menu_tags));
		// actionBar.setHomeLogo(R.drawable.icon);
		// actionBar.setHomeAction(new IntentAction(this, HomeActivity
		// .createIntent(this), R.drawable.ic_launcher));
		// actionBar.addAction(new IntentAction(this, createShareIntent(),
		// R.drawable.ic_title_share_default));
		// actionBar.addAction(new ToastAction());
	}

}