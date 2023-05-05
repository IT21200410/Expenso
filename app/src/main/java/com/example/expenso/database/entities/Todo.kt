package com.example.expenso.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Todo(var item:String) {

    @PrimaryKey
    var id:Int? = null


}