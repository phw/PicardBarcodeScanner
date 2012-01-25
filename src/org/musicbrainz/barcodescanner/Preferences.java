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

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

	private Context mContext;
	private SharedPreferences mSettings;

	public Preferences(Context packageContext) {
		mContext = packageContext;
		mSettings = packageContext.getSharedPreferences(
				Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	public int getDefaultPort() {
		String port = mContext.getString(R.string.picard_default_port);

		try {
			return Integer.parseInt(port.trim());
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	public String getIpAddress() {
		return mSettings.getString(Constants.PREFERENCE_PICARD_IP_ADDRESS, "");
	}

	public int getPort() {
		return mSettings.getInt(Constants.PREFERENCE_PICARD_PORT,
				getDefaultPort());
	}

	public void setIpAddressAndPort(String ipAddress, int port) {
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putString(Constants.PREFERENCE_PICARD_IP_ADDRESS, ipAddress);
		editor.putInt(Constants.PREFERENCE_PICARD_PORT, port);
		editor.commit();
	}
}
