package com.akitektuo.smartlist2.activity

import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.util.Constants
import com.akitektuo.smartlist2.util.Themes

open class ThemeActivity : AppCompatActivity() {

    private var shouldRefresh = false

    override fun onStart() {
        super.onStart()
        shouldRefresh = false
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

    internal open fun useLightTheme() {
        Themes.setLightStatusBar(this)
    }

    internal open fun useDarkTheme() {
        Themes.setDarkStatusBar(this)
    }

    internal open fun setupWithTheme(isLight: Boolean) {
        loadTheme()
    }

    internal open fun refreshActivity() {
        loadTheme()
    }

    override fun onResume() {
        super.onResume()
        if (database.theme.isSet() && shouldRefresh) {
            refreshActivity()
        }
        shouldRefresh = true
    }

    override fun onDestroy() {
        super.onDestroy()
        database.theme.mode = Constants.NOT_SET
    }

}