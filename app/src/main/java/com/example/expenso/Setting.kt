package com.example.expenso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.expenso.firestore.FireStoreClass
import com.google.android.material.navigation.NavigationView

class Setting : AppCompatActivity() {
    private lateinit var btnExpense: Button
    private lateinit var btnAboutus:Button
    private lateinit var btnEditProfile: Button
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var settingUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        btnExpense = findViewById(R.id.btnexpen)
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_background))
        btnAboutus = findViewById(R.id.aboutus)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        usernameTextView = headerView.findViewById(R.id.user_name)
        emailTextView = headerView.findViewById(R.id.email)
        settingUserName = findViewById(R.id.tvSettingName)
        btnEditProfile = findViewById(R.id.btEditProfile)

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
                    false
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, chat::class.java))
                    finish()
                }
                R.id.nav_reminders -> {
                    startActivity(Intent(this, add_reminder::class.java))
                    finish()
                }

            }

            true
        }


        btnExpense.setOnClickListener{
            startActivity(Intent(this, addExpenses::class.java))
        }
        btnAboutus.setOnClickListener{
            startActivity(Intent(this, About_us::class.java))
        }
        btnEditProfile.setOnClickListener{
            startActivity(Intent(this, UpdateProfile::class.java))
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

    fun displayUser(firstName: String?, lastName: String?, email:String?)
    {
        usernameTextView.text = firstName
        emailTextView.text = email
        settingUserName.text = "${firstName} ${lastName}"
    }




}