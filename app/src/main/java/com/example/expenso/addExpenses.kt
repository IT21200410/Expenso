package com.example.expenso

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.database.TodoDatabase
import com.example.expenso.database.entities.Todo
import com.example.expenso.database.repositories.ExpensesRepository
import com.example.expenso.adapters.ExpensesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class addExpenses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expenses)

        val recyclerView: RecyclerView = findViewById(R.id.To_do_list)
        val adapter = ExpensesAdapter()
        val repository = ExpensesRepository(TodoDatabase.getInstance(this))

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


    }
