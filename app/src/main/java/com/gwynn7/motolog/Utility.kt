package com.gwynn7.motolog

import android.app.Activity
import android.content.Context
import android.widget.Toast
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

fun stop(activity: Activity?)
{
    activity?.finish()
}