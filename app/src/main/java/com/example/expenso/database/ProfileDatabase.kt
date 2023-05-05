
package com.example.expenso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expenso.database.daos.ProfileDao
import com.example.expenso.database.entities.Pro


@Database(entities = [Pro::class] , version=1)

abstract class ProfileDatabase:RoomDatabase() {
    abstract fun getProDao():ProfileDao

    companion object{

        @Volatile
        private var INSTANCE:ProfileDatabase? = null

        fun getInstance(context:Context  ):ProfileDatabase{
            synchronized(this){
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    ProfileDatabase::class.java,
                    "pro_db"
                ).allowMainThreadQueries().build().also { INSTANCE = it }
            }
        }

    }

}