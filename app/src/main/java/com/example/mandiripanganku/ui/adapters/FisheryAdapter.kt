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
import com.example.mandiripanganku.data.models.fishery.FisheryProject
import coil.load
import com.example.mandiripanganku.activities.projectupdate.EditFarmingActivity
import com.example.mandiripanganku.activities.projectupdate.EditFisheryActivity
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class FisheryAdapter(
    private val context: Context,
    private val fisheryList: List<FisheryProject>
) : RecyclerView.Adapter<FisheryAdapter.FisheryViewHolder>() {

    class FisheryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameText: TextView = itemView.findViewById(R.id.itemNameText)
        val itemQuantityText: TextView = itemView.findViewById(R.id.itemQuantityText)
        val itemStartDateText: TextView = itemView.findViewById(R.id.itemStartDateText)

        val fishName: TextView = itemView.findViewById(R.id.animalName) // Ganti ID sesuai layout item
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val fishPhoto: ImageView = itemView.findViewById(R.id.animalPhoto)
        val startDate: TextView = itemView.findViewById(R.id.startDate) // Ganti ID sesuai layout item
        val lastUpdated: TextView = itemView.findViewById(R.id.lastUpdated) // Ganti ID sesuai layout item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FisheryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_item_list_layout, parent, false) // Gunakan layout item yang sama
        return FisheryViewHolder(view)
    }

    override fun onBindViewHolder(holder: FisheryViewHolder, position: Int) {
        holder.itemNameText.text = context.getString(R.string.nama_ikan)
        holder.itemQuantityText.text = context.getString(R.string.jumlah_bibit)
        holder.itemStartDateText.text = context.getString(R.string.tanggal_mulai)

        val fishery = fisheryList[position]
        holder.fishName.text = fishery.fishName
        holder.quantity.text = fishery.quantity.toString() // Hanya menampilkan angka
        holder.startDate.text = fishery.startDate // Tanggal mulai
        holder.lastUpdated.text = formatTimestamp(fishery.updatedAt) // Mengonversi lastUpdated

        // Menggunakan Coil untuk memuat gambar dari URL
        holder.fishPhoto.load(fishery.fishPhoto) {
            placeholder(R.drawable.palceholder_load_image) // Gambar placeholder saat memuat
            when(fishery.fishName){
                "Lele" -> error(R.drawable.lele_full)
                "Bawal" -> error(R.drawable.bawal_full)
                "Gurameh" -> error(R.drawable.gurame_full)
                else -> error(R.drawable.error_broken_image)
            }
        }
        holder.itemView.setOnClickListener {
            // Start the Edit Activity
            val intent = Intent(context, EditFisheryActivity::class.java).apply {
                putExtra("fishery_id", fishery.fisheryId )
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return fisheryList.size
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