package com.example.mandiripanganku.data.models

data class Message(
    val messageId: String = "",
    val kkNumber: String = "",
    val headOfFamily: String = "",
    val content: String = "",
    val createdAt: Any? = null, // Change to Any?
    val senderId: String = ""
)