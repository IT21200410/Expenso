package com.example.expenso

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.Reminder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.*

class EditReminder : BaseActivity() {

    private lateinit var addReminderBtn: Button
    private lateinit var labelInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reminder)

        addReminderBtn = findViewById(R.id.btn_add)
        labelInput = findViewById(R.id.labelInput)
        dateInput = findViewById(R.id.DateInput)
        amountInput = findViewById(R.id.amountInput)

        val reminder = intent.getParcelableExtra<Reminder>("reminder")

        if (reminder != null) {
            id = reminder.id
            labelInput.setText(reminder.date)
            dateInput.setText(reminder.type)
            amountInput.setText(reminder.amount.toString())
        }

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


        addReminderBtn.setOnClickListener {
            if (validateReminderDetails()) {
                val intent = Intent(this, ReminderList::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        dateInput.setText(sdf.format(myCalendar.time))
    }

    private fun validateReminderDetails(): Boolean {
        return when {
            TextUtils.isEmpty(labelInput.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_type), true)
                false
            }
            TextUtils.isEmpty(dateInput.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_date), true)
                false
            }
            TextUtils.isEmpty(amountInput.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_amount), true)
                false
            }
            else -> {
                val reminder = Reminder(
                    id,
                    labelInput.text.toString().trim { it <= ' ' },
                    dateInput.text.toString().trim { it <= ' ' },
                    amountInput.text.toString().trim { it <= ' ' }.toDouble()
                )

                FireStoreClass().updateReminder(this@EditReminder, reminder)
                val intent = Intent(this@EditReminder, ReminderList::class.java)
                intent.putExtra("reminder", reminder)
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
        }
    }

    fun updateSuccess() {
        Toast.makeText(this, "Reminder edited", Toast.LENGTH_SHORT).show()
    }

    fun updateFail() {
        Toast.makeText(this, "Reminder Edit failed", Toast.LENGTH_SHORT).show()
    }
}
