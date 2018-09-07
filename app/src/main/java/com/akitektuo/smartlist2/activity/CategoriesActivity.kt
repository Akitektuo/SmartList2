package com.akitektuo.smartlist2.activity

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.CategoryAdapter
import com.akitektuo.smartlist2.adapter.list.CategoryModel
import com.akitektuo.smartlist2.util.hideKeyboard
import com.akitektuo.smartlist2.util.showKeyboard
import com.akitektuo.smartlist2.util.toast
import kotlinx.android.synthetic.main.activity_categories.*

class CategoriesActivity : ThemeActivity() {

    private val constraintSet = ConstraintSet()
    private val adapter = CategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        imageBack.setOnClickListener {
            finish()
        }
        imageSearch.setOnClickListener {
            showSearch()
            editSearch.requestFocus()
            editSearch.showKeyboard(this)
        }
        imageCancelSearch.setOnClickListener {
            if (editSearch.text.toString().isEmpty()) {
                hideSearch()
                editSearch.clearFocus()
                hideKeyboard()
            } else {
                editSearch.setText("")
            }
        }
        listCategories.layoutManager = LinearLayoutManager(this)
        listCategories.adapter = adapter
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                repopulateCategories()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    override fun setupWithTheme(isLight: Boolean) {
        super.setupWithTheme(isLight)
        repopulateCategories()
    }

    override fun refreshActivity() {
        super.refreshActivity()
        repopulateCategories()
    }

    private fun repopulateCategories() {
        adapter.clear()
        database.searchCategories(editSearch.text.toString()) { categories ->
            categories.forEach { category ->
                database.getProducts(category.id) {
                    adapter.add(CategoryModel(category.name, it.size) {
                        toast("Soon")
                    })
                }
            }
        }
    }

    override fun useLightTheme() {
        super.useLightTheme()
        val colorBlack = ContextCompat.getColor(this, R.color.black)
        val colorAccent = ContextCompat.getColor(this, R.color.light_accent)

        layoutCategory.setBackgroundResource(R.color.white)
        textCategories.setTextColor(colorBlack)
        imageBack.setImageResource(R.drawable.light_back)
        imageCancelSearch.setImageResource(R.drawable.ic_light_cancel)
        editSearch.setPrimaryColor(colorAccent)
        editSearch.setMetTextColor(colorBlack)
        imageAdd.setImageResource(R.drawable.light_add)
        imageSearch.setImageResource(R.drawable.ic_light_search)
    }

    override fun useDarkTheme() {
        super.useDarkTheme()
        val colorWhite = ContextCompat.getColor(this, R.color.white)
        val colorAccent = ContextCompat.getColor(this, R.color.dark_accent)

        layoutCategory.setBackgroundResource(R.color.black)
        textCategories.setTextColor(colorWhite)
        imageBack.setImageResource(R.drawable.dark_back)
        imageCancelSearch.setImageResource(R.drawable.ic_dark_cancel)
        editSearch.setPrimaryColor(colorAccent)
        editSearch.setMetTextColor(colorWhite)
        imageAdd.setImageResource(R.drawable.dark_add)
        imageSearch.setImageResource(R.drawable.ic_dark_search)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
    }

    private fun showSearch() {
        constraintSet.clone(this, R.layout.activity_categories_search)
        TransitionManager.beginDelayedTransition(layoutCategory)
        constraintSet.applyTo(layoutCategory)
    }

    private fun hideSearch() {
        constraintSet.clone(this, R.layout.activity_categories)
        TransitionManager.beginDelayedTransition(layoutCategory)
        constraintSet.applyTo(layoutCategory)
    }
}
