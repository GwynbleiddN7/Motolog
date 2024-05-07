package com.gwynn7.motolog.Models

import android.net.Uri
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
    var image: Uri? = null,
    @Embedded
    var logs: Logs = Logs(),
    @Embedded
    val info: Info = Info(),
    @Embedded
    val expiry: Expiry = Expiry(),
): Parcelable

@Parcelize
data class Logs(
    var distance: List<DistanceLog> = listOf(),
    var maintenance: List<RepairsLog> = listOf(),
    var mods: List<ModsLog> = listOf(),
): Parcelable

@Parcelize
data class Info(
    val cylinders: Int = 0,
    val engine_cc: Double = 0.0,
    val horse_power: Double = 0.0,
    val torque: Double = 0.0,
    val price: Double = 0.0,
    val front_tire: String = "",
    val rear_tire: String = "",
    val plate_number: String = ""
): Parcelable

@Parcelize
data class Expiry(
    val insurance: Long = -1,
    val tax: Long = -1,
    val inspection: Long = -1,
): Parcelable

@Parcelize
data class ModsLog(
    val title: String,
    val description: String,
    val date: Long,
    val price: Double
): Parcelable

@Parcelize
data class RepairsLog(
    val typeIndex: Int,
    val typeText: String,
    val notes: String,
    val date: Long,
    val repair_km: Int,
    val price: Double
): Parcelable

@Parcelize
data class DistanceLog(
    val distance: Int,
    val date: Long,
): Parcelable

fun getUpdatedBikeDistance(bike: Motorcycle): Int
{
    return if(bike.logs.distance.isNotEmpty()) bike.logs.distance.first().distance - bike.start_km else 0
}