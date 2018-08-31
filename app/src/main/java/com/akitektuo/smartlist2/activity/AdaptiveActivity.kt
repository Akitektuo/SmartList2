package com.akitektuo.smartlist2.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.Themes
import kotlinx.android.synthetic.main.activity_adaptive.*

class AdaptiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adaptive)

        Themes.setLightStatusBar(this)

        imageBack.setOnClickListener {
            finish()
        }

        radioAdaptiveMode.isChecked = true
        radioLightMode.isChecked = false
        radioDarkMode.isChecked = false
        radioAdaptiveMode.setOnCheckedChangeListener { _, value ->
            if (value) {
                radioLightMode.isChecked = false
                radioDarkMode.isChecked = false
            }
        }
        radioLightMode.setOnCheckedChangeListener { _, value ->
            if (value) {
                radioAdaptiveMode.isChecked = false
                radioDarkMode.isChecked = false
            }
        }
        radioDarkMode.setOnCheckedChangeListener { _, value ->
            if (value) {
                radioAdaptiveMode.isChecked = false
                radioLightMode.isChecked = false
            }
        }
    }
}
