package com.example.expenso.firestore

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.example.expenso.*
import com.example.expenso.databinding.ActivityAddReminderBinding
import com.example.expenso.models.Reminder
import com.example.expenso.models.ExpensesType
import com.example.expenso.*
import com.example.expenso.models.User
import com.example.expenso.utils.Constants
import com.example.expenso.models.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await

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
                     is MainActivity -> {
                         activity.displayUser(user?.firstName, user?.email)
                     }
                     is Display_Transactions -> {
                         activity.displayUser(user?.firstName, user?.email)
                     }
                     is Setting -> {
                         activity.displayUser(user?.firstName, user?.lastName, user?.email)
                     }
                     is chat -> {
                         activity.displayUser(user?.firstName, user?.email)
                     }
                     is UpdateProfile -> {
                         activity.displayUser(user?.id, user?.firstName, user?.lastName, user?.email)
                     }


                 }

             }
    }


    fun updateUserDetails(activity: UpdateProfile, user: User)
    {
        val userRef = mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())

        userRef.set(user)
            .addOnSuccessListener {
                activity.updateSuccess()
            }
            .addOnFailureListener{e ->
                activity.updateFail()
            }
    }

    //Hello

    fun addTransaction(activity: AddExpense, transaction: Transaction)
    {
        val transactionData = mFireStore.collection(Constants.USERTRANSACTIONS)
            .document(getCurrentUserID()).collection(Constants.TRANSACTIONS)

        val newTransactionRef = transactionData.document().toString()
        transaction.id = newTransactionRef

        transactionData.add(transaction)
            .addOnSuccessListener {
                activity.transactionSuccess()
            }
            .addOnFailureListener{
                activity.transactionFail()
            }

    }

    fun updateTransaction(activity: EditTransaction, transaction: Transaction)
    {
        val transactionRef = mFireStore.collection(Constants.USERTRANSACTIONS)
            .document(getCurrentUserID()).collection(Constants.TRANSACTIONS)

        transactionRef.whereEqualTo("id", transaction.id)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    val documentRef = transactionRef.document(document.id)
                    documentRef.set(transaction)
                        .addOnSuccessListener {
                           activity.updateSuccess()

                        }
                        .addOnFailureListener{e ->
                            activity.updateFail()
                        }

                }
            }
            .addOnFailureListener{e ->
                Log.w("Fail", "Couldn't edit", e)
            }
    }
    fun addExpensesType(activity: addExpenses, expense: ExpensesType)
    {
        val expensesData = mFireStore.collection(Constants.EXPENSESTYPE)
            .document(getCurrentUserID()).collection(Constants.EXPENSESL)

        val newExpensesRef = expensesData.document().toString()
        expense.id = newExpensesRef

        expensesData.add(expense)
            .addOnSuccessListener {
                activity.expensesSuccess()
            }
            .addOnFailureListener{
                activity.expensesFail()
            }

    }

    fun updateExpensesType(activity: addExpenses, expenses: ExpensesType)
    {
        val expensesRef = mFireStore.collection(Constants.EXPENSESTYPE)
            .document(getCurrentUserID()).collection(Constants.EXPENSESL)

        expensesRef.whereEqualTo("id", expenses.id)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    val documentRef = expensesRef.document(document.id)
                    documentRef.set(expenses)
                        .addOnSuccessListener {
                            activity.updateSuccess()
                        }
                        .addOnFailureListener{e ->
                            activity.updateFail()
                        }

                }
            }
            .addOnFailureListener{e ->
                Log.w("Fail", "Couldn't edit", e)
            }
    }

    fun deleteTransaction(activity: Display_Transactions, transaction: Transaction)
    {
        val transactionRef = mFireStore.collection(Constants.USERTRANSACTIONS)
            .document(getCurrentUserID()).collection(Constants.TRANSACTIONS)

        transactionRef.whereEqualTo("id", transaction.id)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    val documentRef = transactionRef.document(document.id).delete()
                        .addOnSuccessListener {
                            activity.DeleteSuccess()
                        }
                        .addOnFailureListener{e ->
                            activity.DeleteFail()
                        }

                }
            }
            .addOnFailureListener{e ->
                Log.w("Fail", "Couldn't edit", e)
            }
    }

    fun deleteExpensesType(activity: addExpenses, expenses: ExpensesType)
    {
        val expensesRef = mFireStore.collection(Constants.EXPENSESTYPE)
            .document(getCurrentUserID()).collection(Constants.EXPENSESL)

        expensesRef.whereEqualTo("id", expenses.id)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    val documentRef = expensesRef.document(document.id).delete()
                        .addOnSuccessListener {
                            activity.updateSuccess()
                        }
                        .addOnFailureListener{e ->
                            activity.updateFail()
                        }

                }
            }
            .addOnFailureListener{e ->
                Log.w("Fail", "Couldn't edit", e)
            }
    }

    fun addReminder(activity: add_reminder, reminder: Reminder )
    {
        val reminderData = mFireStore.collection(Constants.USERREMINDERS)
            .document(getCurrentUserID()).collection(Constants.REMINDER)

        val newTransactionRef = reminderData.document().toString()
        reminder.id = newTransactionRef

        reminderData.add(reminder)
            .addOnSuccessListener {
                activity.reminderAddSuccess()
            }
            .addOnFailureListener{
                activity.reminderAddFail()
            }

    }

    fun updateReminder(activity: EditReminder, reminder: Reminder)
    {
        val reminderObj = mFireStore.collection(Constants.USERREMINDERS)
            .document(getCurrentUserID()).collection(Constants.REMINDER)

        reminderObj.whereEqualTo("id", reminder.id)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    val documentRef = reminderObj.document(document.id)
                    documentRef.set(reminder)
                        .addOnSuccessListener {
                            activity.updateSuccess()
                        }
                        .addOnFailureListener{e ->
                            activity.updateFail()
                        }

                }
            }
            .addOnFailureListener{e ->
                Log.w("Fail", "Couldn't edit", e)
            }
    }

    fun deleteReminder(activity: ReminderList, reminder: Reminder)
    {
        val reminderRef = mFireStore.collection(Constants.USERREMINDERS)
            .document(getCurrentUserID()).collection(Constants.REMINDER)

        reminderRef.whereEqualTo("id", reminder.id)
            .get()
            .addOnSuccessListener { documents ->
                for(document in documents){
                    val documentRef = reminderRef.document(document.id).delete()
                        .addOnSuccessListener {
                            activity.deleteSuccess()
                        }
                        .addOnFailureListener{e ->
                            activity.deleteFail()
                        }

                }
            }
            .addOnFailureListener{e ->
                Log.w("Fail", "Couldn't edit", e)
            }
    }

    fun getDashboardStatistics(activity: MainActivity)
    {
        var totalExpense = 0.0
        var totalIncome = 0.0
        var totalBalance = 0.0
        var amount = 0.0

        mFireStore.collection(Constants.USERTRANSACTIONS)
            .document(getCurrentUserID()).collection(Constants.TRANSACTIONS)
            .get()
            .addOnSuccessListener { documents ->

                for(document in documents){

                    amount = document.getDouble("amount")!!

                    if ( document.getString("transactionType") == "Income")
                    {
                        totalIncome += amount
                    }
                    else
                    {
                        totalExpense += amount
                    }

                }

                totalBalance = totalIncome - totalExpense
                activity.updateDashboard(totalIncome, totalExpense, totalBalance)

            }
            .addOnFailureListener{e ->

            }
    }



}
