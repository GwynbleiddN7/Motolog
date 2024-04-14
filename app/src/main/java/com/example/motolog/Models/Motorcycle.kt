package com.example.motolog.Models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "motorcycle")
data class Motorcycle(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val manufacturer: String,
    val model: String,
    val alias: String,
    val year: Int,
    val start_km: Int,
    var personal_km: Int = 0,
    var km_logs: List<DistanceLog> = listOf(),
    val maintenance_logs: List<ActionLog> = listOf(),
    val mods_logs: List<ActionLog> = listOf(),
    val image: Bitmap? = null,
    @Embedded
    val infos: BikeInfo = BikeInfo(),
): Parcelable


@Parcelize
data class BikeInfo(
    val engine_cc: Double = 0.0,
    val cilinders: Int = 0,
    val horse_power: Double = 0.0,
    val price: Double = 0.0,
    val front_tire: String = "",
    val rear_tire: String = "",
    val plate_number: String = ""
): Parcelable

@Parcelize
data class ActionLog(
    val name: String,
    val description: String,
    val date: Long,
    val price: Double
): Parcelable

@Parcelize
data class DistanceLog(
    val distance: Int,
    val date: Long,
): Parcelable