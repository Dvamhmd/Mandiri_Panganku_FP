package com.example.mandiripanganku.activities.projectform

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
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
import com.example.mandiripanganku.data.models.livestock.LivestockProject
import com.example.mandiripanganku.data.repositories.livestock.LivestockProjectRepository
import com.example.mandiripanganku.viewmodels.livestock.LivestockProjectViewModel
import androidx.activity.viewModels
import com.example.mandiripanganku.activities.projectlist.AgrilistActivity
import com.example.mandiripanganku.activities.CommunityActivity
import com.example.mandiripanganku.activities.HomeActivity
import com.example.mandiripanganku.activities.ProfileActivity
import com.example.mandiripanganku.activities.ReportActivity
import com.example.mandiripanganku.activities.projectlist.FarmlistActivity
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.viewmodels.livestock.LivestockProjectViewModelFactory
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import java.text.SimpleDateFormat

@Suppress("DEPRECATION")
class MyFarmingActivity : AppCompatActivity() {

    private lateinit var plantText: TextView
    private lateinit var quantityText: TextView
    private lateinit var plantingDateText: TextView

    private lateinit var animalName: Spinner
    private lateinit var saveButton: Button
    private lateinit var quantityInput: EditText
    private lateinit var startDateInput: TextView
    private lateinit var imageView: ImageView
    private lateinit var selectedPhotoUri: Uri // Untuk menyimpan URI foto yang dipilih
    private lateinit var cancelPhotoButton: Button // Tambahkan ini di deklarasi variabel

