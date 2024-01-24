Picard Barcode Scanner
======================
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/phw/PicardBarcodeScanner)](https://github.com/phw/PicardBarcodeScanner/releases)
[![F-Droid](https://img.shields.io/f-droid/v/org.musicbrainz.picard.barcodescanner)](https://f-droid.org/de/packages/org.musicbrainz.picard.barcodescanner/)
[![PlayStore Downloads](https://PlayBadges.pavi2410.me/badge/downloads?id=org.musicbrainz.picard.barcodescanner)](https://play.google.com/store/apps/details?id=org.musicbrainz.picard.barcodescanner)


Description
-----------
Picard Barcode Scanner helps you to tag your physical releases with
[MusicBrainz Picard](http://picard.musicbrainz.org/). It allows you to scan the barcode of e.g. a
CD and have the corresponding metadata from MusicBrainz automatically loaded into Picard on your desktop.

This is especially useful if you have your physical music collection already digitalized and want
to tag the files using the correct album.


Requirements
------------
 * Picard 1.0 or higher
 * Android 7.0 or higher


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
 3. In Picard open [Options > Advanced > Network](https://picard-docs.musicbrainz.org/en/config/options_network.html)
    and enable "Browser Integration" and disable "Listen only on localhost". It is recommended to
    leave the port on the default 8000.
 4. Start Picard Barcode Scanner on your phone.
 5. On the first start the app will ask you for the IP address and port number Picard is listening
    on. Just enter the IP or network name of your computer. For the port the default 8000 should
    usually be ok. In case you have multiple instances of Picard running on your computer it might
    be 8001 or higher. You can see the port Picard is listening on in the lower right of the Picard
    main window, where it will be displayed with e.g. "Listening on port 8000".
    
    ![Picard status bar showing the listening port number](https://picard-docs.musicbrainz.org/en/_images/picard-status-bar.png)
    
 6. Use the app to scan the barcode of a music album. If the album is in the MusicBrainz database
    and your Picard connection settings are correct the album data will automatically be loaded
    into Picard.
 7. Tag your files in Picard with the loaded album as explained in the
    [Picard documentation](https://picard-docs.musicbrainz.org/en/usage/match.html).


Alternatives
------------
The official [MusicBrainz for Android](https://github.com/metabrainz/musicbrainz-android) app now also includes
the ability to send found releases to Picard.
See [Loading releases with MusicBrainz for Android](https://picard-docs.musicbrainz.org/en/tutorials/android_app.html)
in the Picard documentation.


Translations
------------
You can help translate this project into your language. Please visit the Picard Barcode Scanner
localization project on https://translations.metabrainz.org/projects/picard-barcode-scanner/


License
-------
Picard Barcode Scanner is free software published under the GPL version 3. See LICENSE for details.<br>
Copyright © 2012-2014, 2020-2024 Philipp Wolfer <ph.wolfer@gmail.com><br>
Copyright © 2021 Akshat Tiwari

The Picard Barcode Scanner by Philipp Wolfer is licensed under the
[Creative Commons Attribution-ShareAlike 4.0 International (CC BY-SA 4.0)](https://creativecommons.org/licenses/by-sa/4.0/)
license.

The MusicBrainz Picard icon by the MetaBrainz Foundation is licensed under the
[Creative Commons Attribution-ShareAlike 4.0 International (CC BY-SA 4.0)](https://creativecommons.org/licenses/by-sa/4.0/)
license.

The user interface uses the Android Developer Icons from https://github.com/opoloo/androidicons,
licensed under the [Creative Commons Attribution-ShareAlike 4.0 International (CC BY-SA 4.0)](https://creativecommons.org/licenses/by-sa/4.0/)
license.

Loading animation by [杜磊 on LottieFiles](https://lottiefiles.com/7290-music-play)
licensed under the [Creative Commons Attribution 2.0 Generic (CC BY 2.0)](https://creativecommons.org/licenses/by/2.0/) license.
