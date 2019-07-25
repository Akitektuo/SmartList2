package com.akitektuo.smartlist2.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.util.Themes

open class ThemeActivity : AppCompatActivity() {

    var dialogTheme = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
    }

    private fun applyTheme() {
        with(database.theme) {
            if (!isSet()) {
                restoreTheme(this@ThemeActivity)
            }
        }
        dialogTheme = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            setTheme(R.style.AppTheme_Light)
            Themes.setLightStatusBar(this)
            R.style.DialogLightTheme
        } else {
            setTheme(R.style.AppTheme_Dark)
            Themes.setDarkStatusBar(this)
            R.style.DialogDarkTheme
        }
    }

}