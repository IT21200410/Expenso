package com.example.expenso.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.expenso.database.entities.Todo

@Dao
interface ExpenDao {

    @Insert
    suspend fun insertTodo(todo:Todo)

    @Delete
    suspend fun deleteTodo(todo :Todo)

    @Query("Select * from Todo")
    fun getAllTodos():List<Todo>


}