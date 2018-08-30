package com.akitektuo.smartlist2.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.Themes
import kotlinx.android.synthetic.main.activity_auto_fill.*

class AutoFillActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_fill)

        Themes.setLightStatusBar(this)

        imageBack.setOnClickListener {
            finish()
        }
    }
}
