package com.example.mandiripanganku

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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

        findViewById<ImageView>(R.id.profile).setOnClickListener {
            val intent = Intent (this, ProfileActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.profile_pic).setOnClickListener {
            val intent = Intent (this, ProfileActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.report).setOnClickListener {
            val intent = Intent (this, ReportActivity::class.java)
            startActivity(intent)
        }


    }
}

