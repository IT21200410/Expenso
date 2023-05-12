package com.example.expenso.models

data class Message(
    val message : String,
    val sentBy : String,
//    val timestamp : String
){
    companion object{
        const val USER = "user"
        const val BOT = "bot"
    }
}