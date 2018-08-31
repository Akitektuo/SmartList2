package com.akitektuo.smartlist2.util

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 17:25.
 */
fun Context?.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Date.addToDate(type: Int, amount: Int): Date {
    val newDate = Calendar.getInstance()
    newDate.time = this
    newDate.add(type, amount)
    return newDate.time
}

fun Date.addDays(days: Int): Date {
    return this.addToDate(Calendar.DATE, days)
}

fun Date.addWeeks(weeks: Int): Date {
    return this.addToDate(Calendar.DATE, 7 * weeks)
}

fun Date.addYears(years: Int): Date {
    return this.addToDate(Calendar.YEAR, years)
}

fun Date.formatLastDate(): String {
    val now = Date()
    if (now.addDays(-1).time < this.time) {
        return SimpleDateFormat("kk:mm", Locale.getDefault()).format(this)
    }
    if (now.addWeeks(-1).time < this.time) {
        return SimpleDateFormat("EEE", Locale.getDefault()).format(this)
    }
    if (now.addYears(-1).time < this.time) {
        return SimpleDateFormat("d MMM", Locale.getDefault()).format(this)
    }
    return SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(this)
}