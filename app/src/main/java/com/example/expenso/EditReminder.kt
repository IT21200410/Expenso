package com.example.expenso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.expenso.models.Reminder


class EditReminder : AppCompatActivity() {

    private lateinit var addReminderBtn : Button
    private lateinit var labelInput : EditText
    private lateinit var dateInput : EditText
    private lateinit var amountInput : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reminder)


        addReminderBtn  = findViewById(R.id.btn_add)
        labelInput  = findViewById(R.id.labelInput)
        dateInput  = findViewById(R.id.DateInput)
        amountInput  = findViewById(R.id.amountInput)

//        val transaction = intent.getParcelableExtra<Reminder>("reminder")

    }
}