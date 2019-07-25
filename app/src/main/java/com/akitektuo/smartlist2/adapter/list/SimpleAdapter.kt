package com.akitektuo.smartlist2.adapter.list

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.inflate

class SimpleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textItem = view.findViewById<TextView>(R.id.textItem)

    fun bind(item: String) {
        textItem.text = item
    }
}

class SimpleAdapter : RecyclerView.Adapter<SimpleViewHolder>() {

    private val items = ArrayList<String>()

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) = holder.bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SimpleViewHolder(parent.inflate(R.layout.item_simple))

    fun add(item: String) {
        val position = items.size
        items.add(item)
        notifyItemInserted(position)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun isEmpty() = items.size == 0

}