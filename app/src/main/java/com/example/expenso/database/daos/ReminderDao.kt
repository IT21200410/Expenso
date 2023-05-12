package com.example.expenso.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.expenso.database.entities.Todo

@Dao
interface ReminderDao {

    @Insert
    suspend fun addReminder(reminder:ReminderDao)

    @Delete
    suspend fun deleteReminder(reminder :ReminderDao)

    @Query("Select * from Reminder")
    fun getAllTodos():List<Todo>


}