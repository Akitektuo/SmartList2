package com.akitektuo.smartlist2.util

import android.app.Activity
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import com.akitektuo.smartlist2.R

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

}