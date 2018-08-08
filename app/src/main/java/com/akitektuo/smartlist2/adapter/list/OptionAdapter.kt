package com.akitektuo.smartlist2.adapter.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.akitektuo.smartlist2.R

data class OptionModel(val name: String, val onClick: () -> Unit)

class OptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textOption = view.findViewById<TextView>(R.id.textOption)

    fun bind(option: OptionModel) = with(option) {
        textOption.text = name
        view.setOnClickListener {
            onClick()
        }
    }
}

class OptionAdaper : RecyclerView.Adapter<OptionViewHolder>() {

    private val options = ArrayList<OptionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OptionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_option, parent, false))

    override fun getItemCount() = options.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) = holder.bind(options[position])

    fun add(option: OptionModel) {
        val position = options.size
        options.add(option)
        notifyItemInserted(position)
    }

}