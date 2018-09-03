package com.akitektuo.smartlist2.activity

import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.server.Database
import com.akitektuo.smartlist2.util.*
import kotlinx.android.synthetic.main.activity_adaptive.*
import java.util.*


class AdaptiveActivity : AppCompatActivity() {

    private lateinit var currentUser: Database.User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adaptive)

        loadDataForCurrentUser()
        setupChecks()
        setupClicks()
    }

    private fun loadDataForCurrentUser() {
        database.getCurrentUser {
            currentUser = it
            var lightDate = Date(currentUser.lightStart)
            var darkDate = Date(currentUser.darkStart)
            when (currentUser.mode) {
                0 -> {
                    currentUser.computeThemeForTime { isLight ->
                        loadTheme(isLight)
                    }
                    radioAdaptiveMode.isChecked = true
                }
                1 -> {
                    loadTheme(true)
                    radioLightMode.isChecked = true

                }
                2 -> {
                    loadTheme(false)
                    radioDarkMode.isChecked = true
                }
            }
            textSwitchLight.text = "Switch to light mode at ${lightDate.formatTime()}"
            textSwitchDark.text = "Switch to dark mode at ${darkDate.formatTime()}"
            imageEditLight.setOnClickListener { _ ->
                TimePickerDialog(this, { _, hours: Int, minutes: Int ->
                    val newLightStart = turnIntoMilliseconds(hours, minutes)
                    if (newLightStart == currentUser.darkStart) {
                        toast("Cannot switch to light mode and dark mode at the same time")
                        return@TimePickerDialog
                    }
                    lightDate = Date(newLightStart)
                    currentUser.lightStart = newLightStart
                    database.setUser(currentUser) {
                        textSwitchLight.text = "Switch to light mode at ${lightDate.formatTime()}"
                        currentUser.computeThemeForTime { isLight ->
                            loadTheme(isLight)
                        }
                    }
                }, lightDate.getUtcHours(), lightDate.getUtcMinutes(), true).show()
            }
            imageEditDark.setOnClickListener { _ ->
                TimePickerDialog(this, { _, hours: Int, minutes: Int ->
                    val newDarkStart = turnIntoMilliseconds(hours, minutes)
                    if (newDarkStart == currentUser.lightStart) {
                        toast("Cannot switch to light mode and dark mode at the same time")
                        return@TimePickerDialog
                    }
                    darkDate = Date(newDarkStart)
                    currentUser.darkStart = newDarkStart
                    database.setUser(currentUser) {
                        textSwitchDark.text = "Switch to dark mode at ${darkDate.formatTime()}"
                        currentUser.computeThemeForTime { isLight ->
                            loadTheme(isLight)
                        }
                    }
                }, darkDate.getUtcHours(), darkDate.getUtcMinutes(), true).show()
            }
        }
    }

    private fun setupChecks() {
        radioAdaptiveMode.setOnCheckedChangeListener { _, value ->
            if (value) {
                radioLightMode.isChecked = false
                radioDarkMode.isChecked = false
                currentUser.mode = 0
                database.setUser(currentUser) {
                    currentUser.computeThemeForTime { isLight ->
                        loadTheme(isLight)
                    }
                }
            }
        }
        radioLightMode.setOnCheckedChangeListener { _, value ->
            if (value) {
                radioAdaptiveMode.isChecked = false
                radioDarkMode.isChecked = false
                currentUser.mode = 1
                database.setUser(currentUser) {
                    loadTheme(true)
                }
            }
        }
        radioDarkMode.setOnCheckedChangeListener { _, value ->
            if (value) {
                radioAdaptiveMode.isChecked = false
                radioLightMode.isChecked = false
                currentUser.mode = 2
                database.setUser(currentUser) {
                    loadTheme(false)
                }
            }
        }
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

    private fun loadTheme(isLight: Boolean) {
        if (isLight) {
            useLightTheme()
        } else {
            useDarkTheme()
        }
    }

    private fun useLightTheme() {
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
    }

    private fun useDarkTheme() {
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
    }
}
