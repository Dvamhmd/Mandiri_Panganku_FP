package com.example.mandiripanganku.activities.projectlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.example.mandiripanganku.ui.adapters.FisheryAdapter // Updated to FisheryAdapter
import com.example.mandiripanganku.viewmodels.fishery.FisheryProjectViewModel // Updated to FisheryProjectViewModel
import com.example.mandiripanganku.viewmodels.fishery.FisheryProjectViewModelFactory // Updated to FisheryProjectViewModelFactory
import androidx.activity.viewModels
import com.example.mandiripanganku.activities.CommunityActivity
import com.example.mandiripanganku.activities.FisheryActivity
import com.example.mandiripanganku.activities.HomeActivity
import com.example.mandiripanganku.activities.ProfileActivity
import com.example.mandiripanganku.activities.ReportActivity
import com.example.mandiripanganku.activities.projectform.MyFisheryActivity // Change to your actual activity
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.data.repositories.fishery.FisheryProjectRepository // Updated to FisheryProjectRepository

class FishlistActivity : AppCompatActivity() {

    private lateinit var addProjectText: TextView
    private lateinit var yourProjectText: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var recyclerView: RecyclerView
    private lateinit var fisheryAdapter: FisheryAdapter // Updated to FisheryAdapter
    private val fisheryViewModel: FisheryProjectViewModel by viewModels {
        FisheryProjectViewModelFactory(FisheryProjectRepository()) // Updated to FisheryProjectRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.project_list_layout) // Use the same layout

        addProjectText = findViewById(R.id.addProjectText)
        yourProjectText = findViewById(R.id.yourProjectText)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.project_list_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.perikananku) // Change title according to category

        progressBar = findViewById(R.id.progressBar) // Initialize ProgressBar

        // Initialize Text
        addProjectText.text = getString(R.string.tambah_perikanan)
        yourProjectText.text = getString(R.string.perikanan_anda)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchFisheryProjects()

        // Observe error messages
        fisheryViewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        // Set up click listeners for navigation
        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, FisheryActivity::class.java) // Change to your actual activity
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
            val intent = Intent(this, MyFisheryActivity::class.java) // Change to your actual activity
            startActivity(intent)
        }

        // Handle back press
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@FishlistActivity, HomeActivity::class.java) // Change to your actual activity
                    startActivity(intent)
                    finish()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        // Refresh the fishery projects when the activity resumes
        fetchFisheryProjects()
    }

    private fun fetchFisheryProjects() {
        val kkNumber = FamilySession.family!!.kkNumber.toString()
        progressBar.visibility = View.VISIBLE // Show loading indicator
        fisheryViewModel.fetchFisheryProjects(kkNumber) // Fetch fishery projects

        // Observe changes on fisheryProjects
        fisheryViewModel.fisheryProjects.observe(this) { fisheryProjects ->
            progressBar.visibility = View.GONE // Hide loading indicator
            if (fisheryProjects.isNotEmpty()) {
                fisheryAdapter = FisheryAdapter(this, fisheryProjects) // Updated to FisheryAdapter
                recyclerView.adapter = fisheryAdapter
            } else {
                Toast.makeText(this, "No fishery projects found.", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe error messages
        fisheryViewModel.errorMessage.observe(this) { errorMessage ->
            progressBar.visibility = View.GONE // Hide loading indicator
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}