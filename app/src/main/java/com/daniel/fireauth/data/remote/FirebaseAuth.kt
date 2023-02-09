package com.daniel.fireauth.data.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


// Sign Up with email and password
fun FirebaseAuth.signUpEmailAndPassword(email: String, password: String): Task<AuthResult> =
    this.createUserWithEmailAndPassword(email, password)

// Sing In with email and password
fun FirebaseAuth.signInEmailAndPassword(email: String, password: String): Task<AuthResult> =
    this.signInWithEmailAndPassword(email, password)

// Sign In and Sign Up with google or facebook
fun FirebaseAuth.signInWithCredentialGoogleOrFacebook(credential: AuthCredential): Task<AuthResult> =
    this.signInWithCredential(credential)