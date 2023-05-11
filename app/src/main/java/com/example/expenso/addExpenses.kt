package com.example.expenso

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.text.InputType
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.database.TodoDatabase
import com.example.expenso.database.entities.Todo
import com.example.expenso.database.repositories.ExpensesRepository
import com.example.expenso.adapters.ExpensesAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class addExpenses : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expenses)

        val recyclerView: RecyclerView = findViewById(R.id.To_do_list)
        val adapter = ExpensesAdapter()
        val repository = ExpensesRepository(TodoDatabase.getInstance(this))
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient_background))

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_dashboard -> {
                    false
                }
                R.id.nav_transactions -> {
                    startActivity(Intent(this, Display_Transactions::class.java))
                    finish()
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, Setting::class.java))
                    finish()
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, chat::class.java))
                }
                R.id.nav_reminders -> {
                    startActivity(Intent(this, CalendarContract.Reminders::class.java))
                    finish()
                }

            }

            true
        }

        CoroutineScope(Dispatchers.IO).launch {

            val data = repository.getAllTodos()
            adapter.setData(data,this@addExpenses)
        }


        recyclerView.adapter = adapter
        recyclerView.layoutManager= LinearLayoutManager(this)

        val btnAddTodo = findViewById<Button>(R.id.Btn_addItem)
        btnAddTodo.setOnClickListener({

            displayDialog(repository,adapter)

        })

    }

    fun displayDialog(repository: ExpensesRepository, adapter: ExpensesAdapter){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter New Expenses")
        builder.setMessage("Enter the Expenses")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK"){
                dialog,which->

            val item = input.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(Todo(item))


                val data = repository.getAllTodos()
                runOnUiThread{
                    adapter.setData(data,this@addExpenses)
                }

            }

        }

        builder.setNegativeButton("Cancel"){
                dialog,which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    }
