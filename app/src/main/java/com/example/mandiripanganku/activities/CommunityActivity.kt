package com.example.mandiripanganku.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mandiripanganku.R
import com.example.mandiripanganku.data.models.FamilySession
import com.example.mandiripanganku.data.models.Message
import com.example.mandiripanganku.ui.adapters.MessageAdapter
import com.example.mandiripanganku.data.repositories.MessageRepository
import java.util.UUID

class CommunityActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private val messages: MutableList<Message> = mutableListOf() // List to hold messages
    private val messageRepository = MessageRepository() // Initialize your message repository
    private lateinit var writeEditText: EditText // EditText for writing messages
    private lateinit var kkNumber: String
    private lateinit var headOfFamily: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_community)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.community_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val topBarTitle = findViewById<TextView>(R.id.top_bar_title)
        topBarTitle.text = getString(R.string.community)

        kkNumber = FamilySession.family?.kkNumber.toString()
        headOfFamily = FamilySession.family?.headOfFamily.toString()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(messages, kkNumber)
        recyclerView.adapter = messageAdapter

        // Load messages from Firebase
        loadMessages()

        // Initialize EditText for writing messages
        writeEditText = findViewById(R.id.write)

        // Set up button click listeners
        findViewById<ImageView>(R.id.back).setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_home).setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_profile).setOnClickListener {
            navigateTo(ProfileActivity::class.java)
        }

        findViewById<ImageView>(R.id.ic_report).setOnClickListener {
            navigateTo(ReportActivity::class.java)
        }

        val iconCommunity = findViewById<ImageView>(R.id.ic_community)
        iconCommunity.setImageResource(R.drawable.ic_community_clicked)

        // Handle back press
        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateTo(HomeActivity::class.java)
                }
            }
        )

        // Set up send button click listener
        findViewById<ImageView>(R.id.send_button).setOnClickListener {
            sendMessage()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadMessages() {
        // Fetch messages from Firebase and add them to the messages list
        messageRepository.getMessages(
            onSuccess = { fetchedMessages -> // Provide the onSuccess implementation
                messages.clear()
                messages.addAll(fetchedMessages)
                messageAdapter.notifyDataSetChanged() // Notify the adapter of data changes

                // Scroll to the bottom of the RecyclerView
                if (messages.isNotEmpty()) {
                    recyclerView.scrollToPosition(messages.size - 1)
                }
            },
            onFailure = { e -> // Provide the onFailure implementation
                Toast.makeText(this, "Failed to load messages: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun sendMessage() {
        val content = writeEditText.text.toString()
        if (content.isNotEmpty()) {
            val message = Message(
                messageId = generateUniqueMessageId(), // Generate a unique ID for the message
                kkNumber = kkNumber, // Use the current user's kkNumber
                headOfFamily = headOfFamily, // Replace with actual head of family name
                content = content,
                createdAt = System.currentTimeMillis(), // Use current time as createdAt
                senderId = kkNumber // Set the senderId to the current user's kkNumber
            )

            messageRepository.addMessage(message, {
                writeEditText.text.clear() // Clear the input field after sending
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
                loadMessages() // Reload messages to show the new one
            }, { e ->
                Toast.makeText(this, "Failed to send message: ${e.message}", Toast.LENGTH_SHORT).show()
            })
        } else {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateUniqueMessageId(): String {
        return UUID.randomUUID().toString() // Generate a unique ID for the message
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish() // Close the current activity to prevent returning to it
    }
}