package com.example.expenso.models

data class Transaction(
    val date:String = "",
    val expenseType:String = "",
    val amount:Double = 0.0,
    val note:String = ""
)
