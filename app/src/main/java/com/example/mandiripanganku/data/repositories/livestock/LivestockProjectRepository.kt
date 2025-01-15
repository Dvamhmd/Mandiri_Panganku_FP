package com.example.mandiripanganku.data.repositories.livestock

import android.util.Log
import com.example.mandiripanganku.data.models.livestock.LivestockProject
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class LivestockProjectRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("livestock_project")

    fun addLivestockProject(livestockProject: LivestockProject, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val farmingData = livestockProject.copy(
            createdAt = FieldValue.serverTimestamp(), // Set createdAt to server timestamp
            updatedAt = FieldValue.serverTimestamp()  // Set updatedAt to server timestamp
        )
        db.document(livestockProject.livestockId)
            .set(farmingData)
            .addOnSuccessListener {
                Log.d("Firestore", "Livestock project successfully written!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error writing agriculture project: $e")
                onFailure(e)
            }
    }

    fun updateLivestockProject(livestockId: String, updatedData: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updates = updatedData.toMutableMap()
        updates["updatedAt"] = FieldValue.serverTimestamp() // Update updatedAt to server timestamp

        db.document(livestockId)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Livestock project successfully updated!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating agriculture project: $e")
                onFailure(e)
            }
    }

    fun getLivestockProjects(kkNumber: String, onSuccess: (List<LivestockProject>) -> Unit, onFailure: (Exception) -> Unit) {
        db.whereEqualTo("kkNumber", kkNumber) // Filter berdasarkan kknomor
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

