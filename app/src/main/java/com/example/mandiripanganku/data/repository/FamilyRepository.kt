package com.example.mandiripanganku.data.repository

import android.util.Log
import com.example.mandiripanganku.data.models.Family
import com.example.mandiripanganku.data.models.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FamilyRepository {
    private val database = FirebaseFirestore.getInstance().collection("table_family")

    suspend fun addFamily(family: Family): Result {
        try {
            Log.d("FamilyRepository", "Checking if KK number exists: ${family.kkNumber}")

            // Memeriksa apakah data dengan kk_number sudah ada
            val snapshot = database.document(family.kkNumber!!).get().await()
            if (snapshot.exists()) {

                Log.d("FamilyRepository", "KK number already exists: ${family.kkNumber}")
                Log.e("FamilyRepository", "KK number and password already exist: ${family.kkNumber}")
                return Result.AlreadyExists("Nomor KK sudah terdaftar",family = null)

            } else {
                // Menyimpan data keluarga ke Realtime Database dengan kk_number sebagai kunci
                Log.d("FamilyRepository", "Adding new family with KK number: ${family.kkNumber}")
                database.document(family.kkNumber).set(family).await()
                Log.i("FamilyRepository", "Family added successfully: ${family.kkNumber}")
                return Result.Success("Registrasi Berhasil") // Mengembalikan objek Family yang baru ditambahkan
            }
        } catch (e: Exception) {
            // Menangani kesalahan
            Log.e("FamilyRepository", "Error occurred: ${e.message}", e)
            return Result.Error("Terjadi kesalahan repositori: ${e.message}")
        }
    }

    suspend fun getFamily(family : Family): Any {
        return try {
            Log.d("FamilyRepository", "Checking if KK number exists: ${family.kkNumber}")

            // Memeriksa apakah data dengan kk_number sudah ada
            val snapshot = database.document(family.kkNumber!!).get().await()

            if (snapshot.exists()) {

                Log.d("FamilyRepository", "KK number already exists: ${family.kkNumber}")
                val familyData = snapshot.toObject(Family::class.java)
                Result.AlreadyExists("repositori: Data keluarga dengan Nomor KK ${family.kkNumber} sudah ada dan password sudah diatur.", familyData)

            } else {
                Result.Error("repositori: Data Keluarga belum terdaftar") // Mengembalikan objek Family yang baru ditambahkan
            }
        } catch (e: Exception) {
            // Menangani kesalahan
            Log.e("FamilyRepository", "Error occurred: ${e.message}", e)
            Result.Error("Terjadi kesalahan repositori: ${e.message}")
        }
    }
}