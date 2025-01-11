package com.example.mandiripanganku.viewmodels.agriculture

// AgricultureViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandiripanganku.data.models.agriculture.AgricultureProject
import com.example.mandiripanganku.data.repositories.agriculture.AgricultureProjectRepository

class AgricultureProjectViewModel(private val repository: AgricultureProjectRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    fun saveAgricultureProject(agricultureProject: AgricultureProject) {
        repository.addAgricultureProject(agricultureProject,
            {
                _uploadStatus.value = "Agriculture project saved successfully!"
            },
            { exception ->
                _uploadStatus.value = "Error saving project: ${exception.message}"
            }
        )
    }
}