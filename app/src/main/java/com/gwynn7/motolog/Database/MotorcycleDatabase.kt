package com.gwynn7.motolog.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gwynn7.motolog.Models.Motorcycle


@Database(entities = [Motorcycle::class], version = 14, exportSchema = true)
@TypeConverters(Converters::class)
abstract class MotorcycleDatabase : RoomDatabase() {
    abstract fun motorcycleDao(): MotorcycleDAO

    companion object{
        @Volatile
        private var INSTANCE: MotorcycleDatabase? = null

        fun getDatabase(context: Context): MotorcycleDatabase {
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
                instance.addMigrations(MigrationFrom13To14())
                instance.fallbackToDestructiveMigration()
                val builtInstance = instance.build()
                INSTANCE = builtInstance
                return builtInstance
            }
        }
    }
}
class MigrationFrom13To14 : Migration(13, 14) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE motorcycle ADD COLUMN listImage TYPE TEXT");
    }
}
