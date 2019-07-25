package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.OptionAdapter
import com.akitektuo.smartlist2.adapter.list.OptionModel
import com.akitektuo.smartlist2.adapter.pager.ListsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ThemeActivity() {

    private val optionAdapter = OptionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDrawer()
        setupLists()
        repopulateOptions()
    }

    private fun repopulateOptions() {
        optionAdapter.clear()
        optionAdapter.add(OptionModel("Manage Categories") {
            startActivity(CategoriesActivity::class.java)
        })
        optionAdapter.add(OptionModel("Adaptive Mode") {
            startActivity(AdaptiveActivity::class.java)
        })
        optionAdapter.add(OptionModel("Recommendations") {
            startActivity(RecommendationsActivity::class.java)
        })
        optionAdapter.add(OptionModel("Auto Fill") {
            startActivity(AutoFillActivity::class.java)
        })
        optionAdapter.add(OptionModel("Currencies") {
            startActivity(CurrencyActivity::class.java)
        })
        optionAdapter.add(OptionModel("Statistics") {
            startActivity(StatisticsActivity::class.java)
        })
        optionAdapter.add(OptionModel("Sign out") {
            database.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
    }

    private fun setupDrawer() {
        imageMenu.setOnClickListener {
            drawerMain.openDrawer(GravityCompat.START)
        }
        imageBack.setOnClickListener {
            drawerMain.closeDrawer(GravityCompat.START)
        }
        Glide.with(this).load(database.auth.currentUser?.photoUrl).apply(RequestOptions().placeholder(R.drawable.profile_placeholder)).into(imageProfile)
        textItem.text = database.auth.currentUser?.displayName
        textEmail.text = database.auth.currentUser?.email
        listOptions.layoutManager = LinearLayoutManager(this)
        listOptions.adapter = optionAdapter
    }

    private fun startActivity(cls: Class<*>) {
        drawerMain.closeDrawer(GravityCompat.START)
        startActivity(Intent(this, cls))
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        finish()
    }

    private fun setupLists() {
        val fragmentsLists = ListsAdapter(supportFragmentManager)
        fragmentsLists.add("My lists", true)
        fragmentsLists.add("Shared with me", false)
        pagerLists.adapter = fragmentsLists
        tabLists.setupWithViewPager(pagerLists)
    }

}
