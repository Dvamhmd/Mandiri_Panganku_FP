package com.example.mandiripanganku.data.models

sealed class Result {
    data class Success(val message: String) : Result()
    data class AlreadyExists(val message: String, val family: Family?) : Result()
    data class Error(val message: String) : Result()
}