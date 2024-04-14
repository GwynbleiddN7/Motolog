package com.example.motolog.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.motolog.Database.MotorcycleDatabase
import com.example.motolog.Models.Motorcycle
import com.example.motolog.Repository.MotorcycleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MotorcycleViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Motorcycle>>
    private val repository: MotorcycleRepository
    init {
        val motorcycleDao = MotorcycleDatabase.getDatabase(application).motorcycleDao()
        repository = MotorcycleRepository(motorcycleDao)
        readAllData = repository.readAllData
    }

    fun addMotorcycle(motorcycle: Motorcycle){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMotorcycle(motorcycle)
        }
    }

    fun updateMotorcycle(motorcycle: Motorcycle){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMotorcycle(motorcycle)
        }
    }

    fun deleteMotorcycle(motorcycle: Motorcycle){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteMotorcycle(motorcycle)
        }
    }

    fun getMotorcycle(id: Int): LiveData<List<Motorcycle>>{
        return repository.getMotorcycle(id)
    }

    companion object{
        var currentBikeId: Int? = null
    }
}