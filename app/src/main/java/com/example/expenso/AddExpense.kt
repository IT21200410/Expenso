package com.example.expenso

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.ExpensesType
import com.example.expenso.models.Transaction
import com.example.expenso.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddExpense : BaseActivity(), View.OnClickListener{

    private lateinit var date: EditText
    private lateinit var submitBtn: Button
    private lateinit var eType:MaterialAutoCompleteTextView
    private lateinit var note:EditText
    private lateinit var amount:EditText
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var rbLeft: RadioButton
    private lateinit var rbRight: RadioButton
    private lateinit var items:ArrayList<String>
    private val mFireStore = FirebaseFirestore.getInstance()

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
        rbLeft = findViewById(R.id.rbLeft)
        rbRight = findViewById(R.id.rbRight)

        rbLeft.setOnClickListener(this)
        rbRight.setOnClickListener(this)

        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        items = arrayListOf(
            "Shopping",
            "Gym",
            "Fuel",
            "Food"
        )

        getExpenseType()
        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
        eType.setAdapter(adapter)


        date.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


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

                var transactionType:String = ""

                if (rbLeft.isChecked)
                {
                    transactionType = "Expense"
                }
                else
                {
                    transactionType = "Income"
                }

                val transaction = Transaction(
                    "",
                    transactionType,
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

    override fun onClick(view: View?) {
        if ( view is RadioButton)
        {
            if (view.isChecked)
            {
                when {
                    view.id == R.id.rbLeft -> {
                        rbLeft.setTextColor(Color.WHITE)
                        rbRight.setTextColor(ContextCompat.getColor(this, R.color.chat_gradient_start))

                        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
                        eType.setAdapter(adapter)
                    }
                    view.id == R.id.rbRight -> {
                        rbRight.setTextColor(Color.WHITE)
                        rbLeft.setTextColor(ContextCompat.getColor(this, R.color.chat_gradient_start))

                        var items = listOf(
                            "Salary"
                        )

                        val adapter = ArrayAdapter(this, R.layout.dropdown, items)
                        eType.setAdapter(adapter)
                    }
                }
            }
        }
    }

    private fun getExpenseType()
    {
        mFireStore.collection(Constants.EXPENSESTYPE)
                .document(FireStoreClass().getCurrentUserID())
                .collection(Constants.EXPENSESL)
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty)
                {
                    for(data in it.documents){
                        val expenseType:ExpensesType? = data.toObject<ExpensesType>(ExpensesType::class.java)
                        items.add(expenseType?.expensesName!!)
                    }

                }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

    }

}