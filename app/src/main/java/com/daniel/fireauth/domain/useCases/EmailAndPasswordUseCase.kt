package com.daniel.fireauth.domain.useCases

import com.daniel.fireauth.domain.repository.FireAuthRepository
import com.daniel.fireauth.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class EmailAndPasswordUseCase
@Inject
constructor(private val authRepository: FireAuthRepository) {

    // Authentication with email and password
    suspend fun signUp(email: String, password: String): Task<AuthResult>? {
        return when (val auth = authRepository.signUpEmailAndPassword(email, password)) {
            is Resource.Success -> auth.data
            is Resource.Error -> null
        }
    }

    // Authentication with email and password
    suspend fun signIn(email: String, password: String): Task<AuthResult>? {
        return when (val auth = authRepository.signInEmailAndPassword(email, password)) {
            is Resource.Success -> auth.data
            is Resource.Error -> null
        }
    }

}