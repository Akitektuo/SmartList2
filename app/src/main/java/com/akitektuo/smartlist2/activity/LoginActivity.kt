package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.adapter.pager.BoardingAdapter
import com.akitektuo.smartlist2.server.Authentication
import com.akitektuo.smartlist2.util.Themes.Companion.setLightStatusBar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val authentication = Authentication(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setLightStatusBar(this)

        setupViewPager()

        textSignIn.setOnClickListener {
            authentication.signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authentication.processResult(requestCode, data) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupViewPager() {
        val adapter = BoardingAdapter(supportFragmentManager)
        adapter.add(R.drawable.color_welcome, "Welcome", getString(R.string.welcome_description))
        adapter.add(R.drawable.color_cloud, "Cloud sync", getString(R.string.cloud_description))
        adapter.add(R.drawable.color_sheet, "Excel and Sheets", getString(R.string.sheet_description))
        adapter.add(R.drawable.color_ai, "Artificial Intelligence", getString(R.string.ai_description))
        pagerBoarding.adapter = adapter
        dotsBoarding.setViewPager(pagerBoarding)
        pagerBoarding.setScrollDurationFactor(2.0)
        pagerBoarding.interval = 5000
        pagerBoarding.startAutoScroll(5000)
    }
}
