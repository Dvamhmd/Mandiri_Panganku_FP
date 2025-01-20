package com.example.mandiripanganku.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.Family
import com.example.mandiripanganku.data.models.FamilySession
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("FamilySession", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )

        if (isLoggedIn) {
            // Muat data ke FamilySession
            FamilySession.family = Family(
                kkNumber = sharedPreferences.getString("KK_NUMBER", "") ?: "",
                headOfFamily = sharedPreferences.getString("HEAD_OF_FAMILY", "") ?: "",
                memberCount = sharedPreferences.getInt("MEMBER_COUNT", 0),
                phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "") ?: ""
            )
            FamilySession.isLoggedIn = true

            // Arahkan ke HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}