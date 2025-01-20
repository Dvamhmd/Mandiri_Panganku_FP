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

    fun getAgricultureProjectById(agricultureId: String, onSuccess: (AgricultureProject) -> Unit, onFailure: (Exception) -> Unit) {
        db.document(agricultureId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val agricultureProject = document.toObject(AgricultureProject::class.java)
                    onSuccess(agricultureProject!!)
                } else {
                    Log.e("Firestore", "No such document")
                    onFailure(Exception("No such document"))
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting agriculture project: $e")
                onFailure(e)
            }
    }

    fun updateAgricultureProject(agricultureId: String, quantity: Int, photoUrl: String?, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updates = mutableMapOf<String, Any>(
            "quantity" to quantity // Update quantity
        )
        photoUrl?.let {
            updates["plantPhoto"] = it // Update photo URL if provided
        }
        updates["updatedAt"] = FieldValue.serverTimestamp() // Update updatedAt to server timestamp

        db.document(agricultureId)
            .update(updates)
            .addOnSuccessListener {
                Log.d("Firestore", "Agriculture project fields successfully updated!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating agriculture project fields: $e")
                onFailure(e)
            }
    }

    // New method to delete an agriculture project
    fun deleteAgricultureProject(agricultureId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.document(agricultureId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Agriculture project successfully deleted!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting agriculture project: $e")
                onFailure(e)
            }
    }

    fun getAgricultureProjects(kkNumber: String, onSuccess: (List<AgricultureProject>) -> Unit, onFailure: (Exception) -> Unit) {
        db.whereEqualTo("kkNumber", kkNumber) // Filter berdasarkan kkNumber
            .get()
            .addOnSuccessListener { documents ->
                val agricultureProjects = mutableListOf<AgricultureProject>()
                for (document in documents) {
                    val agricultureProject = document.toObject(AgricultureProject::class.java)
                    agricultureProjects.add(agricultureProject)
                }
                onSuccess(agricultureProjects)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting agriculture projects: $e")
                onFailure(e)
            }
    }
}