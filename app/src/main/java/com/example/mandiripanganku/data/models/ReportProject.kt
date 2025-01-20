package com.example.mandiripanganku.data.models

data class ReportProject(
    val reportId: String = "",
    val kkNumber: String = "",
    val projectName: String = "",
    val headOfFamily: String = FamilySession.family?.headOfFamily.toString(),
    var notes: String = "",
    val plantingDate: String = "",
    val createdAt: Any? = null, // Use Any? to allow for server timestamp
    val updatedAt: Any? = null
)