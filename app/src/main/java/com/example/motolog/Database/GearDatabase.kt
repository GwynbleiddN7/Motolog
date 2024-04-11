package com.example.motolog.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.motolog.Models.Gear

@Database(entities = [Gear::class], version = 2, exportSchema = true)
abstract class GearDatabase: RoomDatabase() {
    abstract fun gearDao(): GearDAO

    companion object{
        @Volatile
        private var INSTANCE: GearDatabase? = null

        fun getDatabase(context: Context): GearDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GearDatabase::class.java,
                    "gear_db"
                )
                instance.fallbackToDestructiveMigration()
                val built_instance = instance.build()
                INSTANCE = built_instance
                return built_instance
            }
        }
    }
}