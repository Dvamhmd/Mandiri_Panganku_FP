package com.example.mandiripanganku.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mandiripanganku.R

class ClueFarmingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clue_farming)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clue_farming)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.panduan_perawatan)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, FarmingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ic_profile).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.ic_home).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
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

        val ayamView = findViewById<View>(R.id.ayam)
        val ayamClick = ayamView.findViewById<CardView>(R.id.card_click)
        val ayamImage = ayamView.findViewById<ImageView>(R.id.card_image)
        val ayamTitle = ayamView.findViewById<TextView>(R.id.card_title)
        val ayamDescription = ayamView.findViewById<ImageView>(R.id.card_description)
        ayamImage.setImageResource(R.drawable.ayam)
        ayamTitle.text = getString(R.string.ayam)
        ayamDescription.setImageResource(R.drawable.text_ayam)

        val kambingView = findViewById<View>(R.id.kambing)
        val kambingClick = kambingView.findViewById<CardView>(R.id.card_click)
        val kambingImage = kambingView.findViewById<ImageView>(R.id.card_image)
        val kambingTitle = kambingView.findViewById<TextView>(R.id.card_title)
        val kambingDescription = kambingView.findViewById<ImageView>(R.id.card_description)
        kambingImage.setImageResource(R.drawable.kambing)
        kambingTitle.text = getString(R.string.kambing)
        kambingDescription.setImageResource(R.drawable.text_kambing)

        val bebekView = findViewById<View>(R.id.bebek)
        val bebekClick = bebekView.findViewById<CardView>(R.id.card_click)
        val bebekImage = bebekView.findViewById<ImageView>(R.id.card_image)
        val bebekTitle = bebekView.findViewById<TextView>(R.id.card_title)
        val bebekDescription = bebekView.findViewById<ImageView>(R.id.card_description)
        bebekImage.setImageResource(R.drawable.bebek)
        bebekTitle.text = getString(R.string.bebek)
        bebekDescription.setImageResource(R.drawable.text_bebek)

        ayamClick.setOnClickListener {
            val intent = Intent(this, PanduanAyam::class.java)
            startActivity(intent)
        }

        kambingClick.setOnClickListener {
            val intent = Intent(this, PanduanKambing::class.java)
            startActivity(intent)
        }

        bebekClick.setOnClickListener {
            val intent = Intent(this, PanduanBebek::class.java)
            startActivity(intent)
        }



        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val intent = Intent (this@ClueFarmingActivity, FarmingActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        )



    }
}