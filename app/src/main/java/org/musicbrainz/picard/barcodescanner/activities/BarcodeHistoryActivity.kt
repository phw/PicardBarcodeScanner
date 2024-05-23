package org.musicbrainz.picard.barcodescanner.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.musicbrainz.picard.barcodescanner.R
import org.musicbrainz.picard.barcodescanner.data.Barcode
import org.musicbrainz.picard.barcodescanner.database.BarcodeDatabase
import org.musicbrainz.picard.barcodescanner.util.BarcodeHistoryManager

class BarcodeHistoryActivity : AppCompatActivity() {

    private lateinit var barcodeRecyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var barcodeHistoryManager: BarcodeHistoryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_history)

        barcodeHistoryManager = BarcodeHistoryManager(BarcodeDatabase.getInstance(application).barcodeDao())

        viewManager = LinearLayoutManager(this)
        viewAdapter = BarcodeHistoryAdapter(barcodeHistoryManager.getAllBarcodes())

        barcodeRecyclerView = findViewById<RecyclerView>(R.id.barcode_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun sendSelectedBarcodes() {
        // This function will handle the selection and transmission of barcodes to MusicBrainz and Picard
        val selectedBarcodes: List<Barcode> = barcodeHistoryManager.getSelectedBarcodes()
        // Code to send the selected barcodes to MusicBrainz and Picard goes here
    }
}
