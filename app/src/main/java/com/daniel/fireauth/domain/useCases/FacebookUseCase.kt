package com.daniel.fireauth.domain.useCases

import android.app.Activity
import com.daniel.fireauth.domain.repository.FireAuthRepository
import com.daniel.fireauth.domain.utilities.Resource
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FacebookUseCase @Inject
constructor(private val authRepository: FireAuthRepository){

    // Sign In and Sign Up with Facebook
    suspend fun invoke(activity: Activity, callbackManager: CallbackManager): Task<AuthResult>?{
        return suspendCoroutine { count ->
            LoginManager.getInstance().logInWithReadPermissions(activity, listOf("public_profile", "email")) // Get permissions to get profile picture and email
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                override fun onCancel() { count.resume(null) }
                override fun onError(error: FacebookException) { count.resume(null) }
                override fun onSuccess(result: LoginResult) {
                    result.let {
                        val credential = FacebookAuthProvider.getCredential(it.accessToken.token)
                        CoroutineScope(Dispatchers.IO).launch{ count.resume(insertCredential(credential)) }
                    }
                }

            })
        }

    }

    // Insert credentials obtained from [Facebook] api
    private suspend fun insertCredential(account: AuthCredential): Task<AuthResult>? {
        return when(val auth = authRepository.signInWithCredentialGoogleOrFacebook(account)){
            is Resource.Success -> auth.data
            is Resource.Error -> null
        }
    }
}