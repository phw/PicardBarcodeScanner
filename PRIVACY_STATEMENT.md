# Picard Barcode Scanner – Privacy Statement
**Last updated: November 2025**

This privacy statement applies to the Android app **Picard Barcode Scanner** as published by me,
*Philipp Wolfer*, on  the Google Play Store and as source/APK via GitHub:

<https://github.com/phw/PicardBarcodeScanner>

It does **not** apply to any version of the app distributed by a third party. If you received the
app from elsewhere, please refer to that provider’s privacy policy.

Picard Barcode Scanner is my personal open source project, that I develop in my spare time for my
personal use. I make it available to you without any intent of making profit, in the hope that it
will be useful, but without any warranty; without even the implied warranty of merchantability or
fitness for a particular purpose.


## 1. Summary: No Personal Data Collected or Stored

Picard Barcode Scanner does **not collect, process, store, or share any personal data**.

- No analytics
- No telemetry
- No advertising
- No device identifiers
- No cookies
- No user accounts
- No profile data

The app operates entirely on your device except when:

1. Performing a barcode lookup on MusicBrainz, or
2. Sending a MusicBrainz Release ID to **MusicBrainz Picard running on your own local network**, on 
   a local IP address you have configured in the app’s preferences.


## 2. Data Transmitted During Barcode Lookup

When you scan a barcode, the app sends **only the scanned barcode number** to the MusicBrainz
service to look up the corresponding release.

As part of any HTTPS request, your device’s **IP address** is transmitted to the server. This is
handled directly by **MetaBrainz**, the operator of MusicBrainz. The app does **not** receive,
store, forward, or process your IP address or any other personal information.

MusicBrainz is an open, community managed music database, which  makes all data available under
free and open licenses. MusicBrainz is operated by the [MetaBrainz Foundation](https://metabrainz.org/),
a California based 501(c)(3) tax-exempt non-profit corporation dedicated to keeping MusicBrainz
[free and open source](https://musicbrainz.org/doc/About/Data_License). For details on how
MusicBrainz handles server logs and other data, please refer to the MetaBrainz Privacy Policy:

<https://metabrainz.org/privacy>

Picard Barcode Scanner is an independent personal project
and is **not endorsed** by the MetaBrainz Foundation.


## 3. Communication with MusicBrainz Picard on Your Local Network

You may configure the app to send the scanned **MusicBrainz Release ID** to your own
instance of **MusicBrainz Picard** running on the same local network.

- You explicitly enter the **IP address** or hostname of your Picard installation.
- This address is stored **locally on your device** as part of the app’s settings.
- The app sends only the Release ID to your Picard instance.
- No personal data is transmitted, processed, or stored by the app as part of this feature.
- All communication happens **only within your local network**, directly between your device and
  your own Picard installation.

No data from this feature is shared with me, MetaBrainz, or any third party.


## 4. Permissions Used

### **Camera**
Required to detect and scan EAN or UPC barcodes. Camera frames are processed **in memory only**,
used exclusively for barcode detection, and are **not stored** or **transmitted**.

### **Internet**
Required for:
- Querying the MusicBrainz database
- Communicating with MusicBrainz Picard on your local network (if configured)

Only the barcode number or the resulting Release ID is transmitted. No user accounts or
authentication are required.


## 5. GDPR / Data Protection Information

Picard Barcode Scanner does not process personal data and therefore does not act as a
data controller under GDPR.

Any personal data that may incidentally be processed during a MusicBrainz lookup (such as your
device’s IP address as  part of a standard HTTPS request) is handled solely by MetaBrainz according
to their own privacy policy.

The app stores only user configuration data (for example, the IP address or hostname of your local
Picard installation).  This data remains **entirely on your device**, is not transmitted to me or
any third party, and cannot be used by me to identify you.

Local network IP addresses (such as 192.168.x.x) used for connecting to your own Picard
installation are **generally not considered personal data under GDPR**.

The app performs no background requests beyond those explicitly described above and only
communicates with external services when you initiate a barcode lookup or when you choose to send a
Release ID to your own Picard installation.


## 6. Source Code Transparency

Picard Barcode Scanner is free and open source software, licensed under the terms of the
[GNU General Public License v3.0 (or later)](https://www.gnu.org/licenses/gpl-3.0.en.html).

The versions distributed on the Google Play Store and on GitHub are built directly from the
publicly  available source code without additional modifications.

You can inspect the full source here:

<https://github.com/phw/PicardBarcodeScanner>

If you notice anything in the source code that concerns your privacy, please let me know.


## 7. Contact

If you have any questions or concerns regarding the privacy or data usage of this app, please open
an issue on GitHub:

<https://github.com/phw/PicardBarcodeScanner/issues>

Or contact me directly:

**Philipp Wolfer**  
**Email:** ph.wolfer@gmail.com
