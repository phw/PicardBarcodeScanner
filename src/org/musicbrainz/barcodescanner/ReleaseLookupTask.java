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
import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseStub;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ReleaseLookupTask extends AsyncTask<String, Integer, ReleaseStub[]> {

	private Context packageContext;
	private TaskCallback<ReleaseStub[]> callback;

	public ReleaseLookupTask(Context packageContext,
			TaskCallback<ReleaseStub[]> callback) {
		this.packageContext = packageContext;
		this.callback = callback;
	}

	@Override
	protected ReleaseStub[] doInBackground(String... params) {
		MusicBrainzWebClient mbClient = new MusicBrainzWebClient(
				packageContext.getString(R.string.webservice_user_agent));
		
		try {
			String barcode = params[0];
			// TODO: We should allow multiple results.
			String searchTerm = String.format("barcode:%s", barcode);
			LinkedList<ReleaseStub> releases = mbClient.searchRelease(searchTerm);
			ReleaseStub[] releaseArray = new ReleaseStub[releases.size()];
			return releases.toArray(releaseArray);
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
			// TODO: Handle error (error callback?)
			return new ReleaseStub[] {};
		}
	}

	@Override
	protected void onPostExecute(ReleaseStub[] result) {
		callback.onResult(result);
	}
}