    private val viewModel: LivestockProjectViewModel by viewModels { LivestockProjectViewModelFactory(LivestockProjectRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.project_form_layout)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_agriculture)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.livestock_project) // Ganti judul sesuai kategori


        //inisialisasi text
        plantText = findViewById(R.id.choose)
        quantityText = findViewById(R.id.quantiyText)
        plantingDateText = findViewById(R.id.startDate)

        // Inisialisasi UI
        animalName = findViewById(R.id.spinnerOptions)
        quantityInput = findViewById(R.id.quantityInput)
        startDateInput = findViewById(R.id.plantingDate) // Ganti nama variabel jika perlu
        imageView = findViewById(R.id.imageView)

        setupText()
        setupNavigation()
        setupSpinner()
        setupSaveButton()
        setupDatePicker() // Setup DatePicker
        setupSelectPhotoButton() // Setup button untuk memilih foto
        cancelPhotoButton = findViewById(R.id.button_cancel_photo)
        cancelPhotoButton.setOnClickListener {
            cancelPhotoSelection() // Panggil fungsi untuk membatalkan pemilihan foto
        }
        handleBackPress()
    }

    private fun setupText() {
        plantText.text = getString(R.string.pilih_ternak_anda)
        quantityText.text = getString(R.string.jumlah_ternak)
        plantingDateText.text = getString(R.string.tanggal_mulai)

        quantityInput.hint = getString(R.string.masukkan_jumlah_ternak)
        startDateInput.hint = getString(R.string.masukkan_tanggal_mulai)
    }

    private fun setupNavigation() {
        findViewById<ImageView>(R.id.back).setOnClickListener {
            navigateTo(FarmlistActivity::class.java)
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
        animalName = findViewById(R.id.spinnerOptions)
        ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_peternakan, // Pastikan Anda memiliki array ini di resources
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            animalName.adapter = adapter
        }
    }

    private fun setupSelectPhotoButton() {
        findViewById<Button>(R.id.button_select_photo).setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedPhotoUri = data.data!! // Ambil URI foto yang dipilih
            imageView.setImageURI(selectedPhotoUri) // Tampilkan gambar di ImageView
        }
    }

    private fun setupSaveButton() {
        saveButton = findViewById(R.id.save)
        saveButton.setOnClickListener {
            // Ambil data dari input pengguna
            uploadImageToFirebase()
        }
    }

    private fun uploadImageToFirebase() {
        val storageReference = FirebaseStorage.getInstance().getReference("uploads")
        val fileReference = storageReference.child(System.currentTimeMillis().toString() + ".jpg")

        // Cek apakah selectedPhotoUri diinisialisasi
        val photoUriString = if (::selectedPhotoUri.isInitialized) {
            selectedPhotoUri
        } else {
            Uri.EMPTY // Atau Anda bisa menggunakan string kosong jika Anda ingin
        }

        if (photoUriString != Uri.EMPTY) {
            fileReference.putFile(photoUriString)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->

                        val quantity = quantityInput.text.toString().toIntOrNull() ?: 0 // Ambil jumlah hewan
                        val startDate = startDateInput.text.toString().trim() // Ambil tanggal mulai

                        // Validasi input
                        if (quantity <= 0 || startDate.isEmpty()) {
                            Toast.makeText(
                                this,
                                "Silakan lengkapi semua input!",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@addOnSuccessListener
                        }

                        val kkNumber = FamilySession.family!!.kkNumber.toString()
                        // Menghasilkan livestockId
                        val livestockId = generateLivestockId(kkNumber)

                        val livestockProject = LivestockProject(
                            livestockId = livestockId,
                            kkNumber = kkNumber,
                            animalName = animalName.selectedItem.toString(),
                            animalPhoto = uri.toString(), // Sementara, kita akan mengupdate ini setelah upload
                            quantity = quantity,
                            startDate = startDate
                        )
                        // Simpan proyek peternakan
                        viewModel.saveLivestockProject(livestockProject)
                        Toast.makeText(
                            this,
                            "Proyek peternakan berhasil disimpan!",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateTo(AgrilistActivity::class.java) // Navigasi ke AgrilistActivity
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Jika foto tidak dipilih, set animalPhoto ke string kosong
            val quantity = quantityInput.text.toString().toIntOrNull() ?: 0 // Ambil jumlah hewan
            val startDate = startDateInput.text.toString().trim() // Ambil tanggal mulai

            // Validasi input
            if (quantity <= 0 || startDate.isEmpty()) {
                Toast.makeText(
                    this,
                    "Silakan lengkapi semua input!",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            val kkNumber = FamilySession.family!!.kkNumber.toString()
            // Menghasilkan livestockId
            val livestockId = generateLivestockId(kkNumber)

            val livestockProject = LivestockProject(
                livestockId = livestockId,
                kkNumber = kkNumber,
                animalName = animalName.selectedItem.toString(),
                animalPhoto = "", // Set ke string kosong jika foto tidak dipilih
                quantity = quantity,
                startDate = startDate
            )
            // Simpan proyek peternakan
            viewModel.saveLivestockProject(livestockProject)
            Toast.makeText(
                this,
                "Proyek peternakan berhasil disimpan!",
                Toast.LENGTH_SHORT
            ).show()
            navigateTo(FarmlistActivity::class.java) // Navigasi ke AgrilistActivity
        }
    }

    private fun cancelPhotoSelection() {
        selectedPhotoUri = Uri.EMPTY // Atur gambar di ImageView ke placeholder
        imageView.setImageDrawable(null)
        Toast.makeText(
            this,
            "Menghapus Foto",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupDatePicker() {
        startDateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Format tanggal yang dipilih
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                startDateInput.text = selectedDate // Tampilkan tanggal yang dipilih di TextView
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
                    navigateTo(FarmlistActivity::class.java)
                }
            }
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun generateLivestockId(kkNumber: String): String {
        // Mendapatkan waktu saat ini
        val currentTime = Calendar.getInstance()

        // Format untuk tanggal, bulan, tahun, menit, dan detik
        val day = SimpleDateFormat("dd").format(currentTime.time) // Tanggal
        val month = SimpleDateFormat("MM").format(currentTime.time) // Bulan
        val year = SimpleDateFormat("yyyy").format(currentTime.time) // Tahun
        val minutes = SimpleDateFormat("mm").format(currentTime.time) // Menit
        val seconds = SimpleDateFormat("ss").format(currentTime.time) // Detik

        // Membuat livestock ID
        return "$kkNumber$day$month$year$minutes$seconds"
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1 // Kode permintaan untuk memilih gambar
    }
}