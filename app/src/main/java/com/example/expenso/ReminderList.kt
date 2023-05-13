package com.example.expenso

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.adapters.ReminderAdapter
import com.example.expenso.adapters.TransactionAdapter
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.models.Reminder
import com.example.expenso.models.Transaction
import com.example.expenso.utils.Constants
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class ReminderList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reminderList: ArrayList<Reminder>
    private lateinit var oldReminder: ArrayList<Reminder>
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var  deletedReminder : Reminder
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


        val touchHelper = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteReminder(reminderList[viewHolder.adapterPosition])
            }

        }

        val swipeHelper = ItemTouchHelper(touchHelper)
        swipeHelper.attachToRecyclerView(recyclerView)

    }

    private fun EventChangeListener()
    {

        val fireStore = FirebaseFirestore.getInstance()


        fireStore.collection(Constants.USERREMINDERS)
            .document(FireStoreClass().getCurrentUserID())
            .collection(Constants.REMINDER)
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null)
                    {
                        Log.e(TAG,"onEvent", error)
                        return
                    }
                    if ( value != null && !value.isEmpty)
                    {
                        reminderList.clear()
                        for(document in value.documents) {
                            val data = document.toObject<Reminder>()
                            reminderList.add(data!!)
                        }

                        reminderAdapter.notifyDataSetChanged()
                    }
                    else
                    {
                        Log.e(TAG, "onEvent: query snapshot was null")
                    }
                }

            })

//        fireStore.collection(Constants.USERREMINDERS)
//            .document(FireStoreClass().getCurrentUserID()).collection(Constants.REMINDER)
//            .get()
//            .addOnSuccessListener {
//                if (!it.isEmpty) {
//                    reminderList.clear() // clear the list before adding updated reminders
//                    for (data in it.documents) {
//                        val reminder: Reminder? = data.toObject<Reminder>(Reminder::class.java)
//                        reminderList.add(reminder!!)
//                    }
//                    recyclerView.adapter = ReminderAdapter(this, reminderList)
//                    reminderAdapter.notifyDataSetChanged() // notify adapter for data change
//                }
//            }
//            .addOnFailureListener{
//                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
//            }
    }

    private fun deleteReminder(reminder: Reminder)
    {
        deletedReminder = reminder
        oldReminder = reminderList

        FireStoreClass().deleteReminder(this, deletedReminder)
        val index = reminderList.indexOf(reminder)
        reminderList.removeAt(index)
        reminderAdapter.notifyItemRemoved(index)
//        EventChangeListener()
    }

    fun deleteSuccess(){
        Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show()
    }

    fun deleteFail(){
        Toast.makeText(this, "Can't delete Reminder", Toast.LENGTH_SHORT).show()
    }



    }
