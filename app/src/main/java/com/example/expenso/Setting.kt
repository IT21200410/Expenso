package com.example.expenso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Setting : AppCompatActivity() {
    private lateinit var btnExpense: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        btnExpense = findViewById(R.id.btnexpen)

        btnExpense.setOnClickListener{
            startActivity(Intent(this, addExpenses::class.java))
            finish()
        }

    }

}