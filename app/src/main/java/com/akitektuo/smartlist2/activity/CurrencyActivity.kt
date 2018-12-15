package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import com.akitektuo.smartlist2.R
import kotlinx.android.synthetic.main.activity_currency.*

class CurrencyActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        imageBack.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
        super.finish()
    }
}
