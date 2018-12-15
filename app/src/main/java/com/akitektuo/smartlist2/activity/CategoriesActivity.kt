package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintSet
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
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        imageBack.setOnClickListener {
            finish()
        }
        imageAdd.setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
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
                val searchText = text.toString()
                if (this@CategoriesActivity.searchText != searchText) {
                    repopulateCategories()
                    this@CategoriesActivity.searchText = searchText
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        repopulateCategories()
    }

    private fun repopulateCategories() {
        database.searchCategories(editSearch.text.toString()) { categories ->
            adapter.clear()
            categories.forEach { category ->
                database.getProducts(category.id) {
                    adapter.add(CategoryModel(category.name, it.size) {
                        toast("Soon")
                    })
                }
            }
        }
    }

    override fun finish() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
        super.finish()
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
