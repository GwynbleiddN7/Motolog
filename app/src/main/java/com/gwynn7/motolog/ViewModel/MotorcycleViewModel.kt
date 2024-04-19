package com.gwynn7.motolog.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gwynn7.motolog.Database.MotorcycleDatabase
import com.gwynn7.motolog.Models.Motorcycle
import com.gwynn7.motolog.Repository.MotorcycleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

class MotorcycleViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<Motorcycle>>
    private val repository: MotorcycleRepository
    private var imageDirectory: File?
    init {
        val motorcycleDao = MotorcycleDatabase.getDatabase(application).motorcycleDao()
        imageDirectory = application.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        repository = MotorcycleRepository(motorcycleDao)
        readAllData = repository.readAllData
    }

    fun addMotorcycle(motorcycle: Motorcycle, bitmap: Bitmap?){
        if(bitmap != null) motorcycle.image = saveImage(bitmap)
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMotorcycle(motorcycle)
        }
    }

    fun updateMotorcycle(motorcycle: Motorcycle, bitmap: Bitmap?, removeImageOnNull: Boolean = false){
        if(bitmap != null){
            deleteImage(motorcycle)
            motorcycle.image = saveImage(bitmap)
        }
        else if(removeImageOnNull) {
            deleteImage(motorcycle)
            motorcycle.image = null
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMotorcycle(motorcycle)
        }
    }

    fun deleteMotorcycle(motorcycle: Motorcycle){
        deleteImage(motorcycle)
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteMotorcycle(motorcycle)
        }
    }

    fun getMotorcycle(id: Int): LiveData<List<Motorcycle>>{
        return repository.getMotorcycle(id)
    }

    private fun saveImage(bitmap: Bitmap): Uri {
        val filePath = File(imageDirectory, String.format("%S.jpg", UUID.randomUUID().toString()))
        val outputStream: OutputStream = FileOutputStream(filePath)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        outputStream.flush()
        outputStream.close()
        return filePath.toUri()
    }

    private fun deleteImage(motorcycle: Motorcycle){
        val oldImage = motorcycle.image
        if(oldImage != null){
            val oldFile = File(oldImage.path!!)
            if(oldFile.exists()) oldFile.delete()
        }
    }

    companion object{
        var currentBikeId: Int? = null
    }
}