package com.example.mandiripanganku.viewmodels.livestock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mandiripanganku.data.repositories.livestock.LivestockProjectRepository

class LivestockProjectViewModelFactory(private val livestockRepository: LivestockProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LivestockProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LivestockProjectViewModel(livestockRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}