package com.example.mandiripanganku.data.models

// Data class untuk respons setelah menambahkan keluarga
data class Family(
    val kkNumber: String? = null,
    val headOfFamily: String? = null,
    val memberCount: Int? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val password: String? = null,
    val createdAt: Any? = null, // Menyimpan waktu pembuatan
    val updatedAt: Any? = null   // Menyimpan waktu pembaruan
)