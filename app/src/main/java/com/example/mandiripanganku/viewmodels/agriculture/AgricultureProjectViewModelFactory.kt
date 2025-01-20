package com.example.mandiripanganku.viewmodels.agriculture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mandiripanganku.data.repositories.agriculture.AgricultureProjectRepository

class AgricultureProjectViewModelFactory(private val repository: AgricultureProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AgricultureProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AgricultureProjectViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}