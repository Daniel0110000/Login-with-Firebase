package com.daniel.fireauth.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.fireauth.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI(){
        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            Animatoo.animateSlideLeft(this)
        }
    }
}