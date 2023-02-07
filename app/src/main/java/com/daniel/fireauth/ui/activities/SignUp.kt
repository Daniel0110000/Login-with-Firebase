package com.daniel.fireauth.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.fireauth.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI(){
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.backLayout.setOnClickListener { onBack() }
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            onBack()
        }
    }

    private fun onBack(){
        finish()
        Animatoo.animateSlideRight(this)
    }
}