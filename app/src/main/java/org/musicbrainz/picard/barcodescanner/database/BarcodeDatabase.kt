package org.musicbrainz.picard.barcodescanner.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.musicbrainz.picard.barcodescanner.data.Barcode

@Database(entities = [Barcode::class], version = 1)
abstract class BarcodeDatabase : RoomDatabase() {
    abstract fun barcodeDao(): BarcodeDao
}
