package com.example.mandiripanganku.viewmodels.livestock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandiripanganku.data.models.livestock.LivestockProject
import com.example.mandiripanganku.data.repositories.livestock.LivestockProjectRepository

class LivestockProjectViewModel(private val livestockRepository: LivestockProjectRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    // LiveData untuk menyimpan daftar proyek peternakan
    private val _livestockProjects = MutableLiveData<List<LivestockProject>>()
    val livestockProjects: LiveData<List<LivestockProject>> get() = _livestockProjects

    // LiveData untuk menyimpan pesan kesalahan
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Fungsi untuk menyimpan proyek peternakan
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

    // Fungsi untuk mengambil proyek peternakan dari Firestore
    fun fetchLivestockProjects(kkNumber : String) {
        livestockRepository.getLivestockProjects( kkNumber,
            onSuccess = { projects ->
                _livestockProjects.value = projects
            },
            onFailure = { exception ->
                _errorMessage.value = "Error fetching livestock projects: ${exception.message}"
            }
        )
    }
}