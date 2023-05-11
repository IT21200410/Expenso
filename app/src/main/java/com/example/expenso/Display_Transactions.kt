package com.example.expenso

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.adapters.TransactionAdapter
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.utils.Constants
import com.google.firebase.firestore.*
import com.example.expenso.models.Transaction
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Display_Transactions : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionList: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var addBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_transactions)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        transactionList = ArrayList()
        transactionAdapter = TransactionAdapter(this,transactionList)
        recyclerView.adapter = transactionAdapter

        addBtn = findViewById(R.id.flbutton)

        addBtn.setOnClickListener{
            val intent  = Intent(this@Display_Transactions, AddExpense::class.java)
            startActivity(intent)
            finish()
        }

        EventChangeListener()

    }

    private fun EventChangeListener()
    {

        val mFireStore = FirebaseFirestore.getInstance()

        mFireStore.collection(Constants.USERTRANSACTIONS)
            .document(FireStoreClass().getCurrentUserID()).collection(Constants.TRANSACTIONS)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null)
                    {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for( mFireStore: DocumentChange in value?.documentChanges!!)
                    {
                        if(mFireStore.type == DocumentChange.Type.ADDED)
                        {
                            transactionList.add(mFireStore.document.toObject(Transaction::class.java))
                            Log.i("",mFireStore.document.id)

                        }
                    }
                    transactionAdapter.notifyDataSetChanged()

                }

            })

    }



}