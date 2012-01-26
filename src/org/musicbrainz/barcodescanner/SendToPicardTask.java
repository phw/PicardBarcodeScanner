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

import java.io.IOException;

import org.musicbrainz.android.api.data.ReleaseStub;

import android.os.AsyncTask;
import android.util.Log;

// TODO: Common superclass for all callback AsyncTasks
public class SendToPicardTask extends AsyncTask<ReleaseStub, Integer, ReleaseStub[]> {

	private Preferences preferences;
	private TaskCallback<ReleaseStub[]> callback;

	public SendToPicardTask(Preferences preferences,
			TaskCallback<ReleaseStub[]> callback) {
		this.preferences = preferences;
		this.callback = callback;
	}

	@Override
	protected ReleaseStub[] doInBackground(ReleaseStub... params) {
		PicardClient client = new PicardClient(preferences.getIpAddress(),
				preferences.getPort());

		try {
			for (ReleaseStub release : params) {
				client.openRelease(release.getReleaseMbid());
			}
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
			// TODO: Handle error (error callback?)
		}

		return params;
	}

	@Override
	protected void onPostExecute(ReleaseStub[] result) {
		callback.onResult(result);
	}
}
