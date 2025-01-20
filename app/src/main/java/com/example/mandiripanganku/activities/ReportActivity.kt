package com.example.mandiripanganku.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.data.models.ReportProject
import com.example.mandiripanganku.data.repositories.ReportProjectRepository
import java.text.SimpleDateFormat
import java.util.Calendar

class ReportActivity : AppCompatActivity() {

    private lateinit var reportType: EditText
    private lateinit var reportHead: EditText
    private lateinit var reportDate: TextView
    private lateinit var reportNote: EditText
    private lateinit var submitButton: Button
    private lateinit var agreementCheckBox: CheckBox // Checkbox for agreement
    private lateinit var progressBar: ProgressBar // ProgressBar for loading
    private val reportRepository = ReportProjectRepository() // Initialize the repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.report)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val iconReport = findViewById<ImageView>(R.id.ic_report)
        iconReport.setImageResource(R.drawable.ic_report_clicked)

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.report)

        reportType = findViewById(R.id.report_type)
        reportHead = findViewById(R.id.report_head)
        reportDate = findViewById(R.id.report_date)
        reportNote = findViewById(R.id.report_note)
        submitButton = findViewById(R.id.save_report) // Assuming you have a submit button in your layout
        agreementCheckBox = findViewById(R.id.report_check) // Assuming you have a checkbox in your layout
        progressBar = findViewById(R.id.progressBar) // Initialize the ProgressBar

        // Set up the date picker
        setupDatePicker()

        // Set up navigation
        setupNavigation()

        // Handle submit button click
        submitButton.setOnClickListener {
            submitReport()
        }

        // Handle back press
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateTo(HomeActivity::class.java)
                }
            }
        )
    }

    private fun setupNavigation() {
        findViewById<ImageView>(R.id.back).setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_home).setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_profile).setOnClickListener {
            navigateTo(ProfileActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_community).setOnClickListener {
            navigateTo(CommunityActivity::class.java)
        }
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish() // Close the current activity to prevent returning to it
    }

    private fun setupDatePicker() {
        reportDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                reportDate.text = selectedDate // Display the selected date in TextView
            }, year, month, day)

            datePickerDialog.show() // Show the date picker dialog
        }
    }

    private fun submitReport() {
        // Validate input
        val type = reportType.text.toString()
        val head = reportHead.text.toString()
        val date = reportDate.text.toString()
        val note = reportNote.text.toString()

        if (type.isEmpty() || head.isEmpty() || date.isEmpty() || note.isEmpty()) {
            Toast.makeText(this, "Silahkan isi semua form", Toast.LENGTH_SHORT).show()
            return
        }

        if (!agreementCheckBox.isChecked) { // Check if the checkbox is checked
            Toast.makeText(this, "Laporan harus benar", Toast.LENGTH_SHORT).show()
            return
        }

        val kkNumber = FamilySession.family!!.kkNumber.toString()
        // Generate reportId
        val reportId = generateReportId(kkNumber)

        // Create a ReportProject object
        val reportProject = ReportProject(
            reportId = reportId,
            kkNumber = kkNumber,
            projectName = type,
            notes = note,
            plantingDate = date
        )

        // Show the ProgressBar
        progressBar.visibility = View.VISIBLE

        // Save the report project to Firestore
        reportRepository.addReportProject(reportProject, {
            progressBar.visibility = View.GONE // Hide the ProgressBar
            Toast.makeText(this, "Berhasil membuat laporan", Toast.LENGTH_SHORT).show()
            // Optionally, navigate back to the previous activity
            navigateTo(HomeActivity::class.java)
        }, { e ->
            progressBar.visibility = View.GONE // Hide the ProgressBar
            Toast.makeText(this, "Gagal membuat laporan: ${e.message}", Toast.LENGTH_SHORT).show()
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun generateReportId(kkNumber: String): String {
        // Get the current time
        val currentTime = Calendar.getInstance()

        // Format for day, month, year, minutes, and seconds
        val day = SimpleDateFormat("dd").format(currentTime.time) // Day
        val month = SimpleDateFormat("MM").format(currentTime.time) // Month
        val year = SimpleDateFormat("yyyy").format(currentTime.time) // Year
        val minutes = SimpleDateFormat("mm").format(currentTime.time) // Minutes
        val seconds = SimpleDateFormat("ss").format(currentTime.time) // Seconds

        // Create report ID
        return "$kkNumber$day$month$year$minutes$seconds"
    }
}