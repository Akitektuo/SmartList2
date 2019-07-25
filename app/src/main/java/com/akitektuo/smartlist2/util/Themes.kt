package com.akitektuo.smartlist2.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.server.Database
import com.akitektuo.smartlist2.util.Constants.Companion.KEY_DARK_START
import com.akitektuo.smartlist2.util.Constants.Companion.KEY_LIGHT_START
import com.akitektuo.smartlist2.util.Constants.Companion.KEY_MODE
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_ADAPTIVE
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_DARK
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_LIGHT
import com.akitektuo.smartlist2.util.Constants.Companion.NOT_SET
import java.util.*

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 13:59.
 */
class Themes {

    companion object {
        fun setLightStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val view = activity.window.decorView.rootView
                val flags = view.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.systemUiVisibility = flags
                activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
                return
            }
            setDarkStatusBar(activity)
        }

        fun setDarkStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val view = activity.window.decorView.rootView
                val flags = view.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                view.systemUiVisibility = flags
            }
            activity.window.statusBarColor = (ContextCompat.getColor(activity, R.color.black))
        }
    }

    var lightStart = NOT_SET.toLong()
    var darkStart = NOT_SET.toLong()
    var mode = NOT_SET

    fun isSet() = mode != NOT_SET

    fun loadTheme(user: Database.User) {
        mode = user.mode
        darkStart = user.darkStart
        lightStart = user.lightStart
    }

    private fun isLight(): Boolean {
        return when (mode) {
            MODE_ADAPTIVE -> {
                val deviceTime = Date(System.currentTimeMillis())
                val deviceHoursAndMinutes = turnIntoMilliseconds(deviceTime.getLocalHours(), deviceTime.getLocalMinutes())
                if (lightStart < darkStart) {
                    deviceHoursAndMinutes in lightStart..(darkStart - 1)
                } else {
                    deviceHoursAndMinutes !in darkStart..(lightStart - 1)
                }
            }
            MODE_LIGHT -> true
            MODE_DARK -> false
            else -> true
        }
    }

    fun setupTheme() {
        AppCompatDelegate.setDefaultNightMode(if (isLight()) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        })
    }

    fun saveInMemory(context: Context) {
        val preferences = Preferences(context)
        with(preferences) {
            set(KEY_MODE, mode)
            set(KEY_LIGHT_START, lightStart)
            set(KEY_DARK_START, darkStart)
        }
    }

    fun restoreTheme(context: Context) {
        val preferences = Preferences(context)
        with(preferences) {
            mode = getInt(KEY_MODE)
            lightStart = getLong(KEY_LIGHT_START)
            darkStart = getLong(KEY_DARK_START)
        }
        setupTheme()
    }

}