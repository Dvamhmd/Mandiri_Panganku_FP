package com.example.mandiripanganku.viewmodels.fishery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mandiripanganku.data.repositories.fishery.FisheryProjectRepository

class FisheryProjectViewModelFactory(private val fisheryRepository: FisheryProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FisheryProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FisheryProjectViewModel(fisheryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}