package com.daniel.fireauth.data.repository

import android.content.Context
import com.daniel.fireauth.data.local.SharedPreferences
import com.daniel.fireauth.data.remote.signInWithCredentialGoogleOrFacebook
import com.daniel.fireauth.data.remote.signUpEmailAndPassword
import com.daniel.fireauth.domain.repository.FireAuthRepository
import com.daniel.fireauth.domain.utilities.CallHandler
import com.daniel.fireauth.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FireAuthRepositoryImpl
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val sh: SharedPreferences
) : FireAuthRepository {

    override suspend fun signUpEmailAndPassword(email: String, password: String): Resource<Task<AuthResult>> = CallHandler.callHandler {
        auth.signUpEmailAndPassword(email, password)
    }

    override suspend fun signInEmailAndPassword(email: String, password: String): Resource<Task<AuthResult>> = CallHandler.callHandler {
        auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun signInWithCredentialGoogleOrFacebook(credential: AuthCredential): Resource<Task<AuthResult>> = CallHandler.callHandler {
        auth.signInWithCredentialGoogleOrFacebook(credential)
    }

    override suspend fun insertUserData(context: Context, profileImage: String, username: String, email: String) {
        CallHandler.callHandler { sh.insertUserData(context, profileImage, username, email) }
    }

    override suspend fun readUserData(context: Context): Resource<ArrayList<String>> = CallHandler.callHandler { sh.readUserData(context) }

    override suspend fun deleteUserData(context: Context) {
        CallHandler.callHandler { sh.deleteUserData(context) }
    }


}