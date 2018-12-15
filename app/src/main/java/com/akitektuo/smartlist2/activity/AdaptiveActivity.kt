package com.akitektuo.smartlist2.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.util.*
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_ADAPTIVE
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_DARK
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_LIGHT
import kotlinx.android.synthetic.main.activity_adaptive.*
import java.util.*


class AdaptiveActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adaptive)

        loadDataForCurrentUser()
        setupChecks()
        setupClicks()
    }

    override fun finish() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
        super.finish()
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
                    changeTheme()
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
                    changeTheme()
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
        changeTheme()
    }

    private fun changeTheme() {
        database.theme.setupTheme()
        recreate()
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

}
