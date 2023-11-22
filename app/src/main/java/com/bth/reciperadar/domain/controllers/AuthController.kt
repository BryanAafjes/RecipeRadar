package com.bth.reciperadar.domain.controllers

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

class AuthController(val applicationContext: Context) {
    private var _auth: FirebaseAuth = Firebase.auth
    private var _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow<FirebaseUser?>(_auth.currentUser)

    val auth: FirebaseAuth
        get() = _auth

    val currentUser: MutableStateFlow<FirebaseUser?>
        get() = _currentUser

    fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = _auth.currentUser
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
    }

}