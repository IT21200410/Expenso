package com.example.expenso

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract.Reminders
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.expenso.firestore.FireStoreClass
import com.google.android.material.navigation.NavigationView
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var tvBalance: TextView
    private lateinit var tvIncome: TextView
    private lateinit var tvExpense: TextView
    private lateinit var navigationView: NavigationView
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_background))

        val drawerLayout:DrawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        usernameTextView = headerView.findViewById(R.id.user_name)
        emailTextView = headerView.findViewById(R.id.email)
        tvBalance = findViewById(R.id.tvBalance)
        tvIncome = findViewById(R.id.tvIncome)
        tvExpense = findViewById(R.id.tvExpense)


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_dashboard -> {
                    false
                }
                R.id.nav_transactions -> {
                    startActivity(Intent(this, Display_Transactions::class.java))
                    finish()
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, Setting::class.java))
                    finish()
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, chat::class.java))
                    finish()
                }
                R.id.nav_reminders -> {
                    startActivity(Intent(this, Reminders::class.java))
                    finish()
                }

            }

            true
        }

        FireStoreClass().getDashboardStatistics(this)
        FireStoreClass().getUserDetails(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun updateDashboard(tIncome:Double,tExpense:Double, tBalance:Double)
    {
        tvBalance.text = tBalance.toString()
        tvIncome.text = tIncome.toString()
        tvExpense.text = tExpense.toString()
    }

    fun displayUser(username: String?, email:String?)
    {
        usernameTextView.text = username
        emailTextView.text = email
    }


}

