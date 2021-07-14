Picard Barcode Scanner
======================

Description
-----------
Picard Barcode Scanner helps you to tag your physical releases with
[MusicBrainz Picard](http://picard.musicbrainz.org/). It allows you to scan the barcode of e.g. a
CD and have the corresponding metadata from MusicBrainz automatically loaded into Picard.

This is especially useful if you have your physical music collection already digitalized and want
to tag the files using the correct album.


Requirements
------------
 * Picard 1.0 or higher
 * Android 4.0 or higher
 * [ZXing barcode scanner](https://play.google.com/store/apps/details?id=com.google.zxing.client.android)


Installation
------------
You can install the Picard Barcode Scanner from the
[Google Play Store](https://play.google.com/store/apps/details?id=org.musicbrainz.picard.barcodescanner)
or [F-Droid](https://f-droid.org/de/packages/org.musicbrainz.picard.barcodescanner/).

You can also manually download and install the APK file from the
[release page](https://github.com/phw/PicardBarcodeScanner/releases).


Usage
-----
 1. Connect both your computer and your phone to the same WiFi network.
 2. Start MusicBrainz Picard (version 1.0 or higher).
 3. In Picard open Options > Advanced > Network and enable "Browser Integration" and disable
    "Listen only on localhost". It is recommended to leave the port on the default 8000.
 4. Start Picard Barcode Scanner on your phone.
 5. On the first start the app will ask you for the IP address and port number Picard is listening
    on. Just enter the IP or network name of your computer. For the port the default 8000 should
	usually be ok. In case you have multiple instances of Picard running on your computer it might
	be 8001 or higher. You can see the port Picard is listening on in the lower right of the Picard
    main window, where it will be displayed with e.g. "Listening on port 8000".
 6. Use the app to scan the barcode of a music album. If the album is in the MusicBrainz database
    and your Picard connection settings are correct the album data will automatically be loaded
	into Picard.
 7. Tag your files in Picard with the loaded album as explained in the
    [Picard documentation](https://picard-docs.musicbrainz.org/en/usage/match.html).


Build requirements
------------------
 * Android ActionBar from https://github.com/johannilsson/android-actionbar
 * The MusicBrainz API from https://github.com/jdamcd/musicbrainz-android/


Translations
------------
You can help translate this project into your language. Please visit the Picard Barcode Scanner
localization project on https://www.transifex.com/projects/p/picard-barcode-scanner/


License
-------
Picard Barcode Scanner is free software published under the GPL version 3. See LICENSE for details.
Copyright © 2012-2014, 2020-2021 Philipp Wolfer <ph.wolfer@googlemail.com>
Copyright © 2021 Akshat Tiwari

The Picard icon Copyright © 2009 by Carlin Mangar is licensed under the
[Creative Commons Attribution-ShareAlike 2.0 UK: England & Wales](http://creativecommons.org/licenses/by-sa/2.0/uk/)
license.

The user interface uses the Android Developer Icons from https://github.com/opoloo/androidicons,
licensed under the [Creative Commons Attribution-ShareAlike 4.0 International CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/)
license.


Author
------
Philipp Wolfer <ph.wolfer@googlemail.com>
