package com.akitektuo.smartlist2.adapter.list

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.inflate
import com.akitektuo.smartlist2.util.rotate

data class ProductModel(val id: String, val name: String, val onCheck: (id: String, checked: Boolean) -> Unit)
data class CategoryExpandableModel(val name: String, val products: ArrayList<ProductModel>)

class CategoryExpandableViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private var expanded = false
    private val constraintSet = ConstraintSet()
    private val layoutCategoryExpandable = view.findViewById<ConstraintLayout>(R.id.layoutCategoryExpandable)
    private val textName = view.findViewById<TextView>(R.id.textCategoryName)
    private val imageExpand = view.findViewById<ImageView>(R.id.imageExpand)
    private val layoutItemsContainer = view.findViewById<LinearLayout>(R.id.layoutItemsContainer)

    fun bind(categoryExpandable: CategoryExpandableModel) = with(categoryExpandable) {
        textName.text = name
        layoutItemsContainer.removeAllViews()
        products.forEach { layoutItemsContainer.addView(generateCheckBox(it)) }
        imageExpand.setOnClickListener {
            if (expanded) {
                collapse()
            } else {
                expand()
            }
        }
    }

    private fun generateCheckBox(product: ProductModel): CheckBox {
        val checkBox = CheckBox(view.context)
        checkBox.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val padding = (view.context.resources.displayMetrics.density * 20 + 0.5f).toInt()
        checkBox.setPadding(padding, padding, padding, padding)
        checkBox.text = product.name
        checkBox.setOnCheckedChangeListener { _, checked ->
            product.onCheck(product.id, checked)
        }
        return checkBox
    }

    private fun expand() {
        constraintSet.clone(view.context, R.layout.product_category_expandable_expanded)
        TransitionManager.beginDelayedTransition(layoutCategoryExpandable)
        constraintSet.applyTo(layoutCategoryExpandable)
        imageExpand.rotate()
        expanded = true
    }

    private fun collapse() {
        constraintSet.clone(view.context, R.layout.product_category_expandable)
        TransitionManager.beginDelayedTransition(layoutCategoryExpandable)
        constraintSet.applyTo(layoutCategoryExpandable)
        imageExpand.rotate(true)
        expanded = false
    }

}

class CategoryExpandableAdapter : RecyclerView.Adapter<CategoryExpandableViewHolder>() {

    private val categories = ArrayList<CategoryExpandableModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryExpandableViewHolder(parent.inflate(R.layout.product_category_expandable))

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryExpandableViewHolder, position: Int) = holder.bind(categories[position])

    fun add(category: CategoryExpandableModel) {
        val position = categories.size
        categories.add(category)
        notifyItemInserted(position)
    }

    fun clear() {
        categories.clear()
        notifyDataSetChanged()
    }

    fun isEmpty() = categories.size == 0

}