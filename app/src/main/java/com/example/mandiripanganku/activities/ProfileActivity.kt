package com.example.mandiripanganku.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.FamilySession

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val family = FamilySession.family
        val headOfFamily = findViewById<TextView>(R.id.head_of_family)
        val memberCount = findViewById<TextView>(R.id.member_count)
        val phoneNumber = findViewById<TextView>(R.id.phone_number)

        headOfFamily.text = family!!.headOfFamily
        memberCount.text = family.memberCount.toString()
        phoneNumber.text = family.phoneNumber


        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent (this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        val logoutButton = findViewById<TextView>(R.id.exit)
        logoutButton.setOnClickListener {
            // Menghapus data dari SharedPreferences
            val sharedPreferences = getSharedPreferences("FamilySession", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Menghapus data dari FamilySession
            FamilySession.family = null
            FamilySession.isLoggedIn = false

            // Arahkan kembali ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.ic_home).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ic_community).setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ic_report).setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.profil)

        val iconProfile = findViewById<ImageView>(R.id.ic_profile)
        iconProfile.setImageResource(R.drawable.ic_profile_clicked)

        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val intent = Intent (this@ProfileActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        )















    }




}
