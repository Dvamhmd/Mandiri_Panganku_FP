package com.example.mandiripanganku.data.repositories.fishery

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.mandiripanganku.data.models.fishery.FisheryProject // Updated to FisheryProject
import com.google.firebase.firestore.FieldValue

class FisheryProjectRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("fishery_project") // Updated to fishery_project

    @SuppressLint("SuspiciousIndentation")
    fun addFisheryProject(fisheryProject: FisheryProject, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val fisheryData = fisheryProject.copy(
            createdAt = FieldValue.serverTimestamp(), // Set createdAt to server timestamp
            updatedAt = FieldValue.serverTimestamp()  // Set updatedAt to server timestamp
        )
        db.document(fisheryProject.fisheryId) // Assuming fisheryId is the ID
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

    fun getFisheryProjectById(fisheryId: String, onSuccess: (FisheryProject) -> Unit, onFailure: (Exception) -> Unit) {
        db.document(fisheryId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val fisheryProject = document.toObject(FisheryProject::class.java)
                    onSuccess(fisheryProject!!)
                } else {
                    Log.e("Firestore", "No such document")
                    onFailure(Exception("No such document"))
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting fishery project: $e")
                onFailure(e)
            }
    }

    fun updateFisheryProject(fisheryId: String, quantity: Int, photoUrl: String?, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updates = mutableMapOf<String, Any>(
            "quantity" to quantity // Update quantity
        )
        photoUrl?.let {
            updates["fishPhoto"] = it // Update photo URL if provided
        }
        updates["updatedAt"] = FieldValue.serverTimestamp() // Update updatedAt to server timestamp

        db.document(fisheryId)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Fishery project fields successfully updated!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating fishery project fields: $e")
                onFailure(e)
            }
    }

    // New method to delete a fishery project
    fun deleteFisheryProject(fisheryId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.document(fisheryId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Fishery project successfully deleted!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting fishery project: $e")
                onFailure(e)
            }
    }

    fun getFisheryProjects(kkNumber: String, onSuccess: (List<FisheryProject>) -> Unit, onFailure: (Exception) -> Unit) {
        db.whereEqualTo("kkNumber", kkNumber) // Filter based on kkNumber
            .get()
            .addOnSuccessListener { documents ->
                val fisheryProjects = mutableListOf<FisheryProject>()
                for (document in documents) {
                    val fisheryProject = document.toObject(FisheryProject::class.java)
                    fisheryProjects.add(fisheryProject)
                }
                onSuccess(fisheryProjects)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting fishery projects: $e")
                onFailure(e)
            }
    }
}