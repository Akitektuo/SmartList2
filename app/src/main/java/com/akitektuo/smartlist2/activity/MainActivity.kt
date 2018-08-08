package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.OptionAdaper
import com.akitektuo.smartlist2.adapter.list.OptionModel
import com.akitektuo.smartlist2.util.Themes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if user is logged in
        if (!database.isUserSignedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Setup theme
        Themes.setLightStatusBar(this)

        // Setup functionality
        imageMenu.setOnClickListener {
            drawerMain.openDrawer(Gravity.START)
        }
        imageBack.setOnClickListener {
            drawerMain.closeDrawer(Gravity.START)
        }
        Glide.with(this).load(database.currentUser?.photoUrl).apply(RequestOptions().placeholder(R.drawable.profile_placeholder)).into(imageProfile)
        textName.text = database.currentUser?.displayName
        textEmail.text = database.currentUser?.email
        val adapter = OptionAdaper()
        listOptions.layoutManager = LinearLayoutManager(this)
        listOptions.adapter = adapter
        adapter.add(OptionModel("Manage Categories") {

        })
        adapter.add(OptionModel("Adaptive Mode") {

        })
        adapter.add(OptionModel("Recommendations") {

        })
        adapter.add(OptionModel("Auto Fill") {

        })
        adapter.add(OptionModel("Preferred Currency") {

        })
        adapter.add(OptionModel("Graph Columns") {

        })
        adapter.add(OptionModel("Sign out") {
            database.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
    }
}
