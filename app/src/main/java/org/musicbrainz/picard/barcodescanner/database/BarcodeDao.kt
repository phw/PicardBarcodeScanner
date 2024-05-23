package org.musicbrainz.picard.barcodescanner.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.musicbrainz.picard.barcodescanner.data.Barcode

/**
 * Data Access Object for the Barcode table.
 */
@Dao
interface BarcodeDao {

    /**
     * Insert a new barcode into the database.
     */
    @Insert
    suspend fun insertBarcode(barcode: Barcode)

    /**
     * Update an existing barcode in the database.
     */
    @Update
    suspend fun updateBarcode(barcode: Barcode)

    /**
     * Query all barcodes stored in the database.
     */
    @Query("SELECT * FROM Barcode")
    suspend fun getAllBarcodes(): List<Barcode>

    /**
     * Query a barcode by its value.
     */
    @Query("SELECT * FROM Barcode WHERE value = :value")
    suspend fun getBarcodeByValue(value: String): Barcode?
}
