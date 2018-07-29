package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.server.Database

class MainActivity : AppCompatActivity() {

    private val database = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!database.isUserSignedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
