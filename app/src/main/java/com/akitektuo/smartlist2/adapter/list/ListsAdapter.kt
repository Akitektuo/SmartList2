package com.akitektuo.smartlist2.adapter.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.formatLastDate
import java.util.*

data class ListModel(val name: String, val products: Int, val timestamp: Date, val owner: String, val onClick: () -> Unit)

class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textList = view.findViewById<TextView>(R.id.textList)
    private val textProducts = view.findViewById<TextView>(R.id.textProducts)
    private val textModifiedDate = view.findViewById<TextView>(R.id.textModifiedDate)
    private val textOwner = view.findViewById<TextView>(R.id.textOwner)

    fun bind(list: ListModel) = with(list) {
        textList.text = name
        textProducts.text = if (products > 0) "$products product${if (products > 1) "s" else ""}" else "No products"
        textModifiedDate.text = timestamp.formatLastDate()
        textOwner.text = owner
        view.setOnClickListener {
            onClick()
        }
    }
}

class ListsAdaper : RecyclerView.Adapter<ListViewHolder>() {

    private val lists = ArrayList<ListModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))

    override fun getItemCount() = lists.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) = holder.bind(lists[position])

    fun add(list: ListModel) {
        val position = lists.size
        lists.add(list)
        notifyItemInserted(position)
    }

}