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

class ClueAgricultureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clue_agriculture)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clue_agriculture)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.panduan_perawatan)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, AgricultureActivity::class.java)
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

        val tomatView = findViewById<View>(R.id.tomat)
        val tomatClick = tomatView.findViewById<CardView>(R.id.card_click)
        val tomatImage = tomatView.findViewById<ImageView>(R.id.card_image)
        val tomatTitle = tomatView.findViewById<TextView>(R.id.card_title)
        val tomatDescription = tomatView.findViewById<ImageView>(R.id.card_description)
        tomatImage.setImageResource(R.drawable.tomat)
        tomatTitle.text = getString(R.string.tomat)
        tomatDescription.setImageResource(R.drawable.text_tomat)

        val kentangView = findViewById<View>(R.id.kentang)
        val kentangClick = kentangView.findViewById<CardView>(R.id.card_click)
        val kentangImage = kentangView.findViewById<ImageView>(R.id.card_image)
        val kentangTitle = kentangView.findViewById<TextView>(R.id.card_title)
        val kentangDescription = kentangView.findViewById<ImageView>(R.id.card_description)
        kentangImage.setImageResource(R.drawable.kentang)
        kentangTitle.text = getString(R.string.kentang)
        kentangDescription.setImageResource(R.drawable.text_kentang)

        val bayamView = findViewById<View>(R.id.bayam)

        val bayamImage = bayamView.findViewById<ImageView>(R.id.card_image)
        val bayamClick = bayamView.findViewById<CardView>(R.id.card_click)
        val bayamTitle = bayamView.findViewById<TextView>(R.id.card_title)
        val bayamDescription = bayamView.findViewById<ImageView>(R.id.card_description)
        bayamImage.setImageResource(R.drawable.bayam)
        bayamTitle.text = getString(R.string.bayam)
        bayamDescription.setImageResource(R.drawable.text_bayam)

        tomatClick.setOnClickListener {
            val intent = Intent(this, PanduanTomat::class.java)
            startActivity(intent)
        }

        kentangClick.setOnClickListener {
            val intent = Intent(this, PanduanKentang::class.java)
            startActivity(intent)
        }

       bayamClick.setOnClickListener {
            val intent = Intent(this, PanduanBayam::class.java)
            startActivity(intent)
        }



        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val intent = Intent (this@ClueAgricultureActivity, AgricultureActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        )



    }
}