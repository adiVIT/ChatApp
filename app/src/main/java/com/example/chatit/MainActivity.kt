package com.example.chatit
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set the theme before calling super.onCreate
        setTheme(R.style.AppTheme)  // Use your theme here

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if the user is logged in
        if (auth.currentUser != null) {
            // If user is logged in, navigate directly to UserListActivity
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
            finish()  // Close MainActivity so the user can't go back to it
        } else {
            // If user is not logged in, show the login button
            val loginButton = findViewById<Button>(R.id.loginButton)
            loginButton.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
