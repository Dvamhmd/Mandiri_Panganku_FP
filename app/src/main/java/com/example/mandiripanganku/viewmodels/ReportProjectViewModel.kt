package com.example.mandiripanganku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandiripanganku.data.models.ReportProject
import com.example.mandiripanganku.data.repositories.ReportProjectRepository

class ReportProjectViewModel(private val repository: ReportProjectRepository) : ViewModel() {

    private val _reportProjects = MutableLiveData<List<ReportProject>>()
    val reportProjects: LiveData<List<ReportProject>> get() = _reportProjects

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun addReportProject(reportProject: ReportProject) {
        repository.addReportProject(reportProject, {
            // Handle success (optional)
        }, { e ->
            _errorMessage.value = e.message
        })
    }

    fun getReportProjectById(reportId: String) {
        repository.getReportProjectById(reportId, { project ->
            // Handle the retrieved project (optional)
        }, { e ->
            _errorMessage.value = e.message
        })
    }

    fun updateReportProject(reportId: String, notes: String) {
        repository.updateReportProject(reportId, notes, {
            // Handle success (optional)
        }, { e ->
            _errorMessage.value = e.message
        })
    }

    fun deleteReportProject(reportId: String) {
        repository.deleteReportProject(reportId, {
            // Handle success (optional)
        }, { e ->
            _errorMessage.value = e.message
        })
    }

    fun fetchReportProjects(kkNumber: String) {
        repository.getReportProjects(kkNumber, { projects ->
            _reportProjects.value = projects
        }, { e ->
            _errorMessage.value = e.message
        })
    }
}