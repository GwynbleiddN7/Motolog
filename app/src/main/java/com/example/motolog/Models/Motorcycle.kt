package com.example.motolog.Models

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
    val price: Double,
    val total_km: Double = 0.0,
    val km_logs: String = "",
    val maintenance_logs: String = "",
    val mods_logs: String = "",
    val image: String = "",
    @Embedded
    val infos: BikeInfo = BikeInfo(),

): Parcelable


@Parcelize
data class BikeInfo(
    val engine_cc: Double = 0.0,
    val cilinders: Int = 0,
    val horse_power: Double = 0.0,
    val front_tire: String = "",
    val rear_tire: String = "",
    val plate_number: String = ""
): Parcelable