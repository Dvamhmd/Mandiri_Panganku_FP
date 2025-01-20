package com.example.mandiripanganku.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.Family
import com.example.mandiripanganku.data.models.FamilySession

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity" // Tag untuk log

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val family = FamilySession.family
        val headOfFamily = findViewById<TextView>(R.id.head_of_family)

        // Periksa apakah family tidak null sebelum mengakses headOfFamily
        if (family != null) {
            headOfFamily.text = family.headOfFamily
            logFamilyData(family) // Log data keluarga
        } else {
            headOfFamily.text = getString(R.string.keluarga_tidak_ditemukan) // Tampilkan pesan default
            Log.w(TAG, "Family data is null") // Log peringatan jika data keluarga null
        }

        findViewById<ImageView>(R.id.ic_profile).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.profile_pic).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ic_report).setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ic_community).setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.pertanian).setOnClickListener {
            val intent = Intent(this, AgricultureActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.perikanan).setOnClickListener {
            val intent = Intent(this, FisheryActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.peternakan).setOnClickListener {
            val intent = Intent(this, FarmingActivity::class.java)
            startActivity(intent)
        }

        val iconHome = findViewById<ImageView>(R.id.ic_home)
        iconHome.setImageResource(R.drawable.ic_home_clicked)

        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishAffinity()
                }
            }
        )
    }

    // Fungsi untuk log data keluarga
    private fun logFamilyData(family: Family) {
        Log.i(TAG, "KK Number: ${family.kkNumber}")
        Log.i(TAG, "Head of Family: ${family.headOfFamily}")
        Log.i(TAG, "Member Count: ${family.memberCount}")
        Log.i(TAG, "Phone Number: ${family.phoneNumber}")
    }
}