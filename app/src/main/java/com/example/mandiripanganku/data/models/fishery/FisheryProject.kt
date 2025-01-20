package com.example.mandiripanganku.data.models.fishery

data class FisheryProject(
    val fisheryId: String = "",
    val kkNumber: String = "",
    val fishName: String = "",
    val fishPhoto: String = "",
    val quantity: Int = 0,
    val startDate: String = "",
    val createdAt: Any? = null, // Use Any? to allow for server timestamp
    val updatedAt: Any? = null
)
