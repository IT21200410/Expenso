package com.example.expenso

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import com.example.expenso.models.Transaction
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.*

class EditTransaction : AppCompatActivity() {
    private lateinit var date: EditText
    private lateinit var submitBtn: Button
    private lateinit var eType: MaterialAutoCompleteTextView
    private lateinit var note: EditText
    private lateinit var amount: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        date = findViewById(R.id.DateInput)
        submitBtn = findViewById(R.id.btn_add)
        eType = findViewById(R.id.typeInput)
        note = findViewById(R.id.noteInput)
        amount = findViewById(R.id.amountInput)

        val transaction = intent.getParcelableExtra<Transaction>("transaction")

        if ( transaction != null )
        {
            date.setText(transaction.date)
            eType.setText(transaction.expenseType)
            note.setText(transaction.note)
            amount.setText(transaction.amount.toString())
        }

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }
        val items = listOf(
            "Shopping",
            "Gym",
            "Fuel",
            "Food"
        )

        date.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        eType.setAdapter(adapter)
    }

    private fun updateLabel(myCalendar:Calendar)
    {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        date.setText(sdf.format(myCalendar.time))

    }
}