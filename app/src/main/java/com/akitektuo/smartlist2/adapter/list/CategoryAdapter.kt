package com.akitektuo.smartlist2.adapter.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.inflate

data class CategoryModel(val name: String, val products: Int, val onClick: () -> Unit)

class CategoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.findViewById<TextView>(R.id.textItem)
    private val textProducts = view.findViewById<TextView>(R.id.textProducts)

    fun bind(context: Context, category: CategoryModel) = with(category) {
        textName.text = name
        textProducts.text = when (products) {
            0 -> "No products"
            1 -> "1 product"
            else -> "$products products"
        }
        view.setOnClickListener {
            onClick()
        }
    }
}

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {

    private val categories = ArrayList<CategoryModel>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        context = parent.context
        return CategoryViewHolder(parent.inflate(R.layout.item_category))
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) = holder.bind(context, categories[position])

    fun add(category: CategoryModel) {
        val position = categories.size
        categories.add(category)
        notifyItemInserted(position)
    }

    fun clear() {
        categories.clear()
        notifyDataSetChanged()
    }

}