package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.util.Themes

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Themes.setLightStatusBar(this)

        if (!database.isUserSignedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        database.getCurrentUser {
            with(database.theme) {
                loadTheme(it)
                saveInMemory(this@SplashActivity)
                setupTheme()
            }
            Handler().postDelayed({
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 100)
        }
    }
}
