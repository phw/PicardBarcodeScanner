/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.zxing.integration.android

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import java.util.*

/**
 *
 * A utility class which helps ease integration with Barcode Scanner via [Intent]s. This is a simple
 * way to invoke barcode scanning and receive the result, without any need to integrate, modify, or learn the
 * project's source code.
 *
 * <h2>Initiating a barcode scan</h2>
 *
 *
 * To integrate, create an instance of `IntentIntegrator` and call [.initiateScan] and wait
 * for the result in your app.
 *
 *
 * It does require that the Barcode Scanner (or work-alike) application is installed. The
 * [.initiateScan] method will prompt the user to download the application, if needed.
 *
 *
 * There are a few steps to using this integration. First, your [Activity] must implement
 * the method [Activity.onActivityResult] and include a line of code like this:
 *
 * <pre>`public void onActivityResult(int requestCode, int resultCode, Intent intent) {
 * IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
 * if (scanResult != null) {
 * // handle scan result
 * }
 * // else continue with any other code you need in the method
 * ...
 * }
`</pre> *
 *
 *
 * This is where you will handle a scan result.
 *
 *
 * Second, just call this in response to a user action somewhere to begin the scan process:
 *
 * <pre>`IntentIntegrator integrator = new IntentIntegrator(yourActivity);
 * integrator.initiateScan();
`</pre> *
 *
 *
 * Note that [.initiateScan] returns an [AlertDialog] which is non-null if the
 * user was prompted to download the application. This lets the calling app potentially manage the dialog.
 * In particular, ideally, the app dismisses the dialog if it's still active in its [Activity.onPause]
 * method.
 *
 *
 * You can use [.setTitle] to customize the title of this download prompt dialog (or, use
 * [.setTitleByID] to set the title by string resource ID.) Likewise, the prompt message, and
 * yes/no button labels can be changed.
 *
 *
 * Finally, you can use [.addExtra] to add more parameters to the Intent used
 * to invoke the scanner. This can be used to set additional options not directly exposed by this
 * simplified API.
 *
 *
 * By default, this will only allow applications that are known to respond to this intent correctly
 * do so. The apps that are allowed to response can be set with [.setTargetApplications].
 * For example, set to [.TARGET_BARCODE_SCANNER_ONLY] to only target the Barcode Scanner app itself.
 *
 * <h2>Sharing text via barcode</h2>
 *
 *
 * To share text, encoded as a QR Code on-screen, similarly, see [.shareText].
 *
 *
 * Some code, particularly download integration, was contributed from the Anobiit application.
 *
 * <h2>Enabling experimental barcode formats</h2>
 *
 *
 * Some formats are not enabled by default even when scanning with [.ALL_CODE_TYPES], such as
 * PDF417. Use [.initiateScan] with
 * a collection containing the names of formats to scan for explicitly, like "PDF_417", to use such
 * formats.
 *
 * @author Sean Owen
 * @author Fred Lin
 * @author Isaac Potoczny-Jones
 * @author Brad Drehmer
 * @author gcstang
 */

class IntentIntegrator(private val activity: Activity) {
    var title: String
    var message: String
    var buttonYes: String
    var buttonNo: String
    private var targetApplications: List<String>
    private val moreExtras: MutableMap<String, Any>
    fun setTitleByID(titleID: Int) {
        title = activity.getString(titleID)
    }

    fun setMessageByID(messageID: Int) {
        message = activity.getString(messageID)
    }

    fun setButtonYesByID(buttonYesID: Int) {
        buttonYes = activity.getString(buttonYesID)
    }

    fun setButtonNoByID(buttonNoID: Int) {
        buttonNo = activity.getString(buttonNoID)
    }

    fun getTargetApplications(): Collection<String> {
        return targetApplications
    }

    fun setTargetApplications(targetApplications: List<String>) {
        require(targetApplications.isNotEmpty()) { "No target applications" }
        this.targetApplications = targetApplications
    }

