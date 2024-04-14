package com.example.motolog

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

fun longToDateString(date: Long): String{
    val simpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    return simpleDateFormat.format(Date(date))
}
fun yearFromLong(date: Long): Int{
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(Date(date))
    return cal.get(Calendar.YEAR)
}
fun showToast(context: Context, text: String, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context, text, length).show()
}
fun formatThousand(number: Int): String{
    val formatter = DecimalFormat("#,###")
    return formatter.format(number)
}