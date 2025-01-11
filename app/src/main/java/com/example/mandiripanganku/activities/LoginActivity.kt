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

class LoginActivity : AppCompatActivity() {
    private lateinit var familyViewModel: FamilyViewModel


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val familyRepository = FamilyRepository()

        // Inisialisasi ViewModel
        familyViewModel = ViewModelProvider(this, FamilyViewModelFactory(familyRepository))[FamilyViewModel::class.java]

        // Ambil referensi ke EditText dan Button
        val kkNumberEditText = findViewById<EditText>(R.id.number)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val signupButton = findViewById<Button>(R.id.signup)
        val loginButton = findViewById<Button>(R.id.login)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Panggil metode untuk mendapatkan keluarga

        loginButton.setOnClickListener {
            // Ambil nilai dari EditText
            val kkNumberInput = kkNumberEditText.text.toString().trim()
            val passwordInput = passwordEditText.text.toString().trim()

            // Validasi: Periksa apakah ada field yang kosong
            if (kkNumberInput.isEmpty() || passwordInput.isEmpty()) {
                // Tampilkan pesan kesalahan jika ada field yang kosong
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Kembali tanpa melanjutkan
            }

            loginButton.isEnabled = false
            progressBar.visibility = View.VISIBLE

            // Buat objek Family
            val dataFamily = Family(kkNumber = kkNumberInput)

            // Panggil metode
            familyViewModel.getFamily(dataFamily)
            val family = FamilySession.family

            if ( family != null) {
                // Periksa apakah password yang dimasukkan cocok
                if (passwordInput == family.password) {
                    // Jika berhasil login
                    Toast.makeText(this, "Selamat datang kembali ${family.headOfFamily}", Toast.LENGTH_SHORT).show()

                    val sharedPreferences = getSharedPreferences("FamilySession", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("KK_NUMBER", family.kkNumber)
                    editor.putString("HEAD_OF_FAMILY", family.headOfFamily)
                    editor.putInt("MEMBER_COUNT", family.memberCount!!)
                    editor.putString("PHONE_NUMBER", family.phoneNumber)
                    editor.putBoolean("IS_LOGGED_IN", true) // Menyimpan status login
                    editor.apply()

                    FamilySession.family = family
                    FamilySession.isLoggedIn = true

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // Jika login gagal
                    Toast.makeText(this, "Password salah", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Nomor KK tidak terdaftar", Toast.LENGTH_SHORT).show()
            }


            // Aktifkan kembali tombol dan sembunyikan ProgressBar
            loginButton.isEnabled = true
            progressBar.visibility = View.GONE
        }

        signupButton.setOnClickListener {
            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}