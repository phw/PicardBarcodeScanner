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

package org.musicbrainz.picard.barcodescanner.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class PicardClient {

	private static final String PICARD_OPENALBUM_URL = "http://%s:%d/openalbum?id=%s";

	private AbstractHttpClient httpClient;
	private String ipAddress;
	private int port;

	public PicardClient(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
		httpClient = new DefaultHttpClient();
	}

	public Boolean openRelease(String releaseId) throws IOException {
		String url = String.format(PICARD_OPENALBUM_URL, ipAddress,
				port, uriEncode(releaseId));
		HttpResponse response = get(url);
		return isResponseSuccess(response);
	}

	private HttpResponse get(String url) throws IOException {
		HttpGet get = new HttpGet(url);
		HttpResponse response = httpClient.execute(get);
		return response;
	}

	private Boolean isResponseSuccess(HttpResponse response) {
		return response.getStatusLine().getStatusCode() < 400;
	}

	private String uriEncode(String releaseId) {
		try {
			return URLEncoder.encode(releaseId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
			return URLEncoder.encode(releaseId);
		}
	}
}
