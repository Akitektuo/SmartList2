package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.OptionAdapter
import com.akitektuo.smartlist2.adapter.list.OptionModel
import com.akitektuo.smartlist2.adapter.pager.ListsAdapter
import com.akitektuo.smartlist2.util.Themes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ThemeActivity() {

    private val optionAdapter = OptionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!database.isUserSignedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        with(database.theme) {
            if (isSet()) {
                setupDrawer()
                setupLists()
                return
            }
            database.getCurrentUser {
                mode = it.mode
                lightStart = it.lightStart
                darkStart = it.darkStart
                loadTheme()
                setupDrawer()
                setupLists()
                repopulateOptions()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!database.theme.isSet()) {
            return
        }
        loadTheme()
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
        optionAdapter.add(OptionModel("Preferred Currency") {
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
            drawerMain.openDrawer(Gravity.START)
        }
        imageBack.setOnClickListener {
            drawerMain.closeDrawer(Gravity.START)
        }
        Glide.with(this).load(database.auth.currentUser?.photoUrl).apply(RequestOptions().placeholder(R.drawable.profile_placeholder)).into(imageProfile)
        textName.text = database.auth.currentUser?.displayName
        textEmail.text = database.auth.currentUser?.email
        listOptions.layoutManager = LinearLayoutManager(this)
        listOptions.adapter = optionAdapter
    }

    private fun startActivity(cls: Class<*>) {
        drawerMain.closeDrawer(Gravity.START)
        startActivity(Intent(this, cls))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun setupLists() {
        val fragmentsLists = ListsAdapter(supportFragmentManager)
        fragmentsLists.add("My lists", true)
        fragmentsLists.add("Shared with me", false)
        pagerLists.adapter = fragmentsLists
        tabLists.setupWithViewPager(pagerLists)
    }

    override fun useLightTheme() {
        val colorBlack = ContextCompat.getColor(this, R.color.black)
        val colorAccent = ContextCompat.getColor(this, R.color.light_accent)

        Themes.setLightStatusBar(this)
        drawerMain.setBackgroundResource(R.color.white)
        textApplicationName.setTextColor(colorBlack)
        imageMenu.setImageResource(R.drawable.ic_light_menu)
        imageAdd.setImageResource(R.drawable.light_add)
        tabLists.setTabTextColors(colorBlack, colorAccent)
        tabLists.setSelectedTabIndicatorColor(colorAccent)

        imageDrawerBackground.setImageResource(R.drawable.drawer_light_background)
        listOptions.setBackgroundResource(R.color.white)
    }

    override fun useDarkTheme() {
        val colorWhite = ContextCompat.getColor(this, R.color.white)
        val colorAccent = ContextCompat.getColor(this, R.color.dark_accent)

        Themes.setDarkStatusBar(this)
        drawerMain.setBackgroundResource(R.color.black)
        textApplicationName.setTextColor(colorWhite)
        imageMenu.setImageResource(R.drawable.ic_dark_menu)
        imageAdd.setImageResource(R.drawable.dark_add)
        tabLists.setTabTextColors(colorWhite, colorAccent)
        tabLists.setSelectedTabIndicatorColor(colorAccent)

        imageDrawerBackground.setImageResource(R.drawable.drawer_dark_background)
        listOptions.setBackgroundResource(R.color.black)
    }

}
