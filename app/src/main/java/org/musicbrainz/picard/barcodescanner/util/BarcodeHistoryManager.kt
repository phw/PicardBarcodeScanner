package org.musicbrainz.picard.barcodescanner.util

import org.musicbrainz.picard.barcodescanner.data.Barcode
import org.musicbrainz.picard.barcodescanner.database.BarcodeDao

/**
 * Manager class to handle barcode data operations.
 */
class BarcodeHistoryManager(private val barcodeDao: BarcodeDao) {

    /**
     * Adds a new barcode scan to the history.
     */
    suspend fun addBarcodeScan(barcode: Barcode) {
        barcodeDao.insertBarcode(barcode)
    }

    /**
     * Retrieves all barcode scans from the history.
     */
    suspend fun getAllBarcodes(): List<Barcode> {
        return barcodeDao.getAllBarcodes()
    }
}
