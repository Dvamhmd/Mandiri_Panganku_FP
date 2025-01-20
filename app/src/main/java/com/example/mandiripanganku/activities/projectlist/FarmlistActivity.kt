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
import com.example.mandiripanganku.ui.adapters.LivestockAdapter // Updated to LivestockAdapter
import com.example.mandiripanganku.viewmodels.livestock.LivestockProjectViewModel // Updated to LivestockProjectViewModel
import com.example.mandiripanganku.viewmodels.livestock.LivestockProjectViewModelFactory // Updated to LivestockProjectViewModelFactory
import androidx.activity.viewModels// Change to your actual activity
import com.example.mandiripanganku.activities.CommunityActivity
import com.example.mandiripanganku.activities.FarmingActivity
import com.example.mandiripanganku.activities.HomeActivity
import com.example.mandiripanganku.activities.ProfileActivity
import com.example.mandiripanganku.activities.ReportActivity
import com.example.mandiripanganku.activities.projectform.MyFarmingActivity// Change to your actual activity
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.data.repositories.livestock.LivestockProjectRepository // Updated to LivestockProjectRepository

class FarmlistActivity : AppCompatActivity() {

    private lateinit var addProjectText: TextView
    private lateinit var yourProjectText: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var recyclerView: RecyclerView
    private lateinit var livestockAdapter: LivestockAdapter // Updated to LivestockAdapter
    private val livestockViewModel: LivestockProjectViewModel by viewModels {
        LivestockProjectViewModelFactory(LivestockProjectRepository()) // Updated to LivestockProjectRepository
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
        topBarTitle.text = getString(R.string.peternakanku) // Change title according to category

        progressBar = findViewById(R.id.progressBar) // Initialize ProgressBar

        // Initialize Text
        addProjectText.text = getString(R.string.tambah_peternakan)
        yourProjectText.text = getString(R.string.peternakan_anda)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchLivestockProjects()

        // Observe error messages
        livestockViewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        // Set up click listeners for navigation
        findViewById<ImageView>(R.id.back).setOnClickListener {
            val intent = Intent(this, FarmingActivity::class.java) // Change to your actual activity
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
            val intent = Intent(this, MyFarmingActivity::class.java) // Change to your actual activity
            startActivity(intent)
        }

        // Handle back press
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@FarmlistActivity, FarmingActivity::class.java) // Change to your actual activity
                    startActivity(intent)
                    finish()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        // Refresh the livestock projects when the activity resumes
        fetchLivestockProjects()
    }

    private fun fetchLivestockProjects() {
        val kkNumber = FamilySession.family!!.kkNumber.toString()
        progressBar.visibility = View.VISIBLE // Show loading indicator
        livestockViewModel.fetchLivestockProjects(kkNumber) // Fetch livestock projects

        // Observe changes on livestockProjects
        livestockViewModel.livestockProjects.observe(this) { livestockProjects ->
            progressBar.visibility = View.GONE // Hide loading indicator
            if (livestockProjects.isNotEmpty()) {
                livestockAdapter = LivestockAdapter(this, livestockProjects) // Updated to LivestockAdapter
                recyclerView.adapter = livestockAdapter
            } else {
                Toast.makeText(this, "No livestock projects found.", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe error messages
        livestockViewModel.errorMessage.observe(this) { errorMessage ->
            progressBar.visibility = View.GONE // Hide loading indicator
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}