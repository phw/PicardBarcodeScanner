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
package org.musicbrainz.picard.barcodescanner.tasks

import android.os.AsyncTask

abstract class AsyncCallbackTask<Params, Progress, Result> : AsyncTask<Params, Progress, Result>() {
    private var mError = false
    var callback: TaskCallback<Result>? = null
    var errorCallback: TaskCallback<Exception>? = null
    override fun onPostExecute(result: Result) {
        if (callback != null && !mError) {
            callback!!.onResult(result)
        }
    }

    /*
	 * Called when an error occurred.
	 * 
	 * This method should be called by the implementation whenever an error
	 * is preventing the successful termination of the task. Calling onError()
	 * will prevent the normal callback being called.
	 */
    protected fun onError(ex: Exception) {
        if (errorCallback != null) {
            mError = true
            errorCallback!!.onResult(ex)
        }
    }
}