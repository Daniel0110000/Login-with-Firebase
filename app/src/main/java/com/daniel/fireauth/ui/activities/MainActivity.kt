package com.daniel.fireauth.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.daniel.fireauth.databinding.ActivityMainBinding
import com.daniel.fireauth.ui.commons.Snackbar
import com.daniel.fireauth.ui.viewModels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        initUI()

    }

    // Initialization for the operation of the UI components
    private fun initUI() {
        binding.logOutButton.setOnClickListener {
            auth.signOut()
            viewModel.deleteUserData()
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        observers()
    }

    // ViewModel observers
    private fun observers() {
        viewModel.profileImage.observe(this) { image ->
            Picasso.get().load(image).into(binding.profileImage)
        }
        viewModel.username.observe(this) { username ->
            binding.welcomeUser.text = "Welcome $username!"
        }
        viewModel.email.observe(this) { email -> binding.email.text = email }
        viewModel.message.observe(this) { message ->
            Snackbar.showMessage(
                message,
                binding.mainLayout
            )
        }
    }
}