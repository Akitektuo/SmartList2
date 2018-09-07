package com.akitektuo.smartlist2.activity

import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.util.*
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_ADAPTIVE
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_DARK
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_LIGHT
import com.akitektuo.smartlist2.util.Constants.Companion.NOT_SET
import kotlinx.android.synthetic.main.activity_adaptive.*
import java.util.*


class AdaptiveActivity : ThemeActivity() {

    private var dialogTheme = NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adaptive)

        setupClicks()
    }

    override fun setupWithTheme(isLight: Boolean) {
        super.setupWithTheme(isLight)

        loadDataForCurrentUser()
        setupChecks()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
    }

    private fun loadDataForCurrentUser() {
        with(database.theme) {
            var lightDate = Date(lightStart)
            var darkDate = Date(darkStart)
            when (mode) {
                MODE_ADAPTIVE -> radioAdaptiveMode.isChecked = true
                MODE_LIGHT -> radioLightMode.isChecked = true
                MODE_DARK -> radioDarkMode.isChecked = true
            }
            textSwitchLight.text = "Switch to light mode at ${lightDate.formatTime()}"
            textSwitchDark.text = "Switch to dark mode at ${darkDate.formatTime()}"
            imageEditLight.setOnClickListener { _ ->
                TimePickerDialog(this@AdaptiveActivity, dialogTheme, { _, hours: Int, minutes: Int ->
                    val newLightStart = turnIntoMilliseconds(hours, minutes)
                    if (newLightStart == darkStart) {
                        toast("Cannot switch to light mode and dark mode at the same time")
                        return@TimePickerDialog
                    }
                    lightDate = Date(newLightStart)
                    lightStart = newLightStart
                    database.editCurrentUser {
                        it.lightStart = newLightStart
                        it
                    }
                    textSwitchLight.text = "Switch to light mode at ${lightDate.formatTime()}"
                    loadTheme()
                }, lightDate.getUtcHours(), lightDate.getUtcMinutes(), true).show()
            }
            imageEditDark.setOnClickListener { _ ->
                TimePickerDialog(this@AdaptiveActivity, dialogTheme, { _, hours: Int, minutes: Int ->
                    val newDarkStart = turnIntoMilliseconds(hours, minutes)
                    if (newDarkStart == lightStart) {
                        toast("Cannot switch to light mode and dark mode at the same time")
                        return@TimePickerDialog
                    }
                    darkDate = Date(newDarkStart)
                    darkStart = newDarkStart
                    database.editCurrentUser {
                        it.darkStart = newDarkStart
                        it
                    }
                    textSwitchDark.text = "Switch to dark mode at ${darkDate.formatTime()}"
                    loadTheme()
                }, darkDate.getUtcHours(), darkDate.getUtcMinutes(), true).show()
            }
        }
    }

    private fun setupChecks() {
        with(database.theme) {
            radioAdaptiveMode.setOnCheckedChangeListener { _, value ->
                if (value) {
                    radioLightMode.isChecked = false
                    radioDarkMode.isChecked = false
                    mode = MODE_ADAPTIVE
                    updateMode(mode)
                }
            }
            radioLightMode.setOnCheckedChangeListener { _, value ->
                if (value) {
                    radioAdaptiveMode.isChecked = false
                    radioDarkMode.isChecked = false
                    mode = MODE_LIGHT
                    updateMode(mode)
                }
            }
            radioDarkMode.setOnCheckedChangeListener { _, value ->
                if (value) {
                    radioAdaptiveMode.isChecked = false
                    radioLightMode.isChecked = false
                    mode = MODE_DARK
                    updateMode(mode)
                }
            }
        }
    }

    private fun updateMode(mode: Int) {
        database.editCurrentUser {
            it.mode = mode
            it
        }
        loadTheme()
    }

    private fun setupClicks() {
        imageBack.setOnClickListener {
            finish()
        }
        textAdaptiveMode.setOnClickListener {
            radioAdaptiveMode.isChecked = true
        }
        textLightMode.setOnClickListener {
            radioLightMode.isChecked = true
        }
        textDarkMode.setOnClickListener {
            radioDarkMode.isChecked = true
        }
        textSwitchLight.setOnClickListener {
            imageEditLight.performClick()
        }
        textSwitchDark.setOnClickListener {
            imageEditDark.performClick()
        }
    }

    override fun useLightTheme() {
        val colorBlack = ContextCompat.getColor(this, R.color.black)
        val colorAccent = ContextCompat.getColor(this, R.color.light_accent)
        val colorGray = ContextCompat.getColor(this, R.color.gray)
        val colorState = ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                intArrayOf(colorGray, colorAccent)
        )

        Themes.setLightStatusBar(this)

        layoutAdaptive.setBackgroundResource(R.color.white)
        textAdaptive.setTextColor(colorBlack)
        imageBack.setImageResource(R.drawable.light_back)
        radioAdaptiveMode.buttonTintList = colorState
        textAdaptiveMode.setTextColor(colorBlack)
        imageEditLight.setImageResource(R.drawable.ic_light_time)
        imageEditDark.setImageResource(R.drawable.ic_light_time)
        radioLightMode.buttonTintList = colorState
        textLightMode.setTextColor(colorBlack)
        radioDarkMode.buttonTintList = colorState
        textDarkMode.setTextColor(colorBlack)
        dialogTheme = R.style.DialogLightTheme
    }

    override fun useDarkTheme() {
        val colorWhite = ContextCompat.getColor(this, R.color.white)
        val colorAccent = ContextCompat.getColor(this, R.color.dark_accent)
        val colorGray = ContextCompat.getColor(this, R.color.gray)
        val colorState = ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                intArrayOf(colorGray, colorAccent)
        )

        Themes.setDarkStatusBar(this)

        layoutAdaptive.setBackgroundResource(R.color.black)
        textAdaptive.setTextColor(ContextCompat.getColor(this, R.color.white))
        imageBack.setImageResource(R.drawable.dark_back)
        radioAdaptiveMode.buttonTintList = colorState
        textAdaptiveMode.setTextColor(colorWhite)
        imageEditLight.setImageResource(R.drawable.ic_dark_time)
        imageEditDark.setImageResource(R.drawable.ic_dark_time)
        radioLightMode.buttonTintList = colorState
        textLightMode.setTextColor(colorWhite)
        radioDarkMode.buttonTintList = colorState
        textDarkMode.setTextColor(colorWhite)
        dialogTheme = R.style.DialogDarkTheme
    }
}
