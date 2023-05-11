package com.example.expenso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Setting : AppCompatActivity() {
    private lateinit var btnExpense: Button
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        btnExpense = findViewById(R.id.btnexpen)
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_background))

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
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
                }
                R.id.nav_reminders -> {
                    startActivity(Intent(this, CalendarContract.Reminders::class.java))
                    finish()
                }

            }

            true
        }


        btnExpense.setOnClickListener{
            startActivity(Intent(this, addExpenses::class.java))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}