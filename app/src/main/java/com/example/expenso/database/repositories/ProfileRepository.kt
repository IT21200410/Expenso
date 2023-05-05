package com.example.expenso.database.repositories

import com.example.expenso.database.entities.Pro
import com.example.expenso.database.ProfileDatabase

class ProfileRepository(private val db:ProfileDatabase) {
    suspend fun insert(pro: Pro) = db.getProDao().insertPro(pro)
    suspend fun delete(pro : Pro)= db.getProDao().deletePro(pro)
    fun getAllPros() = db.getProDao().getAllPros()

}
