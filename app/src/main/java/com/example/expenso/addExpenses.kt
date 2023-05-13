package com.example.expenso

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.adapters.ExpensesAdapter
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.ExpensesType
import com.example.expenso.models.Transaction
import com.example.expenso.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class addExpenses :BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var expensesList: ArrayList<ExpensesType>
    private lateinit var expensesAdapter: ExpensesAdapter
    private lateinit var toggle: ActionBarDrawerToggle
    private val mFireStore = FirebaseFirestore.getInstance()

//    //update

    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expenses)

        recyclerView = findViewById(R.id.Recycleviewlist)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        //update




        //background
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_background))

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                R.id.nav_transactions -> {
                    startActivity(Intent(this, Display_Transactions::class.java))
                    finish()
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, Setting::class.java))
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, chat::class.java))
                    finish()
                }
                R.id.nav_reminders -> {
                    startActivity(Intent(this, add_reminder::class.java))
                    finish()
                }

            }

            true
        }

        expensesList = ArrayList()
        expensesAdapter = ExpensesAdapter(this, expensesList)
        recyclerView.adapter = expensesAdapter


        val addbtn = findViewById<Button>(R.id.Btn_addItem)
        addbtn.setOnClickListener({

            displayDialog(expensesAdapter)

        })
        EventChangeListener()
    }


    fun displayDialog(adapter: ExpensesAdapter) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter New Expenses")
        builder.setMessage("Enter the Expenses")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->

            val item = input.text.toString()

            val expensesType = ExpensesType(
                "",
                item.trim { it <= ' ' },

                )

            FireStoreClass().addExpensesType(this@addExpenses, expensesType)


        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun updateDialog(updateType: ExpensesType){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Expense Type")


        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(updateType.expensesName)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->

            val item = input.text.toString()

            val expensesType = ExpensesType(
                updateType.id,
                item.trim { it <= ' ' },

                )

             FireStoreClass().updateExpensesType(this@addExpenses, expensesType)


        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun EventChangeListener() {

        mFireStore.collection(Constants.EXPENSESTYPE)
            .document(FireStoreClass().getCurrentUserID())
            .collection(Constants.EXPENSESL)
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null)
                    {
                        Log.e(TAG,"onEvent", error)
                        return
                    }
                    if ( value != null && !value.isEmpty)
                    {
                        expensesList.clear()
                        for(document in value.documents) {
                            val data = document.toObject<ExpensesType>()
                            expensesList.add(data!!)
                        }

                        expensesAdapter.notifyDataSetChanged()
                    }
                    else
                    {
                        Log.e(TAG, "onEvent: query snapshot was null")
                    }
                }

            })

    }






    fun expensesSuccess() {
        Toast.makeText(this, "Expense Type added", Toast.LENGTH_SHORT).show()
    }


    fun expensesFail() {
        Toast.makeText(this, "Expense Type was not added", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem)
            : Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
//update

    fun updateSuccess() {
        Toast.makeText(this, "Expenses Type edited", Toast.LENGTH_SHORT).show()
    }

    fun updateFail() {
        Toast.makeText(this, "Couldn't edit expenses Type", Toast.LENGTH_SHORT).show()
    }
    fun deleteExpenseType(deleteType:ExpensesType){
        FireStoreClass().deleteExpensesType(this,deleteType)
    }

}

