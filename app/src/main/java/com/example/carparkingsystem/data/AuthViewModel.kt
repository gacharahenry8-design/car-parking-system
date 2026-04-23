package com.example.carparkingsystem.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.carparkingsystem.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed class AuthEvent {
    object NavigateToLogin     : AuthEvent()
    object NavigateToDashboard : AuthEvent()
    data class ShowMessage(val message: String) : AuthEvent()
}

class AuthViewModel : ViewModel() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent: SharedFlow<AuthEvent> = _authEvent

    private fun emit(event: AuthEvent) {
        viewModelScope.launch { _authEvent.emit(event) }
    }

    fun signup(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            emit(AuthEvent.ShowMessage("Please fill all the fields"))
            return
        }
        if (password != confirmPassword) {
            emit(AuthEvent.ShowMessage("Passwords do not match"))
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user   = UserModel(username = username, email = email, userId = userId)
                    saveUserToDatabase(user)
                } else {
                    emit(AuthEvent.ShowMessage(task.exception?.message ?: "Registration failed"))
                }
            }
    }

    private fun saveUserToDatabase(user: UserModel) {
        FirebaseDatabase.getInstance()
            .getReference("Users/${user.userId}")
            .setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emit(AuthEvent.ShowMessage("Registered successfully!"))
                    emit(AuthEvent.NavigateToLogin)
                } else {
                    emit(AuthEvent.ShowMessage(task.exception?.message ?: "Failed to save user"))
                }
            }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            emit(AuthEvent.ShowMessage("Email and password are required"))
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emit(AuthEvent.ShowMessage("Login successful!"))
                    emit(AuthEvent.NavigateToDashboard)
                } else {
                    emit(AuthEvent.ShowMessage(task.exception?.message ?: "Login failed"))
                }
            }
    }
}