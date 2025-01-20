package com.example.mandiripanganku.viewmodels.agriculture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandiripanganku.data.models.agriculture.AgricultureProject
import com.example.mandiripanganku.data.repositories.agriculture.AgricultureProjectRepository

class AgricultureProjectViewModel(private val agricultureRepository: AgricultureProjectRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> get() = _uploadStatus

    // LiveData untuk menyimpan daftar proyek pertanian
    private val _agricultureProjects = MutableLiveData<List<AgricultureProject>>()
    val agricultureProjects: LiveData<List<AgricultureProject>> get() = _agricultureProjects

    // LiveData untuk menyimpan pesan kesalahan
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Fungsi untuk menyimpan proyek pertanian
    fun saveAgricultureProject(agricultureProject: AgricultureProject) {
        agricultureRepository.addAgricultureProject(agricultureProject,
            {
                _uploadStatus.value = "Agriculture project saved successfully!"
            },
            { exception ->
                _uploadStatus.value = "Error saving project: ${exception.message}"
            }
        )
    }

    // Fungsi untuk mengambil proyek pertanian dari Firestore
    fun fetchAgricultureProjects(kkNumber: String) {
        agricultureRepository.getAgricultureProjects(kkNumber,
            onSuccess = { projects ->
                _agricultureProjects.value = projects
            },
            onFailure = { exception ->
                _errorMessage.value = "Error fetching agriculture projects: ${exception.message}"
            }
        )
    }
}