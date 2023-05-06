package com.example.expenso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.expenso.model.Message




class MessageAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    companion object {
        const val USER = 0
        const val BOT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return when (viewType) {
            USER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_right_item, parent, false)
                MessageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_left_item, parent, false)
                MessageViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return when (message.sentBy) {
            Message.USER -> USER
            else -> BOT
        }
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val leftChatTextView: TextView? = itemView.findViewById(R.id.left_chat)
        val leftChatTimestamp: TextView? = itemView.findViewById(R.id.left_chat)
        val rightChatTextView: TextView? = itemView.findViewById(R.id.right_chat)
        val rightChatTimestamp: TextView? = itemView.findViewById(R.id.right_chat)

        fun bind(message: Message) {
            when (message.sentBy) {
                Message.USER -> {
                    rightChatTextView?.text = message.message
//                    rightChatTimestamp?.text = message.timestamp
                }
                else -> {
                    leftChatTextView?.text = message.message
//                    leftChatTimestamp?.text = message.timestamp
                }
            }
        }
    }
}