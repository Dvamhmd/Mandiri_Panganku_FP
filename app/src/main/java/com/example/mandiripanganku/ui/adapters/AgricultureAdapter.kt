package com.example.mandiripanganku.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.agriculture.AgricultureProject
import coil.load
import com.example.mandiripanganku.activities.projectupdate.EditAgricultureActivity
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class AgricultureAdapter(
    private val context: Context, // Add context parameter
    private val agricultureList: List<AgricultureProject>
) : RecyclerView.Adapter<AgricultureAdapter.AgricultureViewHolder>() {

    class AgricultureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameText: TextView = itemView.findViewById(R.id.itemNameText)
        val itemQuantityText: TextView = itemView.findViewById(R.id.itemQuantityText)
        val itemStartDateText: TextView = itemView.findViewById(R.id.itemStartDateText)

        val plantName: TextView = itemView.findViewById(R.id.animalName) // Ganti ID sesuai layout item
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val plantPhoto: ImageView = itemView.findViewById(R.id.animalPhoto)
        val plantingDate: TextView = itemView.findViewById(R.id.startDate) // Ganti ID sesuai layout item
        val lastUpdated: TextView = itemView.findViewById(R.id.lastUpdated) // Ganti ID sesuai layout item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgricultureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_item_list_layout, parent, false)
        return AgricultureViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgricultureViewHolder, position: Int) {
        holder.itemNameText.text = context.getString(R.string.nama_tanaman)
        holder.itemQuantityText.text = context.getString(R.string.jumlah_tanaman)
        holder.itemStartDateText.text = context.getString(R.string.tanggal_tanam)

        val agriculture = agricultureList[position]

        holder.plantName.text = agriculture.plantName
        holder.quantity.text = agriculture.quantity.toString() // Hanya menampilkan angka
        holder.plantingDate.text = agriculture.plantingDate // Tanggal penanaman
        holder.lastUpdated.text = formatTimestamp(agriculture.updatedAt) // Mengonversi lastUpdated

        holder.plantPhoto.load(agriculture.plantPhoto) {
            placeholder(R.drawable.palceholder_load_image)
            when(agriculture.plantName){
                "Tomat" -> error(R.drawable.tomat_full)
                "Kentang" -> error(R.drawable.kentang_full)
                "Bayam" -> error(R.drawable.bayam_full)
                else -> error(R.drawable.error_broken_image)
            }
        }

        // Set click listener for the item view
        holder.itemView.setOnClickListener {
            // Start the Edit Activity
            val intent = Intent(context, EditAgricultureActivity::class.java).apply {
                putExtra("agriculture_id", agriculture.agricultureId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return agricultureList.size
    }

    // Fungsi untuk mengonversi Any? ke String
    private fun formatTimestamp(timestamp: Any?): String {
        return if (timestamp is Timestamp) {
            val date = timestamp.toDate() // Mengonversi Timestamp ke Date
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Format tanggal
            dateFormat.format(date) // Mengonversi Date ke String
        } else {
            "Tanggal tidak tersedia" // Pesan default jika timestamp tidak valid
        }
    }
}