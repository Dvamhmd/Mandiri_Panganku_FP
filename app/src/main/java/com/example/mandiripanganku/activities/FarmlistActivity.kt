package com.example.mandiripanganku.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.livestock.LivestockProject
import com.example.mandiripanganku.ui.adapters.LivestockAdapter
import com.example.mandiripanganku.viewmodels.livestock.LivestockProjectViewModel
import com.example.mandiripanganku.viewmodels.livestock.LivestockProjectViewModelFactory
import androidx.activity.viewModels
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.data.repositories.livestock.LivestockProjectRepository

class FarmlistActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var livestockAdapter: LivestockAdapter
    private val livestockViewModel: LivestockProjectViewModel by viewModels {
        LivestockProjectViewModelFactory(LivestockProjectRepository()) // Pastikan repository diinisialisasi
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_farmlist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.farmlist)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.peternakanku)

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val kkNumber = FamilySession.family!!.kkNumber.toString()
        // Ambil data proyek peternakan
        livestockViewModel.fetchLivestockProjects(kkNumber)

        // Amati perubahan pada livestockProjects
        livestockViewModel.livestockProjects.observe(this) { livestockProjects ->
            // Jika ada proyek peternakan, perbarui adapter
            if (livestockProjects.isNotEmpty()) {
                livestockAdapter = LivestockAdapter(livestockProjects)
                recyclerView.adapter = livestockAdapter
            } else {
                // Tampilkan pesan jika tidak ada proyek
                Toast.makeText(this, "No livestock projects found.", Toast.LENGTH_SHORT).show()
            }
        }

        // Amati pesan kesalahan
        livestockViewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        // Set up click listeners for navigation
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

        findViewById<View>(R.id.add).setOnClickListener {
            val intent = Intent(this, MyFarmingActivity::class.java)
            startActivity(intent)
        }

        // Handle back press
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@FarmlistActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        )
    }
}