package com.example.mandiripanganku

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ClueFisheryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_clue_fishery)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clue_fishery)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.panduan_perawatan)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
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

        val bawalView = findViewById<View>(R.id.bawal)
        val bawalImage = bawalView.findViewById<ImageView>(R.id.card_image)
        val bawalTitle = bawalView.findViewById<TextView>(R.id.card_title)
        val bawalDescription = bawalView.findViewById<ImageView>(R.id.card_description)
        bawalImage.setImageResource(R.drawable.bawal)
        bawalTitle.text = getString(R.string.bawal)
        bawalDescription.setImageResource(R.drawable.text_bawal)



        val guramehView = findViewById<View>(R.id.gurame)
        val guramehImage = guramehView.findViewById<ImageView>(R.id.card_image)
        val guramehTitle = guramehView.findViewById<TextView>(R.id.card_title)
        val guramehDescription = guramehView.findViewById<ImageView>(R.id.card_description)
        guramehImage.setImageResource(R.drawable.gurame)
        guramehTitle.text = getString(R.string.gurameh)
        guramehDescription.setImageResource(R.drawable.text_gurameh)



        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    val intent = Intent (this@ClueFisheryActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        )



    }
}