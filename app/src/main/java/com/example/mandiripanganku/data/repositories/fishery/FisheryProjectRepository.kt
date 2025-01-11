package com.example.mandiripanganku.data.repositories.fishery

import android.util.Log
import com.example.mandiripanganku.data.models.fishery.FisheryProject
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FisheryProjectRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("fishery_project") // Nama koleksi untuk proyek perikanan

    fun addFisheryProject(fisheryProject: FisheryProject, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val fisheryData = fisheryProject.copy(
            createdAt = FieldValue.serverTimestamp(), // Set createdAt to server timestamp
            updatedAt = FieldValue.serverTimestamp()  // Set updatedAt to server timestamp
        )
        db.document(fisheryProject.fisheryId) // Pastikan fisheryId adalah String
            .set(fisheryData)
            .addOnSuccessListener {
                Log.d("Firestore", "Fishery project successfully written!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error writing fishery project: $e")
                onFailure(e)
            }
    }

    fun updateFisheryProject(fisheryId: String, updatedData: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updates = updatedData.toMutableMap()
        updates["updatedAt"] = FieldValue.serverTimestamp() // Update updatedAt to server timestamp

        db.document(fisheryId)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Fishery project successfully updated!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating fishery project: $e")
                onFailure(e)
            }
    }
}