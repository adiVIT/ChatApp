package com.example.chatit.utils

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun saveUserToDatabase(user: FirebaseUser, onComplete: (Boolean) -> Unit) {
        val userData = hashMapOf(
            "email" to user.email,
            "uid" to user.uid
        )
        database.child(user.uid).setValue(userData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase", "User data saved successfully")
                    onComplete(true)
                } else {
                    Log.e("Firebase", "Failed to save user: ${task.exception?.message}")
                    onComplete(false)
                }
            }
    }
}
