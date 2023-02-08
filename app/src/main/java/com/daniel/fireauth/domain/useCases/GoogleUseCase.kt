package com.daniel.fireauth.domain.useCases

import android.content.Context
import android.content.Intent
import com.daniel.fireauth.R
import com.daniel.fireauth.domain.repository.FireAuthRepository
import com.daniel.fireauth.domain.utilities.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class GoogleUseCase @Inject constructor(
    private val authRepository: FireAuthRepository
) {
    // Provider the google client
    fun providerGoogleSignInClient(context: Context): Intent{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient.signInIntent
    }

    // Insert credentials obtained from [Google] api
    suspend fun insertCredentials(account: AuthCredential): Task<AuthResult>? {
        return when(val auth = authRepository.signInWithCredentialGoogleOrFacebook(account)){
            is Resource.Success -> auth.data
            is Resource.Error -> null
        }
    }
}