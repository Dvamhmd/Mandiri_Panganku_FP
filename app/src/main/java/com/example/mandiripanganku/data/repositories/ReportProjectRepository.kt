package com.example.mandiripanganku.data.repositories

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.mandiripanganku.data.models.ReportProject
import com.google.firebase.firestore.FieldValue

class ReportProjectRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("report_projects") // Firestore collection name

    @SuppressLint("SuspiciousIndentation")
    fun addReportProject(reportProject: ReportProject, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val reportData = reportProject.copy(
            createdAt = FieldValue.serverTimestamp(), // Set createdAt to server timestamp
            updatedAt = FieldValue.serverTimestamp()  // Set updatedAt to server timestamp
        )
        db.document(reportProject.reportId) // Assuming reportId is the ID
            .set(reportData)
            .addOnSuccessListener {
                Log.d("Firestore", "Report project successfully written!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error writing report project: $e")
                onFailure(e)
            }
    }

    fun getReportProjectById(reportId: String, onSuccess: (ReportProject) -> Unit, onFailure: (Exception) -> Unit) {
        db.document(reportId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val reportProject = document.toObject(ReportProject::class.java)
                    onSuccess(reportProject!!)
                } else {
                    Log.e("Firestore", "No such document")
                    onFailure(Exception("No such document"))
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting report project: $e")
                onFailure(e)
            }
    }

    fun updateReportProject(reportId: String, notes: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updates = mutableMapOf<String, Any>(
            "notes" to notes, // Update notes
            "updatedAt" to FieldValue.serverTimestamp() // Update updatedAt to server timestamp
        )

        db.document(reportId)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Report project fields successfully updated!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating report project fields: $e")
                onFailure(e)
            }
    }

    fun deleteReportProject(reportId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.document(reportId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Report project successfully deleted!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting report project: $e")
                onFailure(e)
            }
    }

    fun getReportProjects(kkNumber: String, onSuccess: (List<ReportProject>) -> Unit, onFailure: (Exception) -> Unit) {
        db.whereEqualTo("kkNumber", kkNumber) // Filter based on kkNumber
            .get()
            .addOnSuccessListener { documents ->
                val reportProjects = mutableListOf<ReportProject>()
                for (document in documents) {
                    val reportProject = document.toObject(ReportProject::class.java)
                    reportProjects.add(reportProject)
                }
                onSuccess(reportProjects)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting report projects: $e")
                onFailure(e)
            }
    }
}