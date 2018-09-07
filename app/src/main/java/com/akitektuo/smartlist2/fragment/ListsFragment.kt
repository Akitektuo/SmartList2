package com.akitektuo.smartlist2.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.ListModel
import com.akitektuo.smartlist2.adapter.list.ListsAdapter
import com.akitektuo.smartlist2.util.addDays
import com.akitektuo.smartlist2.util.toast
import kotlinx.android.synthetic.main.fragment_lists.*
import java.util.*

class ListsFragment : Fragment() {

    private val adapter = ListsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_lists, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listLists.layoutManager = LinearLayoutManager(context)
        listLists.adapter = adapter
        swipeRefreshLists.setOnRefreshListener { repopulateList() }
    }

    override fun onResume() {
        super.onResume()
        loadTheme()
        repopulateList()
    }

    private fun repopulateList() {
        adapter.clear()
        for (i in 0..20) {
            adapter.add(ListModel("List ${i + 1}", i * 2, Date().addDays(-i), "Alex Copindean") {
                context.toast("Hi hi, you clicked me :\")")
            })
        }
        swipeRefreshLists.isRefreshing = false
    }

    private fun loadTheme() {
        if (database.theme.isLight()) {
            swipeRefreshLists.setProgressBackgroundColorSchemeResource(R.color.white)
            swipeRefreshLists.setColorSchemeResources(R.color.light_accent)
        } else {
            swipeRefreshLists.setProgressBackgroundColorSchemeResource(R.color.black)
            swipeRefreshLists.setColorSchemeResources(R.color.dark_accent)
        }
    }

}