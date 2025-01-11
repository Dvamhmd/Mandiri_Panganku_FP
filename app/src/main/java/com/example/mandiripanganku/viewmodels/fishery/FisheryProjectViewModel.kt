package com.example.mandiripanganku.viewmodels.fishery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandiripanganku.data.models.fishery.FisheryProject
import com.example.mandiripanganku.data.repositories.fishery.FisheryProjectRepository

class FisheryProjectViewModel(private val fisheryRepository: FisheryProjectRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    fun saveFisheryProject(fisheryProject: FisheryProject) {
        fisheryRepository.addFisheryProject(fisheryProject,
            {
                _uploadStatus.value = "Fishery project saved successfully!"
            },
            { exception ->
                _uploadStatus.value = "Error saving project: ${exception.message}"
            }
        )
    }
}