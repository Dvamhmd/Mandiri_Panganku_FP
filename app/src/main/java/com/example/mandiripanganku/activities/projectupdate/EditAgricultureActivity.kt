package com.example.mandiripanganku.activities.projectupdate

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mandiripanganku.R
import com.example.mandiripanganku.activities.CommunityActivity
import com.example.mandiripanganku.activities.HomeActivity
import com.example.mandiripanganku.activities.ProfileActivity
import com.example.mandiripanganku.activities.ReportActivity
import com.example.mandiripanganku.activities.projectlist.AgrilistActivity
import com.example.mandiripanganku.data.models.agriculture.AgricultureProject
import com.example.mandiripanganku.data.repositories.agriculture.AgricultureProjectRepository

@Suppress("DEPRECATION")
class EditAgricultureActivity : AppCompatActivity() {

    private lateinit var projectNameText: TextView
    private lateinit var quantityText: TextView
    private lateinit var startDateText: TextView

    private lateinit var projectNameTextView: TextView
    private lateinit var quantityInput: EditText
    private lateinit var plantingDateTextView: TextView
    private lateinit var imageView: ImageView

    private lateinit var selectPhotoButton: Button
    private lateinit var deletePhotoButton: Button
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    private var currentPhotoUri: Uri? = null // To hold the selected photo URI
    private lateinit var agricultureProject: AgricultureProject // To hold the project data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_update_layout) // Replace with your actual layout file

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.project_update_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        projectNameText = findViewById(R.id.projectNameText)
        quantityText = findViewById(R.id.quantityText)
        startDateText = findViewById(R.id.startDateText)

        projectNameTextView = findViewById(R.id.projectName)
        quantityInput = findViewById(R.id.quantityInput)
        plantingDateTextView = findViewById(R.id.plantingDate)
        imageView = findViewById(R.id.imageView)

        selectPhotoButton = findViewById(R.id.button_select_photo)
        deletePhotoButton = findViewById(R.id.button_cancel_photo)
        saveButton = findViewById(R.id.save)
        deleteButton = findViewById(R.id.delete)

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.my_agriculture)


        setupNavigation()

        // Load existing project data
        loadProjectData()

        // Set up button listeners
        selectPhotoButton.setOnClickListener {
            openImagePicker()
        }

        deletePhotoButton.setOnClickListener {
            currentPhotoUri = null // Clear the photo URI
            imageView.setImageDrawable(null) // Reset to placeholder
        }

        saveButton.setOnClickListener {
            saveProject()
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }


    private fun setupNavigation() {
        findViewById<ImageView>(R.id.back).setOnClickListener {
            navigateTo(AgrilistActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_profile).setOnClickListener {
            navigateTo(ProfileActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_home).setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_report).setOnClickListener {
            navigateTo(ReportActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_community).setOnClickListener {
            navigateTo(CommunityActivity::class.java)
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish() // Menutup activity saat ini agar tidak kembali ke sini
    }

    private fun loadProjectData() {
        // Get the project ID from the intent
        val projectId = intent.getStringExtra("agriculture_id") ?: return

        // Fetch the project data from the repository
        AgricultureProjectRepository().getAgricultureProjectById(projectId,
            { project ->
                agricultureProject = project // Store the project data
                // Populate the views with the project data
                projectNameText.text = getString(R.string.nama_tanaman)
                quantityText.text = getString(R.string.jumlah_tanaman)
                startDateText.text = getString(R.string.tanggal_tanam)

                projectNameTextView.text = agricultureProject.plantName // Assuming plantName is the project name
                quantityInput.setText(agricultureProject.quantity.toString())
                quantityInput.hint = getString(R.string.masukkan_jumlah_tanaman)
                plantingDateTextView.text = agricultureProject.plantingDate // Display planting date

                // Load the existing photo
                currentPhotoUri = Uri.parse(agricultureProject.plantPhoto)
                imageView.setImageURI(currentPhotoUri)
            },
            { e ->
                Toast.makeText(this, "Gagal loading proyek: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun openImagePicker() {
        // Open the image picker to select a photo
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            currentPhotoUri = data?.data // Get the selected image URI
            imageView.setImageURI(currentPhotoUri) // Display the selected image
        }
    }

    private fun saveProject() {
        // Validate input
        val quantity = quantityInput.text.toString().toIntOrNull()
        if (quantity == null) {
            Toast.makeText(this, "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // Save the updated project fields
        AgricultureProjectRepository().updateAgricultureProject(
            agricultureProject.agricultureId,
            quantity,
            currentPhotoUri?.toString(), // Pass the new photo URI
            {
                Toast.makeText(this, "Berhasil memperbarui proyek", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            },
            { e ->
                Toast.makeText(this, "Gagal memperbarui proyek: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Apakah Anda yakin ingin menghapus proyek ini?")
            .setPositiveButton("Ya") { dialog, id ->
                deleteProject()
            }
            .setNegativeButton("Tidak") { dialog, id ->
                dialog.cancel()
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteProject() {
        AgricultureProjectRepository().deleteAgricultureProject(agricultureProject.agricultureId,
            {
                Toast.makeText(this, "Proyek berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            },
            { e ->
                Toast.makeText(this, "Gagal menghapus proyek: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000 // Request code for image picker
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateTo(AgrilistActivity::class.java)
                }
            }
        )
    }
}