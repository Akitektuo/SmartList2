package com.akitektuo.smartlist2.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.Themes
import kotlinx.android.synthetic.main.activity_recommendations.*

class RecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        Themes.setLightStatusBar(this)

        imageBack.setOnClickListener {
            finish()
        }
    }
}
