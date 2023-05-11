package com.example.expenso.firestore

import android.widget.Toast
import com.example.expenso.AddExpense
import com.example.expenso.models.Transaction
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun addTransaction(activity: AddExpense, transaction: Transaction)
    {
        val transactionData = mFireStore.collection("userTransactions")
            .document("1").collection("transactions")

        transactionData.add(transaction)
            .addOnSuccessListener {
                activity.transactionSuccess()
            }
            .addOnFailureListener{
                activity.transactionFail()
            }

    }
}
