package com.akitektuo.smartlist2.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.adapter.list.ListModel
import com.akitektuo.smartlist2.adapter.list.ListsAdaper
import com.akitektuo.smartlist2.util.addDays
import com.akitektuo.smartlist2.util.toast
import kotlinx.android.synthetic.main.fragment_lists.*
import java.util.*

class ListsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_lists, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipeRefreshLists.setProgressBackgroundColorSchemeResource(R.color.white)
        swipeRefreshLists.setColorSchemeResources(R.color.light_accent)

        val adapter = ListsAdaper()
        listLists.layoutManager = LinearLayoutManager(context)
        listLists.adapter = adapter
        for (i in 0..20) {
            adapter.add(ListModel("List ${i + 1}", i * 2, Date().addDays(-i), "Alex Copindean") {
                context.toast("Hi hi, you clicked me :\")")
            })
        }
    }

}