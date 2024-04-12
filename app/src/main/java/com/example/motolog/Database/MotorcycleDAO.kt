package com.example.motolog.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.motolog.Models.Motorcycle

@Dao
interface MotorcycleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMotorcycle(motorcycle: Motorcycle)

    @Update
    suspend fun updateMotorcycle(motorcycle: Motorcycle)

    @Delete
    suspend fun deleteMotorcycle(motorcycle: Motorcycle)

    @Query("SELECT * FROM motorcycle ORDER BY id ASC")
    fun readAllData(): LiveData<List<Motorcycle>>

    @Query("SELECT * FROM motorcycle WHERE id = :id")
    fun getMotorcycle(id: Int): LiveData<List<Motorcycle>>
}