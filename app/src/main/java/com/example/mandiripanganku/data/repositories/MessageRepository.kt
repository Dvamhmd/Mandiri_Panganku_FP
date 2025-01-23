package com.example.mandiripanganku.data.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.mandiripanganku.data.models.Message
import com.google.firebase.firestore.FieldValue

class MessageRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = firestore.collection("community") // Firestore collection for messages

    // Add a new message to Firestore
    fun addMessage(message: Message, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val messageData = message.copy(
            createdAt = FieldValue.serverTimestamp() // Set createdAt to server timestamp
        )
        db.document(message.messageId).set(messageData)
            .addOnSuccessListener {
                Log.d("Firestore", "Message successfully written!")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error writing message: $e")
                onFailure(e)
            }
    }

    // Fetch messages from Firestore
    fun getMessages(onSuccess: (List<Message>) -> Unit, onFailure: (Exception) -> Unit) {
        // Set up a listener for real-time updates
        db.orderBy("createdAt") // Order messages by creation time
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Firestore", "Error getting messages: $e")
                    onFailure(e)
                    return@addSnapshotListener
                }

                val messages = mutableListOf<Message>()
                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        val message = document.toObject(Message::class.java)
                        Log.d("Firestore", "Retrieved message: $message")
                        messages.add(message!!)
                    }
                    onSuccess(messages) // Return the list of messages
                }
            }
    }
}