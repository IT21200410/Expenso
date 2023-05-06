package com.example.expenso.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Reminder(var item:String) {

    @PrimaryKey
    var id:Int? = null


}