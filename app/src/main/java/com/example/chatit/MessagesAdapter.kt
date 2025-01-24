package com.example.chatit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
class MessagesAdapter(private val messagesList: MutableList<Message>) :
    RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        // Bind user and message content
        holder.userTextView.text = currentMessage.user
        holder.messageTextView.text = currentMessage.message

        // Format and bind timestamp
        val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date(currentMessage.timestamp))
        holder.timestampTextView.text = formattedTime

        // Bind message status (sent, delivered, read)
        holder.statusTextView.text = currentMessage.status.capitalize()
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userTextView: TextView = itemView.findViewById(R.id.userTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)

        // Add views for timestamp and status
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
    }
}
