package com.akitektuo.smartlist2.activity

import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.util.Constants

open class ThemeActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        with(database.theme) {
            if (isSet()) {
                setupWithTheme(isLight())
                return
            }
            database.getCurrentUser {
                mode = it.mode
                lightStart = it.lightStart
                darkStart = it.darkStart
                setupWithTheme(isLight())
            }
        }
    }

    internal fun loadTheme() {
        if (database.theme.isLight()) {
            useLightTheme()
        } else {
            useDarkTheme()
        }
    }

    internal open fun useLightTheme() {}

    internal open fun useDarkTheme() {}

    internal open fun setupWithTheme(isLight: Boolean) {
        loadTheme()
    }

    internal open fun refreshActivity() {
        loadTheme()
    }

    override fun onResume() {
        super.onResume()
        if (database.theme.isSet()) {
            refreshActivity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        database.theme.mode = Constants.NOT_SET
    }

}