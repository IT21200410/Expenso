package com.example.expenso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.adapters.TransactionAdapter
import com.example.expenso.models.Transaction

class Display_Transactions : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionList: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_transactions)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        transactionList = ArrayList()

        transactionList.add(Transaction("04/08/2012","Food", 4500.00, ""))
        transactionList.add(Transaction("04/08/2013","Food", 4500.00, ""))
        transactionList.add(Transaction("04/08/2012","Fuel", 4500.00, ""))

        transactionAdapter = TransactionAdapter(this,transactionList)
        recyclerView.adapter = transactionAdapter


    }
}