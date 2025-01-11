package com.example.mandiripanganku.data.models.livestock

data class LivestockProject(
    val livestockId: String = "",
    val kkNumber: String = "",
    val animalName: String = "",
    val animalPhoto: String = "",
    val quantity: Int = 0,
    val startDate: String = "",
    val createdAt: Any? = null, // Use Any? to allow for server timestamp
    val updatedAt: Any? = null
)
