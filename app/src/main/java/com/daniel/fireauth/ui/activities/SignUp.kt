package com.daniel.fireauth.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.fireauth.databinding.ActivitySignUpBinding
import com.daniel.fireauth.ui.commons.Snackbar
import com.daniel.fireauth.ui.viewModels.AuthViewModel
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var callbackManager: CallbackManager

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        callbackManager = CallbackManager.Factory.create()

        initUI()

    }

    // Initialization for the operation of the UI components
    private fun initUI() {
        binding.signUp = viewModel
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.backLayout.setOnClickListener { onBack() }
        binding.facebookButton.setOnClickListener { signUpFacebook() }
        observers()
    }

    // Observers for data changes in the view model
    private fun observers() {

        // Check for changes in the "Message" variable and show it to the user
        viewModel.message.observe(this) { message ->
            if (message.isNotEmpty()) {
                Snackbar.showMessage(message, binding.signUpLayout)
                viewModel.message.value = ""
            }
        }

        // If the variable "isLoading" => "true" display the "ProgressBar"
        // .. else hidden the "ProgressBar"
        viewModel.isLoading.observe(this) { isLoading ->
            binding.signUpButton.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.signUpProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Check if everything was correct
        viewModel.completed.observe(this) { completed ->
            if (completed) {
                cleanFields()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        }

        // If there are changes in the "signInIntent" variable, execute the "launcher" function
        viewModel.signInIntent.observe(this) { intent -> launcher.launch(intent) }
    }

    // Gets the data returned from the activity [Google]
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                viewModel.handleResults(task)
            }
        }

    // Run authentication with facebook
    private fun signUpFacebook() = viewModel.facebook(this, callbackManager)

    // Gets the data returned from the activity [Facebook]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }

    private fun onBack() {
        finish()
        Animatoo.animateSlideRight(this)
    }

    // Clear activity fields
    private fun cleanFields() {
        binding.inputEmail.text.clear()
        binding.inputPassword.text.clear()
    }
}