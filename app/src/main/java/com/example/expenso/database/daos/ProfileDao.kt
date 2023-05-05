package com.example.expenso.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.expenso.database.entities.Pro

@Dao
interface ProfileDao {

    @Insert
    suspend fun insertPro(pro:Pro)

    @Delete
    suspend fun deletePro(pro :Pro)

    @Query("Select * from Pro")
    fun getAllPros():List<Pro>


}