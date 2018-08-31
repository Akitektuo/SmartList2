package com.akitektuo.smartlist2.adapter.pager

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.akitektuo.smartlist2.fragment.ListsFragment
import com.akitektuo.smartlist2.util.Keys.Companion.KEY_OWNER

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 08:32.
 */
class ListsAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val fragments = ArrayList<ListsFragment>()
    private val titles = ArrayList<String>()

    fun add(title: String, isOwner: Boolean) {
        val bundle = Bundle()
        with(bundle) {
            putBoolean(KEY_OWNER, isOwner)
        }
        val fragment = ListsFragment()
        fragment.arguments = bundle
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}