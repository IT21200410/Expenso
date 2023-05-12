package com.example.expenso

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.Transaction
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.*

class AddExpense : BaseActivity() {

    private lateinit var date: EditText
    private lateinit var submitBtn: Button
    private lateinit var eType:MaterialAutoCompleteTextView
    private lateinit var note:EditText
    private lateinit var amount:EditText
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_background))

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.nav_settings -> {
                    startActivity(Intent(this, chat::class.java))
                    finish()
                }
                R.id.nav_chat -> Toast.makeText(applicationContext, "Clicked Chat", Toast.LENGTH_SHORT).show()
                R.id.nav_transactions ->  {
                    startActivity(Intent(this, Display_Transactions::class.java))
                    finish()
                }

            }

            true
        }


        date = findViewById(R.id.DateInput)
        submitBtn = findViewById(R.id.btn_add)
        eType = findViewById(R.id.typeInput)
        note = findViewById(R.id.noteInput)
        amount = findViewById(R.id.amountInput)
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
            validateExpenseDetails()
            startActivity(Intent(this@AddExpense, Display_Transactions::class.java))
            finish()
        }


    }
    private fun updateLabel(myCalendar:Calendar)
    {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        date.setText(sdf.format(myCalendar.time))

    }

    fun transactionSuccess(){
        Toast.makeText(this, "Transaction added", Toast.LENGTH_SHORT).show()
    }


    fun transactionFail(){
        Toast.makeText(this, "Transaction was not added", Toast.LENGTH_SHORT).show()
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
                    "",
                    date.text.toString().trim { it <= ' ' },
                    eType.text.toString().trim { it <= ' ' },
                    amount.text.toString().trim { it <= ' ' }.toDouble(),
                    note.text.toString().trim { it <= ' ' }
                )

                FireStoreClass().addTransaction(this@AddExpense, transaction)
                true
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

//    private fun onRadioButtonClicked(view: View)
//    {
//        var isSelected =
//    }

}