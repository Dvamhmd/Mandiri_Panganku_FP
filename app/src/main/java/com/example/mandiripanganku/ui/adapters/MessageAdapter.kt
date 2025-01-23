package com.example.mandiripanganku.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(private val messages: List<Message>, private val kkNumber: String) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headOfFamily: TextView = itemView.findViewById(R.id.head_of_family)
        val messageContent: TextView = itemView.findViewById(R.id.message_content)
        val messageTime: TextView = itemView.findViewById(R.id.message_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_SENT) R.layout.sent_message else R.layout.received_message
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.headOfFamily.text = message.headOfFamily // Display the head of family
        holder.messageContent.text = message.content // Display the message content
        holder.messageTime.text = formatTimestamp(message.createdAt) // Format and display the timestamp
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == kkNumber) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    private fun formatTimestamp(timestamp: Any?): String {
        return when (timestamp) {
            is Long -> {
                val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                dateFormat.format(Date(timestamp))
            }
            is Timestamp -> {
                val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                dateFormat.format(timestamp.toDate()) // Convert Timestamp to Date and format
            }
            null -> "Unknown time" // Handle null case
            else -> "Invalid timestamp" // Handle unexpected types
        }
    }
    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }
}