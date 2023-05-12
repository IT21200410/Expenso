package com.example.expenso

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.adapters.ReminderAdapter
import com.example.expenso.adapters.TransactionAdapter
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.Reminder
import com.example.expenso.models.Transaction
import com.example.expenso.utils.Constants
import com.google.firebase.firestore.*

class ReminderList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reminderList: ArrayList<Reminder>
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var addButton: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        addButton = findViewById(R.id.btn_add)
        reminderList = ArrayList()

        reminderAdapter = ReminderAdapter(this,reminderList)
        recyclerView.adapter = reminderAdapter

        EventChangeListener()

        addButton.setOnClickListener(){
            startActivity(Intent(this,add_reminder::class.java))
        }


    }

    private fun EventChangeListener()
    {

        val fireStore = FirebaseFirestore.getInstance()

        fireStore.collection(Constants.USERREMINDERS)
            .document(FireStoreClass().getCurrentUserID()).collection(Constants.REMINDER)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null)
                    {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for( fireStore: DocumentChange in value?.documentChanges!!)
                    {
                        if(fireStore.type == DocumentChange.Type.ADDED)
                        {
                            reminderList.add(fireStore.document.toObject(Reminder::class.java))
                            Log.v("list : ",reminderList[0].date)

                        }
                    }
                    reminderAdapter.notifyDataSetChanged()

                }

            })

    }
}