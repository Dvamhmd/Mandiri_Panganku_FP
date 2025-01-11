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
import com.example.mandiripanganku.data.repositories.FamilyRepository
import com.example.mandiripanganku.viewmodels.FamilyViewModel
import com.example.mandiripanganku.viewmodels.FamilyViewModelFactory

class PasswordActivity : AppCompatActivity() {

    private lateinit var kkNumber: String
    private lateinit var headOfFamily: String
    private var memberCount: Int = 0
    private lateinit var phoneNumber: String

    private lateinit var familyViewModel: FamilyViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.password)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Menerima KK number dari Intent
        kkNumber = intent.getStringExtra("KK_NUMBER") ?: ""
        headOfFamily = intent.getStringExtra("HEAD_OF_FAMILY") ?: ""
        memberCount = intent.getIntExtra("MEMBER_COUNT", 0)
        phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""

        val familyRepository = FamilyRepository()

        // Inisialisasi ViewModel
        familyViewModel = ViewModelProvider(this, FamilyViewModelFactory(familyRepository))[FamilyViewModel::class.java]


        val passwordEditText = findViewById<EditText>(R.id.password_input)
        val confirmPasswordEditText = findViewById<EditText>(R.id.password_confirm)
        val signupButton = findViewById<Button>(R.id.signup)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        signupButton.setOnClickListener {
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password == confirmPassword) {
                // Buat objek Family
                progressBar.visibility = View.VISIBLE
                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    // Tampilkan pesan kesalahan jika ada field yang kosong
                    Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    return@setOnClickListener // Kembali tanpa melanjutkan
                }
                val family = Family(kkNumber = kkNumber, headOfFamily = headOfFamily, memberCount = memberCount, phoneNumber = phoneNumber, password = password)


                familyViewModel.addFamily(family)

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

                // Observasi status respons
                // Tampilkan status kepada pengguna, misalnya dengan Toast
                Toast.makeText(this, "Registrasi Berhasil, Selamat Datang ${family.headOfFamily}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                progressBar.visibility = View.GONE
                startActivity(intent)

            } else {
                // Tampilkan pesan kesalahan jika password tidak cocok
                Toast.makeText(this, "Password tidak cocok. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.login).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}