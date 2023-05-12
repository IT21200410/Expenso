package com.example.expenso

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenso.adapters.TransactionAdapter
import com.example.expenso.firestore.FireStoreClass
import com.example.expenso.utils.Constants
import com.google.firebase.firestore.*
import com.example.expenso.models.Transaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.ktx.toObject


class Display_Transactions : AppCompatActivity()
{
    // Variables declared for late initalization
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionList: ArrayList<Transaction>
    private lateinit var oldTransactions: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var addBtn: FloatingActionButton
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var deletedTransaction: Transaction
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_transactions)

        // Variables related to recycler view
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.isAutoMeasureEnabled = false
        recyclerView.layoutManager = layoutManager


        ///////////////////////////////////////

        val itemTouchHelper = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactionList[viewHolder.adapterPosition])
            }

        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerView)

        ///////////////////////////////////////

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
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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
                   false
                }

            }

            true
        }

        transactionList = ArrayList()
        transactionAdapter = TransactionAdapter(this,transactionList)
        recyclerView.adapter = transactionAdapter

        addBtn = findViewById(R.id.flbutton)

        addBtn.setOnClickListener{
            val intent  = Intent(this@Display_Transactions, AddExpense::class.java)
            startActivity(intent)
        }

        EventChangeListener()

    }

    private fun EventChangeListener()
    {

      mFireStore.collection(Constants.USERTRANSACTIONS)
          .document(FireStoreClass().getCurrentUserID())
          .collection(Constants.TRANSACTIONS)
          .addSnapshotListener(object: EventListener<QuerySnapshot>{
              override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                  if (error != null)
                  {
                      Log.e(TAG,"onEvent", error)
                      return
                  }
                  if ( value != null && !value.isEmpty)
                  {
                      transactionList.clear()
                      for(document in value.documents) {
                          val data = document.toObject<Transaction>()
                          transactionList.add(data!!)
                      }

                      transactionAdapter.notifyDataSetChanged()
                  }
                  else
                  {
                      Log.e(TAG, "onEvent: query snapshot was null")
                  }
              }

          })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun DeleteSuccess(){
        Toast.makeText(this, "Transaction deleted", Toast.LENGTH_SHORT).show()
    }

    fun DeleteFail(){
        Toast.makeText(this, "Couldn't delete transaction", Toast.LENGTH_SHORT).show()
    }

    private fun deleteTransaction(transaction: Transaction)
    {
        deletedTransaction = transaction
        oldTransactions = transactionList

        FireStoreClass().deleteTransaction(this, deletedTransaction)

    }

}