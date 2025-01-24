package com.example.chatit

import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
class UserListActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userListView: ListView
    private lateinit var signOutButton: Button
    private lateinit var emailAdapter: EmailAdapter
    private val userEmailsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        if (auth.currentUser == null) {
            // User is not signed in, redirect to login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        // Find views
        userListView = findViewById(R.id.userListView)
        signOutButton = findViewById(R.id.signOutButton)

        // Set up custom adapter
        emailAdapter = EmailAdapter(this, userEmailsList)
        userListView.adapter = emailAdapter

        // Fetch users' emails from Firebase
        fetchUsers()

        // Handle user click to open chat
        userListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUserEmail = userEmailsList[position]
            openChatWithUser(selectedUserEmail)
        }

        // Handle sign-out button click
        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun fetchUsers() {
        database.child("users") // Correctly pointing to the 'users' node
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val userEmail = snapshot.child("email").value as? String
                    if (userEmail != null) {
                        Log.d("Firebase", "Found new user: $userEmail")
                        if (!userEmailsList.contains(userEmail)) {
                            userEmailsList.add(userEmail)  // Add new user to the list
                            emailAdapter.notifyDataSetChanged()  // Notify adapter about the new item
                        }
                    } else {
                        Log.e("Firebase", "Email is missing in snapshot: $snapshot")
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val updatedUserEmail = snapshot.child("email").value as? String
                    if (updatedUserEmail != null) {
                        Log.d("Firebase", "User email updated: $updatedUserEmail")
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val removedUserEmail = snapshot.child("email").value as? String
                    if (removedUserEmail != null) {
                        Log.d("Firebase", "User removed: $removedUserEmail")
                        userEmailsList.remove(removedUserEmail)  // Remove the user from the list
                        emailAdapter.notifyDataSetChanged()  // Notify adapter about the change
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // Handle any movement of data if necessary
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Database error: ${error.message}", error.toException())
                }
            })
    }


    private fun openChatWithUser(userEmail: String) {
        Log.d("UserListActivity", "Selected user email: $userEmail")
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatUserEmail", userEmail)
        startActivity(intent)
    }


    private fun signOut() {
        auth.signOut()
        // Redirect to login screen after sign-out
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}