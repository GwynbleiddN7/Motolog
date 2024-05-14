package com.gwynn7.motolog

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class Path{
    Add,
    Edit
}
val repairColors = arrayOf(
    R.color.red,
    R.color.yellow,
    R.color.orange,
    R.color.blue,
    R.color.cyan,
    R.color.green,
    R.color.violet,
    R.color.purple,
    R.color.pink,
    R.color.white
)

object UnitHelper
{
    private val distanceKey = stringPreferencesKey("distance")
    private val currencyKey = stringPreferencesKey("currency")

    enum class Currency(val value: String){
        EUR("€"),
        USD("$"),
        GBP("£"),
        JPY("¥")
    }

    enum class Distance(val value: String){
        KM("km"),
        MILES("mi"),
    }

    lateinit var distance: Distance
    lateinit var currency: Currency

    fun getDistance() = distance.value
    fun getCurrency() = currency.value

    fun loadData(context: Context) {
        runBlocking {
            val settings = context.settings.data.first()
            distance = fromDistance(settings[distanceKey] ?: Distance.KM.value)
            currency = fromCurrency(settings[currencyKey] ?: Currency.EUR.value)
        }
    }

    fun saveDistance(context: Context, newDistance: Distance) {
        runBlocking { context.settings.edit { settings -> settings[distanceKey] = newDistance.value }}
        distance = newDistance
    }
    fun saveCurrency(context: Context, newCurrency: Currency) {
        runBlocking { context.settings.edit { settings -> settings[currencyKey] = newCurrency.value }}
        currency = newCurrency
    }

    private fun fromDistance(value: String): Distance = Distance.entries.first { it.value == value }
    private fun fromCurrency(value: String): Currency = Currency.entries.first { it.value == value }

}

fun longToDateString(date: Long): String{
    val simpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    return simpleDateFormat.format(Date(date))
}
fun yearFromLong(date: Long): Int{
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(Date(date))
    return cal.get(Calendar.YEAR)
}

fun longFromDate(year: Int, month: Int, dayOfMonth: Int): Long{
    val cal: Calendar = Calendar.getInstance()
    cal.set(year, month, dayOfMonth)
    return cal.getTimeInMillis()
}

fun showToast(context: Context, text: String, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context, text, length).show()
}
fun formatThousand(number: Int): String{
    val formatter = DecimalFormat("#,###")
    return formatter.format(number)
}

fun stop(activity: Activity?) {
    activity?.finish()
}