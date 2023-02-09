package com.daniel.fireauth.ui.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.fireauth.domain.repository.FireAuthRepository
import com.daniel.fireauth.domain.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val authRepository: FireAuthRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    val profileImage = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    init {
        readUserData()
    }

    // Read user data to display activity
    private fun readUserData() {
        viewModelScope.launch {
            when (val read = authRepository.readUserData(context)) {
                is Resource.Success -> {
                    profileImage.value = read.data?.get(0)
                    username.value = read.data?.get(1)
                    email.value = read.data?.get(2)
                }
                is Resource.Error -> {
                    message.value = read.message.toString()
                }
            }
        }
    }

    // Remove user data from [sharedPreferences]
    fun deleteUserData() {
        viewModelScope.launch { authRepository.deleteUserData(context) }
    }

}