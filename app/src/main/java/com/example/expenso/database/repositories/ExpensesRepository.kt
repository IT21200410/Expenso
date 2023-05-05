package com.example.expenso.database.repositories

import com.example.expenso.database.entities.Todo
import com.example.expenso.database.TodoDatabase

class ExpensesRepository(private val db:TodoDatabase) {
    suspend fun insert(todo: Todo) = db.getTodoDao().insertTodo(todo)
    suspend fun delete(todo : Todo)= db.getTodoDao().deleteTodo(todo)
    fun getAllTodos() = db.getTodoDao().getAllTodos()

}
