package com.example.mandiripanganku

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ReportActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.report)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.report)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.dashboard).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.profile).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.community).setOnClickListener {
            val intent = Intent (this, CommunityActivity::class.java)
            startActivity(intent)
        }



    }
}