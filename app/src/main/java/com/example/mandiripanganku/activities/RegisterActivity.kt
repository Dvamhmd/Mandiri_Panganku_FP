package com.example.mandiripanganku.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.Family
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.data.repository.FamilyRepository
import com.example.mandiripanganku.viewmodel.FamilyViewModel
import com.example.mandiripanganku.viewmodel.FamilyViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var familyViewModel: FamilyViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi FamilyRepository
        val familyRepository = FamilyRepository()

        // Inisialisasi ViewModel
        familyViewModel = ViewModelProvider(this, FamilyViewModelFactory(familyRepository))[FamilyViewModel::class.java]

        // Ambil referensi ke EditText dan Button
        val kkNumberEditText = findViewById<EditText>(R.id.number)
        val headOfFamilyEditText = findViewById<EditText>(R.id.family)
        val memberCountEditText = findViewById<EditText>(R.id.member) // Misalkan ini untuk member count
        val phoneNumberEditText = findViewById<EditText>(R.id.phone)
        val signupButton = findViewById<Button>(R.id.signup)
        val loginButton = findViewById<Button>(R.id.login)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Set OnClickListener untuk tombol signup
        // Di dalam RegisterActivity
        signupButton.setOnClickListener {
            // Ambil nilai dari EditText
            val kkNumber = kkNumberEditText.text.toString().trim()
            val headOfFamily = headOfFamilyEditText.text.toString().trim()
            val memberCountString = memberCountEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            // Validasi: Periksa apakah ada field yang kosong
            if (kkNumber.isEmpty() || headOfFamily.isEmpty() || memberCountString.isEmpty() || phoneNumber.isEmpty()) {
                // Tampilkan pesan kesalahan jika ada field yang kosong
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Kembali tanpa melanjutkan
            }

            // Konversi memberCount ke Int
            val memberCount = memberCountString.toIntOrNull() ?: 0

            // Nonaktifkan tombol dan tampilkan ProgressBar
            signupButton.isEnabled = false
            progressBar.visibility = View.VISIBLE

            // Buat objek Family
            val family = Family(kkNumber = kkNumber)

            // Panggil metode untuk menambahkan keluarga
            familyViewModel.getFamily(family)

            if (FamilySession.family != null) {
                Toast.makeText(this, "Gagal Menambahkan, Nomor KK $kkNumber Telah Terdaftar", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PasswordActivity::class.java).apply {
                    putExtra("KK_NUMBER", kkNumber)
                    putExtra("HEAD_OF_FAMILY", headOfFamily)
                    putExtra("MEMBER_COUNT", memberCount)
                    putExtra("PHONE_NUMBER", phoneNumber)
                }
                startActivity(intent)
            }

            // Cek status respons


            signupButton.isEnabled = true // Aktifkan kembali tombol setelah mendapatkan respons
            progressBar.visibility = View.GONE

        }

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}