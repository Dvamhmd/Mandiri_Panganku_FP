package com.example.mandiripanganku.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.livestock.LivestockProject
import coil.load
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class LivestockAdapter(private val livestockList: List<LivestockProject>) : RecyclerView.Adapter<LivestockAdapter.LivestockViewHolder>() {

    class LivestockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animalName: TextView = itemView.findViewById(R.id.animalName)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val animalPhoto: ImageView = itemView.findViewById(R.id.animalPhoto)
        val startDate: TextView = itemView.findViewById(R.id.startDate) // Tambahkan untuk tanggal mulai
        val lastUpdated: TextView = itemView.findViewById(R.id.lastUpdated) // Tambahkan untuk tanggal terakhir diperbarui
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivestockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_livestock, parent, false)
        return LivestockViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivestockViewHolder, position: Int) {
        val livestock = livestockList[position]
        holder.animalName.text = livestock.animalName
        holder.quantity.text = livestock.quantity.toString() // Hanya menampilkan angka

        // Mengonversi Any? ke Timestamp dan kemudian ke String
        holder.startDate.text = livestock.startDate // Mengonversi startDate
        holder.lastUpdated.text = formatTimestamp(livestock.updatedAt) // Mengonversi lastUpdated

        // Menggunakan Coil untuk memuat gambar dari URL
        holder.animalPhoto.load(livestock.animalPhoto) {
            placeholder(R.drawable.palceholder_load_image) // Gambar placeholder saat memuat
            error(R.drawable.error_broken_image) // Gambar error jika gagal memuat
        }
    }

    override fun getItemCount(): Int {
        return livestockList.size
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