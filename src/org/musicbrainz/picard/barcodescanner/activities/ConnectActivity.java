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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConnectActivity extends BaseActivity {

	private EditText mIpAddressInput;
	private EditText mPortInput;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSubView(R.layout.activity_connect);

		mIpAddressInput = (EditText) findViewById(R.id.picard_ip_address);
		mPortInput = (EditText) findViewById(R.id.picard_port);
		mIpAddressInput.setText(getPreferences().getIpAddress());
		mPortInput.setText(String.valueOf(getPreferences().getPort()));

		Button connectBtn = (Button) findViewById(R.id.btn_picard_connect);
		connectBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getPreferences().setIpAddressAndPort(readIpAddressFromInput(),
						readPortFromInput());

				Intent intent = new Intent(ConnectActivity.this,
						ScannerActivity.class);
				startActivity(intent);
			}
		});
	}

	private String readIpAddressFromInput() {
		return mIpAddressInput.getText().toString();
	}

	private int readPortFromInput() {
		String port = mPortInput.getText().toString();

		try {
			return Integer.parseInt(port.trim());
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}
}