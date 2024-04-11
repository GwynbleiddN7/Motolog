package com.example.motolog.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.motolog.Database.GearDatabase
import com.example.motolog.Models.Gear
import com.example.motolog.Repository.GearRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GearViewModel (application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Gear>>
    private val repository: GearRepository
    init {
        val gearDao = GearDatabase.getDatabase(application).gearDao()
        repository = GearRepository(gearDao)
        readAllData = repository.readAllData
    }

    fun addGear(gear: Gear){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGear(gear)
        }
    }

    fun updateGear(gear: Gear){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGear(gear)
        }
    }

    fun deleteGear(gear: Gear){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteGear(gear)
        }
    }
}