    fun setSingleTargetApplication(targetApplication: String) {
        targetApplications = listOf(targetApplication)
    }

    fun getMoreExtras(): Map<String, *> {
        return moreExtras
    }

    fun addExtra(key: String, value: Any) {
        moreExtras[key] = value
    }
    /**
     * Initiates a scan only for a certain set of barcode types, given as strings corresponding
     * to their names in ZXing's `BarcodeFormat` class like "UPC_A". You can supply constants
     * like [.PRODUCT_CODE_TYPES] for example.
     *
     * @return the [AlertDialog] that was shown to the user prompting them to download the app
     * if a prompt was needed, or null otherwise
     */
    /**
     * Initiates a scan for all known barcode types.
     */
    @JvmOverloads
    fun initiateScan(desiredBarcodeFormats: Collection<String?>? = ALL_CODE_TYPES): AlertDialog? {
        val intentScan = Intent("$BS_PACKAGE.SCAN")
        intentScan.addCategory(Intent.CATEGORY_DEFAULT)

        // check which types of codes to scan for
        if (desiredBarcodeFormats != null) {
            // set the desired barcode types
            val joinedByComma = StringBuilder()
            for (format in desiredBarcodeFormats) {
                if (joinedByComma.isNotEmpty()) {
                    joinedByComma.append(',')
                }
                joinedByComma.append(format)
            }
            intentScan.putExtra("SCAN_FORMATS", joinedByComma.toString())
        }

        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        attachMoreExtras(intentScan)
        try {
            startActivityForResult(intentScan, REQUEST_CODE)
        } catch (ex: ActivityNotFoundException) {
            showDownloadDialog()
        }
        return null
    }

    /**
     * Start an activity. This method is defined to allow different methods of activity starting for
     * newer versions of Android and for compatibility library.
     *
     * @param intent Intent to start.
     * @param code Request code for the activity
     * @see android.app.Activity.startActivityForResult
     * @see android.app.Fragment.startActivityForResult
     */
    private fun startActivityForResult(intent: Intent?, code: Int) {
        activity.startActivityForResult(intent, code)
    }

    private fun showDownloadDialog(): AlertDialog {
        val downloadDialog = AlertDialog.Builder(
            activity
        )
        downloadDialog.setTitle(title)
        downloadDialog.setMessage(message)
        downloadDialog.setPositiveButton(buttonYes) { dialogInterface, i ->
            val packageName = targetApplications[0]
            val uri = Uri.parse("market://details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
                activity.startActivity(intent)
            } catch (anfe: ActivityNotFoundException) {
                // Hmm, market is not installed
                Log.w(TAG, "Google Play is not installed; cannot install $packageName")
            }
        }
        downloadDialog.setNegativeButton(buttonNo) { dialogInterface, i -> }
        return downloadDialog.show()
    }
    /**
     * Shares the given text by encoding it as a barcode, such that another user can
     * scan the text off the screen of the device.
     *
     * @param text the text string to encode as a barcode
     * @param type type of data to encode. See `com.google.zxing.client.android.Contents.Type` constants.
     * @return the [AlertDialog] that was shown to the user prompting them to download the app
     * if a prompt was needed, or null otherwise
     */
    /**
     * Defaults to type "TEXT_TYPE".
     * @see .shareText
     */
    @JvmOverloads
    fun shareText(text: CharSequence?, type: CharSequence? = "TEXT_TYPE"): AlertDialog? {
        val intent = Intent()
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.action = "$BS_PACKAGE.ENCODE"
        intent.putExtra("ENCODE_TYPE", type)
        intent.putExtra("ENCODE_DATA", text)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        attachMoreExtras(intent)
        try {
            activity.startActivityForResult(intent, REQUEST_CODE)
        } catch (ex: ActivityNotFoundException) {
            showDownloadDialog()
        }
        return null
    }

