package com.example.motolog.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.motolog.Models.Motorcycle

@Database(entities = [Motorcycle::class], version = 10, exportSchema = true)
@TypeConverters(Converters::class)
abstract class MotorcycleDatabase : RoomDatabase() {
    abstract fun motorcycleDao(): MotorcycleDAO

    companion object{
        @Volatile
        private var INSTANCE: MotorcycleDatabase? = null

        fun getDatabase(context: Context): MotorcycleDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MotorcycleDatabase::class.java,
                    "motorcycles_db"
                )
                instance.fallbackToDestructiveMigration()
                val built_instance = instance.build()
                INSTANCE = built_instance
                return built_instance
            }
        }
    }
}