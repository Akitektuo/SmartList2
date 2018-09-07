package com.akitektuo.smartlist2.adapter.list

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.util.formatLastDate
import com.akitektuo.smartlist2.util.inflate
import java.util.*

data class ListModel(val name: String, val products: Int, val timestamp: Date, val owner: String, val onClick: () -> Unit)

class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textList = view.findViewById<TextView>(R.id.textList)
    private val textProducts = view.findViewById<TextView>(R.id.textProducts)
    private val textModifiedDate = view.findViewById<TextView>(R.id.textModifiedDate)
    private val textOwner = view.findViewById<TextView>(R.id.textOwner)

    fun bind(context: Context, list: ListModel) = with(list) {
        textList.text = name
        textProducts.text = if (products > 0) "$products product${if (products > 1) "s" else ""}" else "No products"
        textModifiedDate.text = timestamp.formatLastDate()
        textOwner.text = owner
        view.setOnClickListener {
            onClick()
        }
        if (database.theme.isLight()) {
            val colorBlack = ContextCompat.getColor(context, R.color.black)
            textList.setTextColor(colorBlack)
            textOwner.setTextColor(colorBlack)
        } else {
            val colorWhite = ContextCompat.getColor(context, R.color.white)
            textList.setTextColor(colorWhite)
            textOwner.setTextColor(colorWhite)
        }
    }
}

class ListsAdapter : RecyclerView.Adapter<ListViewHolder>() {

    private val lists = ArrayList<ListModel>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        context = parent.context
        return ListViewHolder(parent.inflate(R.layout.item_list))
    }

    override fun getItemCount() = lists.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) = holder.bind(context, lists[position])

    fun add(list: ListModel) {
        val position = lists.size
        lists.add(list)
        notifyItemInserted(position)
    }

    fun clear() {
        lists.clear()
        notifyDataSetChanged()
    }

}