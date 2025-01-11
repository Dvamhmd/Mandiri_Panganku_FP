package com.example.mandiripanganku.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.agriculture.AgricultureProject
import com.example.mandiripanganku.data.repositories.agriculture.AgricultureProjectRepository
import com.example.mandiripanganku.viewmodels.agriculture.AgricultureProjectViewModel
import androidx.activity.viewModels
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.viewmodels.agriculture.AgricultureProjectViewModelFactory
import java.util.*
import java.text.SimpleDateFormat

class MyAgricultureActivity : AppCompatActivity() {

    private lateinit var plantName: Spinner
    private lateinit var saveButton: Button
    private lateinit var quantityInput: EditText
    private lateinit var plantingDateInput: TextView // Ganti menjadi TextView
    private val viewModel: AgricultureProjectViewModel by viewModels { AgricultureProjectViewModelFactory(AgricultureProjectRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_agriculture)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_agriculture)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.my_agriculture)

        // Inisialisasi UI
        plantName = findViewById(R.id.spinnerOptions)
        quantityInput = findViewById(R.id.quantityInput)
        plantingDateInput = findViewById(R.id.plantingDate)

        setupNavigation()
        setupSpinner()
        setupSaveButton()
        setupDatePicker() // Setup DatePicker
        handleBackPress()
    }

    private fun setupNavigation() {
        findViewById<ImageView>(R.id.back).setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_profile).setOnClickListener {
            navigateTo(ProfileActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_home).setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_report).setOnClickListener {
            navigateTo(ReportActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_community).setOnClickListener {
            navigateTo(CommunityActivity::class.java)
        }
    }

    private fun setupSpinner() {
        plantName = findViewById(R.id.spinnerOptions)
        ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_pertanian,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            plantName.adapter = adapter
        }
    }

    private fun setupSaveButton() {
        saveButton = findViewById(R.id.save)
        saveButton.setOnClickListener {
            // Ambil data dari input pengguna
            val quantity = quantityInput.text.toString().toIntOrNull() ?: 0 // Ambil jumlah tanaman
            val plantingDate = plantingDateInput.text.toString().trim() // Ambil tanggal penanaman

            // Validasi input
            if (quantity <= 0 || plantingDate.isEmpty()) {
                Toast.makeText(this, "Silakan lengkapi semua input!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil
            val kkNumber = FamilySession.family!!.kkNumber.toString()
            // Menghasilkan agricultureId
            val agricultureId = generateAgricultureId(kkNumber) // Pastikan Anda memiliki kkNumber dari input pengguna

            // Membuat objek AgricultureProject
            val agricultureProject = AgricultureProject(
                agricultureId = agricultureId,
                kkNumber = kkNumber, // Ganti dengan ID keluarga yang sesuai
                plantName = plantName.selectedItem.toString(), // Ambil pilihan dari spinner
                plantPhoto = "", // Ganti dengan URL foto yang sesuai
                quantity = quantity,
                plantingDate = plantingDate
            )

            // Simpan proyek pertanian
            viewModel.saveAgricultureProject(agricultureProject)
            Toast.makeText(this, "Proyek pertanian berhasil disimpan!", Toast.LENGTH_SHORT).show()
            navigateTo(AgrilistActivity::class.java) // Navigasi ke AgrilistActivity
        }
    }

    private fun setupDatePicker() {
        plantingDateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Format tanggal yang dipilih
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                plantingDateInput.text = selectedDate // Tampilkan tanggal yang dipilih di TextView
            }, year, month, day)

            datePickerDialog.show() // Tampilkan dialog pemilih tanggal
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish() // Menutup activity saat ini agar tidak kembali ke sini
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateTo(HomeActivity::class.java)
                }
            }
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun generateAgricultureId(kkNumber: String): String {
        // Mendapatkan waktu saat ini
        val currentTime = Calendar.getInstance()

        // Format untuk tanggal, bulan, tahun, menit, dan detik
        val day = SimpleDateFormat("dd").format(currentTime.time) // Tanggal
        val month = SimpleDateFormat("MM").format(currentTime.time) // Bulan
        val year = SimpleDateFormat("yyyy").format(currentTime.time) // Tahun
        val minutes = SimpleDateFormat("mm").format(currentTime.time) // Menit
        val seconds = SimpleDateFormat("ss").format(currentTime.time) // Detik

        // Membuat agriculture ID
        return "$kkNumber$day$month$year$minutes$seconds"
    }
}