package com.example.expenso.firestore

import android.app.Activity
import android.util.Log
import com.example.expenso.AddExpense
import com.example.expenso.LoginActivity
import com.example.expenso.SignUpActivity
import com.example.expenso.adapters.TransactionAdapter
import com.example.expenso.add_reminder
import com.example.expenso.models.Reminder
import com.example.expenso.models.Transaction
import com.example.expenso.models.User
import com.example.expenso.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity,user: User)
    {
        mFireStore.collection(Constants.USERS)
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
               activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
                activity.hideProgressDialog()
                activity.userRegistrationFail()
            }
    }

    fun getCurrentUserID():String {

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if ( currentUser != null )
        {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity)
    {
         mFireStore.collection(Constants.USERS)
             .document(getCurrentUserID())
             .get()
             .addOnSuccessListener { document ->
                 Log.i(activity.javaClass.simpleName, document.toString())

                 val user = document.toObject(User::class.java)

                 when (activity){
                     is LoginActivity -> {
                         activity.userLoggedInSuccess(user!!)
                     }
                 }

             }
             .addOnFailureListener{e ->

             }
    }

    //Hello

    fun addTransaction(activity: AddExpense, transaction: Transaction)
    {
        val transactionData = mFireStore.collection(Constants.USERTRANSACTIONS)
            .document(getCurrentUserID()).collection(Constants.TRANSACTIONS)

        transactionData.add(transaction)
            .addOnSuccessListener {
                activity.transactionSuccess()
            }
            .addOnFailureListener{
                activity.transactionFail()
            }

    }

    fun addReminder(activity: add_reminder, reminder: Reminder)
    {
        val reminderData = mFireStore.collection(Constants.USERREMINDERS)
            .document(getCurrentUserID()).collection(Constants.REMINDER)

        reminderData.add(reminder)
            .addOnSuccessListener {
                activity.reminderAddSuccess()
            }
            .addOnFailureListener{
                activity.reminderAddFail()
            }

    }


}
