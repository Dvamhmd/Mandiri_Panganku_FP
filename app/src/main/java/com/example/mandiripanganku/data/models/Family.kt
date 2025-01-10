package com.example.mandiripanganku.data.models

import com.google.gson.annotations.SerializedName

// Data class untuk respons setelah menambahkan keluarga
data class Family(
    @SerializedName("kk_number") val kkNumber: String? = null,
    @SerializedName("head_of_family") val headOfFamily: String? = null,
    @SerializedName("member_count") val memberCount: Int? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null
)

object FamilySession{
    var family: Family? = null
    var isLoggedIn: Boolean = false
}