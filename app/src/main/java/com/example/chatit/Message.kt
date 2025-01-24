package com.example.chatit

data class Message(
    val user: String = "",           // Sender's name or email
    val message: String = "",        // The message content
    val timestamp: Long = System.currentTimeMillis(), // Time when the message was sent
    val senderId: String = "",       // Unique identifier for the sender
    val status: String = "sent",     // Message status: "sent", "delivered", "read"
    val type: String = "text",       // Message type: "text", "image", "video", etc.
    val messageId: String = ""       // Unique identifier for the message
)
