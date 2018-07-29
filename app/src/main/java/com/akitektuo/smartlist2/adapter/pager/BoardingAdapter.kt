package com.akitektuo.smartlist2.adapter.pager

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.akitektuo.smartlist2.fragment.BoardingFragment
import com.akitektuo.smartlist2.util.Keys.Companion.KEY_DESCRIPTION
import com.akitektuo.smartlist2.util.Keys.Companion.KEY_IMAGE_ID
import com.akitektuo.smartlist2.util.Keys.Companion.KEY_TITLE

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 08:32.
 */
class BoardingAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val fragments = ArrayList<BoardingFragment>()

    fun add(imageId: Int, title: String, description: String) {
        val bundle = Bundle()
        with(bundle) {
            putInt(KEY_IMAGE_ID, imageId)
            putString(KEY_TITLE, title)
            putString(KEY_DESCRIPTION, description)
        }
        val fragment = BoardingFragment()
        fragment.arguments = bundle
        fragments.add(fragment)
    }

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}