package com.example.motolog.Database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.motolog.Models.ActionLog
import com.example.motolog.Models.DistanceLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

class Converters {
    @TypeConverter
    fun fromActionLogToString(value: List<ActionLog>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ActionLog>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun fromStringToActionLog(value: String): List<ActionLog> {
        val gson = Gson()
        val type = object : TypeToken<List<ActionLog>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromDistanceLogToString(value: List<DistanceLog>): String {
        val gson = Gson()
        val type = object : TypeToken<List<DistanceLog>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun fromStringToActionDistance(value: String): List<DistanceLog> {
        val gson = Gson()
        val type = object : TypeToken<List<DistanceLog>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        if(bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        if(byteArray == null) return null
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}