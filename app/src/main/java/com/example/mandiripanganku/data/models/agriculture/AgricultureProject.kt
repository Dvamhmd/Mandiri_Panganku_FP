package com.example.mandiripanganku.data.models.agriculture

data class AgricultureProject(
    val agricultureId: String = "",
    val kkNumber: String = "",
    val plantName: String = "",
    var plantPhoto: String = "",
    val quantity: Int = 0,
    val plantingDate: String = "",
    val createdAt: Any? = null, // Use Any? to allow for server timestamp
    val updatedAt: Any? = null
)