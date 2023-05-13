package com.example.expenso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Reminders
import android.provider.Settings
import android.text.TextUtils
import android.text.TextUtils.isEmpty
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenso.databinding.ActivityChatBinding
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.Message
import com.google.android.material.navigation.NavigationView
import okhttp3.OkHttpClient

class chat : AppCompatActivity() {

    var client = OkHttpClient()
    private lateinit var chat : ActivityChatBinding
    private lateinit var chatGptViewModel: chat_view
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chat = ActivityChatBinding.inflate(layoutInflater)

        val binding = chat.root
        setContentView(binding)

        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_background))
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        usernameTextView = headerView.findViewById(R.id.user_name)
        emailTextView = headerView.findViewById(R.id.email)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                R.id.nav_transactions -> {
                    startActivity(Intent(this, Display_Transactions::class.java))
                    finish()
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, Settings::class.java))
                    finish()
                }
                R.id.nav_chat -> {
                    false
                }
                R.id.nav_reminders -> {
                    false
                }

            }

            true
        }

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

        FireStoreClass().getUserDetails(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun displayUser(username: String?, email:String?)
    {
        usernameTextView.text = username
        emailTextView.text = email
    }
}