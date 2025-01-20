package com.example.mandiripanganku.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mandiripanganku.data.repositories.ReportProjectRepository

class ReportProjectViewModelFactory(private val repository: ReportProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportProjectViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}