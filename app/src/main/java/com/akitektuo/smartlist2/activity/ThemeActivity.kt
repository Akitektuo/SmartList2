package com.akitektuo.smartlist2.activity

import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.SmartList.Companion.database

open class ThemeActivity : AppCompatActivity() {

    internal fun loadTheme() {
        if (database.theme.isLight()) {
            useLightTheme()
        } else {
            useDarkTheme()
        }
    }

    internal open fun useLightTheme() {}

    internal open fun useDarkTheme() {}

}