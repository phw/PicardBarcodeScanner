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

import org.musicbrainz.android.api.data.Release;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;

import android.content.Context;
import android.os.AsyncTask;

public class ReleaseLookupTask extends AsyncTask<String, Integer, Release> {

	private Context packageContext;
	private TaskCallback<Release> callback;
	
	public ReleaseLookupTask(Context packageContext, TaskCallback<Release> callback) {
		this.packageContext = packageContext;
		this.callback = callback;
	}
	
	@Override
	protected Release doInBackground(String... params) {
		MusicBrainzWebClient mbClient = new MusicBrainzWebClient(
				packageContext.getString(R.string.webservice_user_agent));
		Release release = null;

		try {
			String barcode = params[0];
			release = mbClient.lookupReleaseUsingBarcode(barcode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  release;
	}

	protected void onPostExecute(Release result) {
        callback.onResult(result);
    }
}
