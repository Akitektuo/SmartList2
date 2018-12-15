package com.akitektuo.smartlist2.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.Themes

open class ThemeActivity : AppCompatActivity() {

    var dialogTheme = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
    }

    private fun applyTheme() {
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