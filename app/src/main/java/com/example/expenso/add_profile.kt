package com.example.expenso

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.database.ProfileDatabase
import com.example.expenso.database.entities.Pro
import com.example.expenso.database.repositories.ProfileRepository
import com.example.expenso.adapters.profileAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class add_profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile)

        val recyclerView: RecyclerView = findViewById(R.id.Pro_List)
        val adapter = profileAdapter()
        val repository = ProfileRepository(ProfileDatabase.getInstance(this))

        CoroutineScope(Dispatchers.IO).launch {

            val data = repository.getAllPros()
            adapter.setData(data,this@add_profile)
        }


        recyclerView.adapter = adapter
        recyclerView.layoutManager= LinearLayoutManager(this)

        val btnAddPro = findViewById<Button>(R.id.Btn_addpro)
        btnAddPro.setOnClickListener({

            displayDialog(repository,adapter)

        })

    }

    fun displayDialog(repository: ProfileRepository, adapter: profileAdapter){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter New Profile Name")
        builder.setMessage("Enter the Name")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK"){
                dialog,which->

            val item = input.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(Pro(item))


                val data = repository.getAllPros()
                runOnUiThread{
                    adapter.setData(data,this@add_profile)
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
