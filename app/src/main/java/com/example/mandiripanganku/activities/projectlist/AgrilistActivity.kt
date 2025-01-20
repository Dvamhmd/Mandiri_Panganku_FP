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
import com.example.mandiripanganku.ui.adapters.AgricultureAdapter
import com.example.mandiripanganku.viewmodels.agriculture.AgricultureProjectViewModel
import com.example.mandiripanganku.viewmodels.agriculture.AgricultureProjectViewModelFactory
import androidx.activity.viewModels
import com.example.mandiripanganku.activities.AgricultureActivity
import com.example.mandiripanganku.activities.CommunityActivity
import com.example.mandiripanganku.activities.HomeActivity
import com.example.mandiripanganku.activities.ProfileActivity
import com.example.mandiripanganku.activities.ReportActivity
import com.example.mandiripanganku.activities.projectform.MyAgricultureActivity
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.data.repositories.agriculture.AgricultureProjectRepository

class AgrilistActivity : AppCompatActivity() {

    private lateinit var addProjectText: TextView
    private lateinit var yourProjectText: TextView
    private lateinit var progressbar: ProgressBar

    private lateinit var recyclerView: RecyclerView
    private lateinit var agricultureAdapter: AgricultureAdapter
    private val agricultureViewModel: AgricultureProjectViewModel by viewModels {
        AgricultureProjectViewModelFactory(AgricultureProjectRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.project_list_layout) // Menggunakan layout yang sama

        addProjectText = findViewById(R.id.addProjectText)
        yourProjectText = findViewById(R.id.yourProjectText)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.project_list_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.my_agriculture) // Ganti judul sesuai kategori

        progressbar = findViewById(R.id.progressBar) // Initialize ProgressBar


        // Inisialisasi Text
        addProjectText.text = getString(R.string.tambah_tanaman)
        yourProjectText.text = getString(R.string.tanaman_anda)

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchAgricultureProjects()

        // Amati pesan kesalahan
        agricultureViewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        // Set up click listeners for navigation
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

        findViewById<View>(R.id.add).setOnClickListener {
            val intent = Intent(this, MyAgricultureActivity::class.java)
            startActivity(intent)
        }

        // Handle back press
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@AgrilistActivity, AgricultureActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        // Refresh the agriculture projects when the activity resumes
        fetchAgricultureProjects()
    }

    private fun fetchAgricultureProjects() {
        val kkNumber = FamilySession.family!!.kkNumber.toString()
        progressbar.visibility = View.VISIBLE
        agricultureViewModel.fetchAgricultureProjects(kkNumber)

        // Observe changes on agricultureProjects
        agricultureViewModel.agricultureProjects.observe(this) { agricultureProjects ->
            progressbar.visibility = View.GONE
            if (agricultureProjects.isNotEmpty()) {
                agricultureAdapter = AgricultureAdapter(this, agricultureProjects)
                recyclerView.adapter = agricultureAdapter
            } else {
                Toast.makeText(this, "No agriculture projects found.", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe error messages
        agricultureViewModel.errorMessage.observe(this) { errorMessage ->
            progressbar.visibility = View.GONE
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}