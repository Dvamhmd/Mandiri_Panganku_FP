package com.example.mandiripanganku.data.repositories

import android.util.Log
import com.example.mandiripanganku.data.models.Family
import com.example.mandiripanganku.data.models.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class FamilyRepository {
    private val database = FirebaseFirestore.getInstance().collection("family")

    suspend fun addFamily(family: Family): Result {
        try {
            Log.d("FamilyRepository", "Checking if KK number exists: ${family.kkNumber}")

            // Memeriksa apakah data dengan kk_number sudah ada
            val snapshot = database.document(family.kkNumber!!).get().await()
            if (snapshot.exists()) {
                Log.d("FamilyRepository", "KK number already exists: ${family.kkNumber}")
                return Result.AlreadyExists("Nomor KK sudah terdaftar", family = null)
            } else {
                // Menyimpan data keluarga ke Firestore dengan kk_number sebagai kunci
                Log.d("FamilyRepository", "Adding new family with KK number: ${family.kkNumber}")

                // Membuat objek Family dengan timestamps
                val familyData = family.copy(
                    createdAt = FieldValue.serverTimestamp(), // Set createdAt ke waktu server
                    updatedAt = FieldValue.serverTimestamp()  // Set updatedAt ke waktu server
                )

                database.document(family.kkNumber).set(familyData).await()
                Log.i("FamilyRepository", "Family added successfully: ${family.kkNumber}")
                return Result.Success("Registrasi Berhasil")
            }
        } catch (e: Exception) {
            // Menangani kesalahan
            Log.e("FamilyRepository", "Error occurred: ${e.message}", e)
            return Result.Error("Terjadi kesalahan repositori: ${e.message}")
        }
    }

    suspend fun getFamily(family: Family): Result {
        return try {
            Log.d("FamilyRepository", "Checking if KK number exists: ${family.kkNumber}")

            // Memeriksa apakah data dengan kk_number sudah ada
            val snapshot = database.document(family.kkNumber!!).get().await()

            if (snapshot.exists()) {
                Log.d("FamilyRepository", "KK number found: ${family.kkNumber}")
                val familyData = snapshot.toObject(Family::class.java)
                Result.AlreadyExists("Nomor KK telah terdaftar",familyData) // Mengembalikan data keluarga
            } else {
                Result.Error("Data Keluarga belum terdaftar")
            }
        } catch (e: Exception) {
            // Menangani kesalahan
            Log.e("FamilyRepository", "Error occurred: ${e.message}", e)
            Result.Error("Terjadi kesalahan repositori: ${e.message}")
        }
    }

    suspend fun updateFamily(family: Family): Result {
        return try {
            Log.d("FamilyRepository", "Updating family with KK number: ${family.kkNumber}")

            // Memperbarui data keluarga
            val updates = mapOf(
                "headOfFamily" to family.headOfFamily,
                "memberCount" to family.memberCount,
                "phoneNumber" to family.phoneNumber,
                "email" to family.email,
                "updatedAt" to FieldValue.serverTimestamp() // Set updatedAt ke waktu server
            )

            database.document(family.kkNumber!!).update(updates).await()
            Log.i("FamilyRepository", "Family updated successfully: ${family.kkNumber}")
            Result.Success("Update Berhasil")
        } catch (e: Exception) {
            // Menangani kesalahan
            Log.e("FamilyRepository", "Error occurred: ${e.message}", e)
            Result.Error("Terjadi kesalahan repositori: ${e.message}")
        }
    }
}