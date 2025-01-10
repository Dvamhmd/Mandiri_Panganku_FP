package com.example.mandiripanganku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mandiripanganku.data.models.Family
import com.example.mandiripanganku.data.models.Result
import com.example.mandiripanganku.data.repository.FamilyRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.example.mandiripanganku.data.models.FamilySession

class FamilyViewModel(
    private val familyRepository: FamilyRepository
) : ViewModel() {

    // LiveData untuk menyimpan status respons
    private val _responseStatus = MutableLiveData<String>()

    fun addFamily(family: Family) {
        viewModelScope.launch {
            try {
                when (val add = familyRepository.addFamily(family)) {
                    is Result.Success -> {
                        _responseStatus.value = add.message
                    }
                    is Result.AlreadyExists -> {
                        _responseStatus.value = add.message
                    }
                    is Result.Error -> {
                        _responseStatus.value = add.message
                    }
                }
            } catch (e: Exception) {
                _responseStatus.value = "Terjadi kesalahan viewmodel: ${e.message}"
            }
        }
    }





    fun getFamily(family: Family) {
        viewModelScope.launch {
            try {
                when (val result = familyRepository.getFamily(family)) {
                    is Result.Success -> {
                        _responseStatus.value = "viewmodel success: ${result.message}"
                    }
                    is Result.AlreadyExists -> {
                        _responseStatus.value = "viewmodel already: ${result.message}"
                        FamilySession.family = result.family
                    }
                    is Result.Error -> {
                        _responseStatus.value = "viewmodel eror: ${result.message}"
                        FamilySession.family = null
                    }
                }
            } catch (e: Exception) {
                _responseStatus.value = "Terjadi kesalahan viewmodel: ${e.message}"
            }
        }
    }
}