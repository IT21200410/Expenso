package com.example.expenso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenso.databinding.ActivityChatBinding
import com.example.expenso.models.Message
import okhttp3.OkHttpClient

class chat : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chat)
//    }

    var client = OkHttpClient()
    private lateinit var chat : ActivityChatBinding
    private lateinit var chatGptViewModel: chat_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chat = ActivityChatBinding.inflate(layoutInflater)

        val binding = chat.root
        setContentView(binding)

        chatGptViewModel = ViewModelProvider(this)[chat_view::class.java]

        val llm = LinearLayoutManager(this)
        chat.recyclerView.layoutManager = llm

        chatGptViewModel.messageList.observe(this){messages ->
            val adapter = MessageAdapter(messages)
            chat.recyclerView.adapter = adapter
        }

        chat.sendBtn.setOnClickListener {



            val question = chat.messageEditText.text.toString()

            if(isEmpty(question) ){
                Toast.makeText(this, "enter a text", Toast.LENGTH_SHORT).show()
            }else{
                chatGptViewModel.addToChat(question, Message.USER,chatGptViewModel.getCurrentTimestamp())
                chat.messageEditText.setText("")
                chatGptViewModel.callApi(question)
            }


        }


    }
}