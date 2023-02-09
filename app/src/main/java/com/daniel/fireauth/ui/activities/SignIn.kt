package com.daniel.fireauth.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.fireauth.databinding.ActivitySignInBinding
import com.daniel.fireauth.ui.commons.Snackbar
import com.daniel.fireauth.ui.viewModels.SignInViewModel
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignIn : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var callbackManager: CallbackManager

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isRegistered.observe(this){ isRegistered ->
            if(isRegistered){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        callbackManager = CallbackManager.Factory.create()
        initUI()
    }

    // Initialization for the operation of the UI components
    private fun initUI(){
        binding.singIn = viewModel
        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            Animatoo.animateSlideLeft(this)
        }
        binding.facebookButton.setOnClickListener { signUpFacebook() }

        observers()
    }

    // Observer for data change in the view model
    private fun observers(){
        viewModel.signInIntent.observe(this){ intent -> launcher.launch(intent) }

        // Check for changes in the "Message" variable and show it to the user
        viewModel.message.observe(this){ message ->
            if(message.isNotEmpty()){
                Snackbar.showMessage(message, binding.singInLayout)
                viewModel.message.value = ""
                cleanFields()
            }
        }

        // If the variable "isLoading" => "true" display the "ProgressBar"
        // .. else hidden the "ProgressBar"
        viewModel.isLoading.observe(this){ isLoading ->
            binding.signInButton.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.signInProgressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }

        // Check if everything was correct
        viewModel.completed.observe(this){ completed ->
            if(completed){
                cleanFields()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    // Gets the data returned from the activity [Google]
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            viewModel.handleResults(task)
        }
    }

    // Run authentication with Facebook
    private fun signUpFacebook() = viewModel.facebook(this, callbackManager)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Clean activity fields
    private fun cleanFields(){
        binding.inputEmail.text.clear()
        binding.inputPassword.text.clear()
    }

}