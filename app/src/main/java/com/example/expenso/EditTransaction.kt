package com.example.expenso

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.Transaction
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.*

class EditTransaction : BaseActivity() {
    private lateinit var date: EditText
    private lateinit var submitBtn: Button
    private lateinit var eType: MaterialAutoCompleteTextView
    private lateinit var note: EditText
    private lateinit var amount: EditText
    private lateinit var id:String

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
            id = transaction.id
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

        submitBtn.setOnClickListener {
            if(validateExpenseDetails())
            {
                val intent = Intent(this, Display_Transactions::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    private fun updateLabel(myCalendar:Calendar)
    {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        date.setText(sdf.format(myCalendar.time))

    }

    fun updateSuccess(){
        Toast.makeText(this, "Transaction edited", Toast.LENGTH_SHORT).show()
    }

    fun updateFail(){
        Toast.makeText(this, "Couldn't edit transaction", Toast.LENGTH_SHORT).show()
    }

    private fun validateExpenseDetails():Boolean {
        return when {
            TextUtils.isEmpty(date.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_date), true)
                false

            }
            TextUtils.isEmpty(eType.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_type), true)
                false
            }
            TextUtils.isEmpty(amount.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_amount), true)
                false
            }
            else -> {

                val transaction = Transaction(
                    id,
                    date.text.toString().trim { it <= ' ' },
                    eType.text.toString().trim { it <= ' ' },
                    amount.text.toString().trim { it <= ' ' }.toDouble(),
                    note.text.toString().trim { it <= ' ' }
                )

                FireStoreClass().updateTransaction(this@EditTransaction, transaction)
                true
            }

        }
    }
}