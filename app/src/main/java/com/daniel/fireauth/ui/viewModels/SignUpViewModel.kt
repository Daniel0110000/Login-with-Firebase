package com.daniel.fireauth.ui.viewModels

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniel.fireauth.domain.useCases.EmailAndPasswordUseCase
import com.daniel.fireauth.domain.useCases.FacebookUseCase
import com.daniel.fireauth.domain.useCases.GoogleUseCase
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val emailAndPasswordUseCase: EmailAndPasswordUseCase,
        private val googleUseCase: GoogleUseCase,
        private val facebookUseCase: FacebookUseCase,
        @ApplicationContext val context: Context
    ): ViewModel() {


    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val completed = MutableLiveData<Boolean>()
    val signInIntent = MutableLiveData<Intent>()

    init {
        email.value = ""
        password.value = ""
    }

    // Authentication with email and password
    fun signUp(){
        isLoading.value = true
        if(email.value!!.isNotEmpty() && password.value!!.isNotEmpty()){ // Check if the variables: "email" and "password" are not empty, to execute the authentication
            viewModelScope.launch {
                val result = emailAndPasswordUseCase.signUp(email.value.toString(), password.value.toString())
                if(result != null){
                    result.apply {
                        addOnSuccessListener {
                            completed.value = true
                            cleanFields()
                        }
                        addOnFailureListener { e ->
                            message.value = e.message.toString()
                            isLoading.value = false
                            cleanFields()
                        }
                    }
                }else{
                    isLoading.value = false
                    message.value = "An error has occurred. Please try again"
                    cleanFields()
                }
            }
        }else{ // ... else display a message and clear the fields
            message.value = "Empty fields!"
            cleanFields()
        }
    }

    // Run authentication with google
    fun google(){ signInIntent.value = googleUseCase.providerGoogleSignInClient(context) }

    // Results handler for Google authentication
    fun handleResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if(account != null){
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModelScope.launch {
                    val google = googleUseCase.insertCredentials(credential)
                    if(google != null){
                        google.apply {
                            addOnSuccessListener { completed.value = true }
                            addOnFailureListener { e -> message.value = e.message.toString() }
                        }
                    }else message.value = "An error has occurred. Please try again"
                }
            }
        }else message.value = task.exception!!.message.toString()
    }

    // Run authentication with facebook
    fun facebook(activity: Activity, callbackManager: CallbackManager){
        viewModelScope.launch {
            val result = facebookUseCase.invoke(activity, callbackManager)
            if(result != null){
                result.apply {
                    addOnSuccessListener { completed.value = true }
                    addOnFailureListener { e -> message.value = e.message.toString() }
                }
            }else message.value = "An error has occurred. Please try again"
        }
    }

    // Clean the variables: "email" and "password"
    private fun cleanFields(){
        email.value = ""
        password.value = ""
        isLoading.value = false
    }

}