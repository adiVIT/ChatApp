package com.example.chatit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : ComponentActivity() {
    private lateinit var chatUserEmail: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessagesAdapter
    private val messagesList = mutableListOf<Message>()
    private lateinit var sendButton: Button
    private lateinit var messageEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatUserEmail = intent.getStringExtra("chatUserEmail") ?: "No Email"
        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Find views
        recyclerView = findViewById(R.id.recyclerView)
        sendButton = findViewById(R.id.sendButton)
        messageEditText = findViewById(R.id.messageEditText)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessagesAdapter(messagesList)
        recyclerView.adapter = messageAdapter
        markMessagesAsRead()
        // Send message on button click
        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                messageEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch messages from Firebase in real-time
        fetchMessages()

    }
    private fun markMessagesAsRead() {
        val user = auth.currentUser
        if (user != null) {
            val safeUserEmail = user.email?.replace(".", "_") ?: "NoEmail"
            val safeChatUserEmail = chatUserEmail.replace(".", "_")
            val chatNodeId = if (safeUserEmail < safeChatUserEmail) {
                "${safeUserEmail}_${safeChatUserEmail}"
            } else {
                "${safeChatUserEmail}_${safeUserEmail}"
            }
            database.child("messages").child(chatNodeId).get().addOnSuccessListener { snapshot ->
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        if (message.senderId != user.uid && message.status != "read") {
                            messageSnapshot.ref.child("status").setValue("read")
                        }
                    }
                }
            }
        }
    }

    private fun sendMessage(messageText: String) {
        val user = auth.currentUser
        if (user != null) {
            val safeUserEmail = user.email?.replace(".", "_") ?: "NoEmail"
            val safeChatUserEmail = chatUserEmail.replace(".", "_")
            val chatNodeId = if (safeUserEmail < safeChatUserEmail) {
                "${safeUserEmail}_${safeChatUserEmail}"
            } else {
                "${safeChatUserEmail}_${safeUserEmail}"
            }
            val messageId = database.child("messages").child(chatNodeId).push().key
            val message = Message(
                user = user.email ?: "Anonymous",
                message = messageText,
                senderId = user.uid,
                messageId = messageId ?: ""
            )
            messageId?.let {
                database.child("messages").child(chatNodeId).child(it).setValue(message)
            }
        }
    }


    private fun fetchMessages() {
        val user = auth.currentUser
        if (user != null) {
            val safeUserEmail = user.email?.replace(".", "_") ?: "NoEmail"
            val safeChatUserEmail = chatUserEmail.replace(".", "_")
            val chatNodeId = if (safeUserEmail < safeChatUserEmail) {
                "${safeUserEmail}_${safeChatUserEmail}"
            } else {
                "${safeChatUserEmail}_${safeUserEmail}"
            }

            database.child("messages").child(chatNodeId)
                .addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        val message = snapshot.getValue(Message::class.java)
                        message?.let {
                            // Add message to the list and notify adapter
                            messagesList.add(it)
                            messageAdapter.notifyItemInserted(messagesList.size - 1)
                            recyclerView.scrollToPosition(messagesList.size - 1)

                            // Mark message as read if it is from another user
                            if (it.senderId != user.uid && it.status != "read") {
                                snapshot.ref.child("status").setValue("read")
                            }
                        }
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ChatActivity, "Failed to load messages.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }



}
