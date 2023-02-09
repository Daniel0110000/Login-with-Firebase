package com.daniel.fireauth.domain.repository

import android.content.Context
import com.daniel.fireauth.domain.utilities.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface FireAuthRepository {

    suspend fun signUpEmailAndPassword(email: String, password: String): Resource<Task<AuthResult>>

    suspend fun signInEmailAndPassword(email: String, password: String): Resource<Task<AuthResult>>

    suspend fun signInWithCredentialGoogleOrFacebook(credential: AuthCredential): Resource<Task<AuthResult>>

    suspend fun insertUserData(context: Context, profileImage: String, username: String, email: String)

    suspend fun readUserData(context: Context): Resource<ArrayList<String>>

    suspend fun deleteUserData(context: Context)

}