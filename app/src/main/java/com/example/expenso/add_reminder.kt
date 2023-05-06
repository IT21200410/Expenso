package com.example.expenso

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class add_reminder : AppCompatActivity() {

    private lateinit var addReminderBtn : Button
    private lateinit var labelInput :EditText
    private lateinit var dateInput : EditText
    private lateinit var amountInput : EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

         addReminderBtn  = findViewById(R.id.btn_add)
         labelInput  = findViewById(R.id.labelInput)
         dateInput  = findViewById(R.id.DateInput)
         amountInput  = findViewById(R.id.amountInput)

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        dateInput.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        addReminderBtn.setOnClickListener(){
            val label:String  = labelInput.toString()
            val amount  = amountInput.toString().toDouble()

            if(label.isEmpty()){
                Toast.makeText(applicationContext, "Enter a valid type", Toast.LENGTH_SHORT).show()
            }
            if(amount == null)
                Toast.makeText(applicationContext, "Enter a valid amount", Toast.LENGTH_SHORT).show()

        }
    }

    fun reminderAddSuccess(){
        Toast.makeText(this, "Transaction added", Toast.LENGTH_SHORT).show()
    }

    fun reminderAddFail(){
        Toast.makeText(this, "Transaction was not added", Toast.LENGTH_SHORT).show()
    }

    private fun updateLabel(myCalendar:Calendar)
    {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dateInput.setText(sdf.format(myCalendar.time))

    }
}