package com.example.mandiripanganku

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.ic_profile).setOnClickListener {
            val intent = Intent (this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.profile_pic).setOnClickListener {
            val intent = Intent (this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.ic_report).setOnClickListener {
            val intent = Intent (this, ReportActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ic_community).setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


        findViewById<ImageView>(R.id.pertanian).setOnClickListener {
            val intent = Intent (this, AgricultureActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.perikanan).setOnClickListener {
            val intent = Intent (this, FisheryActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.peternakan).setOnClickListener {
            val intent = Intent (this, FarmingActivity::class.java)
            startActivity(intent)
        }

        val iconHome = findViewById<ImageView>(R.id.ic_home)
        iconHome.setImageResource(R.drawable.ic_home_clicked)

        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    finishAffinity()
                }
            }
        )




    }
}

