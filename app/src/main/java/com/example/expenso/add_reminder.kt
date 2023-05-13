package com.example.expenso

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.Reminder
import java.text.SimpleDateFormat
import java.util.*

class add_reminder : BaseActivity() {

    private lateinit var addReminderBtn : Button
    private lateinit var labelInput :EditText
    private lateinit var dateInput : EditText
    private lateinit var amountInput : EditText

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
            validateReminderDetails()
        }
    }

    private fun validateReminderDetails():Boolean {
        return when {
            TextUtils.isEmpty(dateInput.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_date), true)
                false

            }
            TextUtils.isEmpty(labelInput.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_type), true)
                false
            }
            TextUtils.isEmpty(amountInput.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_amount), true)
                false
            }
            else -> {

                val reminder = Reminder(
                    "",

                    labelInput.text.toString().trim { it <= ' ' },
                    dateInput.text.toString().trim { it <= ' ' },
                    amountInput.text.toString().trim { it <= ' ' }.toDouble(),

                )

               // FireStoreClass().addReminder(this@add_reminder, reminder)
                true
            }

        }
    }

    fun reminderAddSuccess(){
        Toast.makeText(this, "Reminder added", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, ReminderList::class.java))
    }

    fun reminderAddFail(){
        Toast.makeText(this, "Reminder was not added", Toast.LENGTH_SHORT).show()
    }

    private fun updateLabel(myCalendar:Calendar)
    {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dateInput.setText(sdf.format(myCalendar.time))

    }
}