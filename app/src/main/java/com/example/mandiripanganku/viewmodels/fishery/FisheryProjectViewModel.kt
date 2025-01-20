package com.example.mandiripanganku.viewmodels.fishery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandiripanganku.data.models.fishery.FisheryProject
import com.example.mandiripanganku.data.repositories.fishery.FisheryProjectRepository

class FisheryProjectViewModel(private val fisheryRepository: FisheryProjectRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    // LiveData untuk menyimpan daftar proyek perikanan
    private val _fisheryProjects = MutableLiveData<List<FisheryProject>>()
    val fisheryProjects: LiveData<List<FisheryProject>> get() = _fisheryProjects

    // LiveData untuk menyimpan pesan kesalahan
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Fungsi untuk menyimpan proyek perikanan
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

    // Fungsi untuk mengambil proyek perikanan dari Firestore
    fun fetchFisheryProjects(kkNumber: String) {
        fisheryRepository.getFisheryProjects(kkNumber,
            onSuccess = { projects ->
                _fisheryProjects.value = projects
            },
            onFailure = { exception ->
                _errorMessage.value = "Error fetching fishery projects: ${exception.message}"
            }
        )
    }
}