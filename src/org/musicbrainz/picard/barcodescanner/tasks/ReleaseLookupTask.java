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

package org.musicbrainz.picard.barcodescanner.tasks;

import java.io.IOException;
import java.util.LinkedList;

import org.musicbrainz.android.api.data.ReleaseInfo;
import org.musicbrainz.android.api.webservice.MusicBrainzWebClient;
import org.musicbrainz.picard.barcodescanner.R;

import android.content.Context;
import android.util.Log;

public class ReleaseLookupTask extends AsyncCallbackTask<String, Integer, ReleaseInfo[]> {

	private Context mPackageContext;
	
	public ReleaseLookupTask(Context packageContext) {
		mPackageContext = packageContext;
	}

	@Override
	protected ReleaseInfo[] doInBackground(String... params) {
		MusicBrainzWebClient mbClient = new MusicBrainzWebClient(
				mPackageContext.getString(R.string.webservice_user_agent));
		
		try {
			String barcode = params[0];
			String searchTerm = String.format("barcode:%s", barcode);
			LinkedList<ReleaseInfo> releases = mbClient.searchRelease(searchTerm);
			ReleaseInfo[] releaseArray = new ReleaseInfo[releases.size()];
			return releases.toArray(releaseArray);
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
			this.onError(e);
			return new ReleaseInfo[] {};
		}
	}
}
