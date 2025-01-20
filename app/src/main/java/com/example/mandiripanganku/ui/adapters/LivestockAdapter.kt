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
import com.example.mandiripanganku.data.models.livestock.LivestockProject
import coil.load
import com.example.mandiripanganku.activities.projectupdate.EditAgricultureActivity
import com.example.mandiripanganku.activities.projectupdate.EditFarmingActivity
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class LivestockAdapter(
    private val context: Context,
    private val livestockList: List<LivestockProject>
) : RecyclerView.Adapter<LivestockAdapter.LivestockViewHolder>() {

    class LivestockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameText: TextView = itemView.findViewById(R.id.itemNameText)
        val itemQuantityText: TextView = itemView.findViewById(R.id.itemQuantityText)
        val itemStartDateText: TextView = itemView.findViewById(R.id.itemStartDateText)

        val animalName: TextView = itemView.findViewById(R.id.animalName)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val animalPhoto: ImageView = itemView.findViewById(R.id.animalPhoto)
        val startDate: TextView = itemView.findViewById(R.id.startDate) // Tambahkan untuk tanggal mulai
        val lastUpdated: TextView = itemView.findViewById(R.id.lastUpdated) // Tambahkan untuk tanggal terakhir diperbarui
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivestockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_item_list_layout, parent, false)
        return LivestockViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivestockViewHolder, position: Int) {
        holder.itemNameText.text = context.getString(R.string.nama_ternak)
        holder.itemQuantityText.text = context.getString(R.string.jumlah_ternak)
        holder.itemStartDateText.text = context.getString(R.string.tanggal_mulai)

        val livestock = livestockList[position]
        holder.animalName.text = livestock.animalName
        holder.quantity.text = livestock.quantity.toString() // Hanya menampilkan angka

        // Mengonversi Any? ke Timestamp dan kemudian ke String
        holder.startDate.text = livestock.startDate // Mengonversi startDate
        holder.lastUpdated.text = formatTimestamp(livestock.updatedAt) // Mengonversi lastUpdated

        // Menggunakan Coil untuk memuat gambar dari URL
        holder.animalPhoto.load(livestock.animalPhoto) {
            placeholder(R.drawable.palceholder_load_image) // Gambar placeholder saat memuat
            when(livestock.animalName){
                "Ayam" -> error(R.drawable.ayam_full)
                "Kambing" -> error(R.drawable.kambing_full)
                "Bebek" -> error(R.drawable.bebek_full)
                else -> error(R.drawable.error_broken_image)
            }
        }

        holder.itemView.setOnClickListener {
            // Start the Edit Activity
            val intent = Intent(context, EditFarmingActivity::class.java).apply {
                putExtra("livestock_id", livestock.livestockId )
            }
            context.startActivity(intent)
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