package com.example.motolog.Repository

import androidx.lifecycle.LiveData
import com.example.motolog.Database.MotorcycleDAO
import com.example.motolog.Models.Motorcycle

class MotorcycleRepository(private val motorcycleDao: MotorcycleDAO) {
    val readAllData: LiveData<List<Motorcycle>> = motorcycleDao.readAllData()

    suspend fun addMotorcycle(motorcycle: Motorcycle){
        motorcycleDao.addMotorcycle(motorcycle);
    }

    suspend fun updateMotorcycle(motorcycle: Motorcycle){
        motorcycleDao.updateMotorcycle(motorcycle);
    }

    suspend fun deleteMotorcycle(motorcycle: Motorcycle){
        motorcycleDao.deleteMotorcycle(motorcycle)
    }

    fun getMotorcycle(id: Int): LiveData<List<Motorcycle>>{
        return motorcycleDao.getMotorcycle(id)
    }
}