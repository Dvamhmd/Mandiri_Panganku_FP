package com.example.mandiripanganku.viewmodels.livestock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandiripanganku.data.models.livestock.LivestockProject
import com.example.mandiripanganku.data.repositories.livestock.LivestockProjectRepository

class LivestockProjectViewModel(private val livestockRepository: LivestockProjectRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    fun saveLivestockProject(livestockProject: LivestockProject) {
        livestockRepository.addLivestockProject(livestockProject,
            {
                _uploadStatus.value = "Agriculture project saved successfully!"
            },
            { exception ->
                _uploadStatus.value = "Error saving project: ${exception.message}"
            }
        )
    }
}