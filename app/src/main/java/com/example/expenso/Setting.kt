package com.example.expenso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Setting : AppCompatActivity() {
    private lateinit var btnExpense: Button
    private lateinit var btnAdd:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        btnAdd = findViewById(R.id.addprof)
        btnExpense = findViewById(R.id.btnexpen)

        btnAdd.setOnClickListener{
            startActivity(Intent(this, add_profile::class.java))
            finish()
        }
        btnExpense.setOnClickListener{
            startActivity(Intent(this, addExpenses::class.java))
            finish()
        }

    }

}