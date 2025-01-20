package com.example.mandiripanganku.data.repositories.livestock

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.mandiripanganku.data.models.livestock.LivestockProject // Updated to LivestockProject
import com.google.firebase.firestore.FieldValue

class LivestockProjectRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("livestock_project") // Updated to livestock_project

    @SuppressLint("SuspiciousIndentation")
    fun addLivestockProject(livestockProject: LivestockProject, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val livestockData = livestockProject.copy(
            createdAt = FieldValue.serverTimestamp(), // Set createdAt to server timestamp
            updatedAt = FieldValue.serverTimestamp()  // Set updatedAt to server timestamp
        )
        db.document(livestockProject.livestockId) // Assuming livestockId is the ID
            .set(livestockData)
            .addOnSuccessListener {
                Log.d("Firestore", "Livestock project successfully written!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error writing livestock project: $e")
                onFailure(e)
            }
    }

    fun getLivestockProjectById(livestockId: String, onSuccess: (LivestockProject) -> Unit, onFailure: (Exception) -> Unit) {
        db.document(livestockId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val livestockProject = document.toObject(LivestockProject::class.java)
                    onSuccess(livestockProject!!)
                } else {
                    Log.e("Firestore", "No such document")
                    onFailure(Exception("No such document"))
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting livestock project: $e")
                onFailure(e)
            }
    }

    fun updateLivestockProject(livestockId: String, quantity: Int, photoUrl: String?, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updates = mutableMapOf<String, Any>(
            "quantity" to quantity // Update quantity
        )
        photoUrl?.let {
            updates["animalPhoto"] = it // Update photo URL if provided
        }
        updates["updatedAt"] = FieldValue.serverTimestamp() // Update updatedAt to server timestamp

        db.document(livestockId)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Livestock project fields successfully updated!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating livestock project fields: $e")
                onFailure(e)
            }
    }

    // New method to delete a livestock project
    fun deleteLivestockProject(livestockId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.document(livestockId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Livestock project successfully deleted!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting livestock project: $e")
                onFailure(e)
            }
    }

    fun getLivestockProjects(kkNumber: String, onSuccess: (List<LivestockProject>) -> Unit, onFailure: (Exception) -> Unit) {
        db.whereEqualTo("kkNumber", kkNumber) // Filter based on kkNumber
            .get()
            .addOnSuccessListener { documents ->
                val livestockProjects = mutableListOf<LivestockProject>()
                for (document in documents) {
                    val livestockProject = document.toObject(LivestockProject::class.java)
                    livestockProjects.add(livestockProject)
                }
                onSuccess(livestockProjects)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting livestock projects: $e")
                onFailure(e)
            }
    }
}