    private fun attachMoreExtras(intent: Intent) {
        for ((key, value) in moreExtras) {
            // Kind of hacky
            when (value) {
                is Int -> {
                    intent.putExtra(key, value)
                }
                is Long -> {
                    intent.putExtra(key, value)
                }
                is Boolean -> {
                    intent.putExtra(key, value)
                }
                is Double -> {
                    intent.putExtra(key, value)
                }
                is Float -> {
                    intent.putExtra(key, value)
                }
                is Bundle -> {
                    intent.putExtra(key, value)
                }
                else -> {
                    intent.putExtra(key, value.toString())
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 0x0000c0de // Only use bottom 16 bits
        private val TAG = IntentIntegrator::class.java.simpleName
        const val DEFAULT_TITLE = "Install Barcode Scanner?"
        const val DEFAULT_MESSAGE =
            "This application requires Barcode Scanner. Would you like to install it?"
        const val DEFAULT_YES = "Yes"
        const val DEFAULT_NO = "No"
        private const val BS_PACKAGE = "com.google.zxing.client.android"
        private const val BSPLUS_PACKAGE = "com.srowen.bs.android"

        // supported barcode formats
        val PRODUCT_CODE_TYPES: Collection<String> =
            list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "RSS_14")
        val ONE_D_CODE_TYPES: Collection<String> = list(
            "UPC_A", "UPC_E", "EAN_8", "EAN_13", "CODE_39", "CODE_93", "CODE_128",
            "ITF", "RSS_14", "RSS_EXPANDED"
        )
        val QR_CODE_TYPES: Collection<String> = setOf("QR_CODE")
        val DATA_MATRIX_TYPES: Collection<String> = setOf("DATA_MATRIX")
        val ALL_CODE_TYPES: Collection<String?>? = null
        @JvmField
        val TARGET_BARCODE_SCANNER_ONLY = listOf(BS_PACKAGE)
        @JvmField
        val TARGET_ALL_KNOWN = list(
            BSPLUS_PACKAGE,  // Barcode Scanner+
            "$BSPLUS_PACKAGE.simple",  // Barcode Scanner+ Simple
            BS_PACKAGE // Barcode Scanner
            // What else supports this intent?
        )

        private fun contains(availableApps: Iterable<ResolveInfo>, targetApp: String): Boolean {
            for (availableApp in availableApps) {
                val packageName = availableApp.activityInfo.packageName
                if (targetApp == packageName) {
                    return true
                }
            }
            return false
        }

        /**
         *
         * Call this from your [Activity]'s
         * [Activity.onActivityResult] method.
         *
         * @return null if the event handled here was not related to this class, or
         * else an [IntentResult] containing the result of the scan. If the user cancelled scanning,
         * the fields will be null.
         */
        @JvmStatic
        fun parseActivityResult(requestCode: Int, resultCode: Int, intent: Intent): IntentResult? {
            if (requestCode == REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    val contents = intent.getStringExtra("SCAN_RESULT")
                    val formatName = intent.getStringExtra("SCAN_RESULT_FORMAT")
                    val rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES")
                    val intentOrientation =
                        intent.getIntExtra("SCAN_RESULT_ORIENTATION", Int.MIN_VALUE)
                    val orientation =
                        if (intentOrientation == Int.MIN_VALUE) null else intentOrientation
                    val errorCorrectionLevel =
                        intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL")
                    return IntentResult(
                        contents,
                        formatName,
                        rawBytes,
                        orientation,
                        errorCorrectionLevel
                    )
                }
                return IntentResult()
            }
            return null
        }

        private fun list(vararg values: String): List<String> {
            return Collections.unmodifiableList(listOf(*values))
        }
    }

    init {
        title = DEFAULT_TITLE
        message = DEFAULT_MESSAGE
        buttonYes = DEFAULT_YES
        buttonNo = DEFAULT_NO
        targetApplications = TARGET_ALL_KNOWN
        moreExtras = HashMap(3)
    }
}