package com.example.mandiripanganku.data.repositories.agriculture

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.mandiripanganku.data.models.agriculture.AgricultureProject
import com.google.firebase.firestore.FieldValue

class AgricultureProjectRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("agriculture_project")

    @SuppressLint("SuspiciousIndentation")
    fun addAgricultureProject(agricultureProject: AgricultureProject, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val agricultureData = agricultureProject.copy(
            createdAt = FieldValue.serverTimestamp(), // Set createdAt to server timestamp
            updatedAt = FieldValue.serverTimestamp()  // Set updatedAt to server timestamp
        )
            db.document(agricultureProject.agricultureId)
            .set(agricultureData)
            .addOnSuccessListener {
                Log.d("Firestore", "Agriculture project successfully written!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error writing agriculture project: $e")
                onFailure(e)
            }
    }

    fun updateAgricultureProject(agricultureId: String, updatedData: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updates = updatedData.toMutableMap()
        updates["updatedAt"] = FieldValue.serverTimestamp() // Update updatedAt to server timestamp

        db.document(agricultureId)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Agriculture project successfully updated!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating agriculture project: $e")
                onFailure(e)
            }
    }
}