package com.akitektuo.smartlist2.adapter.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.util.inflate

data class OptionModel(val name: String, val onClick: () -> Unit)

class OptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textOption = view.findViewById<TextView>(R.id.textOption)

    fun bind(context: Context, option: OptionModel) = with(option) {
        textOption.text = name
        view.setOnClickListener {
            onClick()
        }
    }
}

class OptionAdapter : RecyclerView.Adapter<OptionViewHolder>() {

    private val options = ArrayList<OptionModel>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        context = parent.context
        return OptionViewHolder(parent.inflate(R.layout.item_option))
    }

    override fun getItemCount() = options.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) = holder.bind(context, options[position])

    fun add(option: OptionModel) {
        val position = options.size
        options.add(option)
        notifyItemInserted(position)
    }

    fun clear() {
        options.clear()
        notifyDataSetChanged()
    }

}