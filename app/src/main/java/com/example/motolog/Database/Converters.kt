package com.example.motolog.Database

import android.net.Uri
import androidx.room.TypeConverter
import com.example.motolog.Models.ActionLog
import com.example.motolog.Models.DistanceLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
    fun getStringFromURI(uri: Uri?): String? {
        if (uri == null) return null
        return uri.toString()
    }

    @TypeConverter
    fun getURIFromString(string: String?): Uri? {
        if (string == null) return null
        return Uri.parse(string)
    }
}