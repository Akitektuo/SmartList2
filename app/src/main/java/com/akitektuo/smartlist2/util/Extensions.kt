package com.akitektuo.smartlist2.util

import android.content.Context
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 17:25.
 */
fun Context?.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

private fun Date.addToDate(type: Int, amount: Int): Date {
    val newDate = Calendar.getInstance()
    newDate.time = this
    newDate.add(type, amount)
    return newDate.time
}

private fun Date.get(type: Int, useUtc: Boolean = false): Int {
    val date = if (useUtc)
        Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    else
        Calendar.getInstance()
    date.time = this
    return date.get(type)
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

fun Date.getUtcHours(): Int {
    return this.get(Calendar.HOUR_OF_DAY, true)
}

fun Date.getUtcMinutes(): Int {
    return this.get(Calendar.MINUTE, true)
}

fun Date.getLocalHours(): Int {
    return this.get(Calendar.HOUR_OF_DAY)
}

fun Date.getLocalMinutes(): Int {
    return this.get(Calendar.MINUTE)
}

fun Date.formatLastDate(): String {
    val now = Date()
    if (now.addDays(-1).time < this.time) {
        return this.format("kk:mm")
    }
    if (now.addWeeks(-1).time < this.time) {
        return this.format("EEE")
    }
    if (now.addYears(-1).time < this.time) {
        return this.format("d MMM")
    }
    return this.format("d MMM yyyy")
}

fun Date.formatTime(): String {
    return this.format("kk:mm", true)
}

fun Date.format(pattern: String, useUtc: Boolean = false): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    if (useUtc) {
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }
    return dateFormat.format(this)
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.displayError(msg: String) {
    val bar = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
    val view = bar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
    view.setTextColor(Color.WHITE)
    bar.view.setBackgroundColor(Color.RED)
    bar.show()
}

fun turnIntoMilliseconds(hours: Int, minutes: Int): Long {
    val oneMinute = 60000
    val oneHour = 60 * oneMinute
    return (oneHour * hours + oneMinute * minutes).toLong()
}