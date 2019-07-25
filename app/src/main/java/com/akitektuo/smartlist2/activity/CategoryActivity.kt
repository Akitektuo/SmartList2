package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.SimpleAdapter
import com.akitektuo.smartlist2.util.Constants.Companion.INTENT_ID
import com.akitektuo.smartlist2.util.hideKeyboard
import com.akitektuo.smartlist2.util.showKeyboard
import com.akitektuo.smartlist2.util.toast
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : ThemeActivity() {

    private val adapter = SimpleAdapter()
    private var categoryId = ""
    private val constraintSet = ConstraintSet()
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        setupList()
        setupClicks()
        setupSearch()
    }

    private fun setupSearch() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                val searchText = text.toString()
                if (this@CategoryActivity.searchText != searchText) {
                    repopulateProducts()
                    this@CategoryActivity.searchText = searchText
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
        repopulateProducts()
    }

    private fun loadData() {
        categoryId = intent?.extras?.getString(INTENT_ID) ?: ""
        database.getCategory(categoryId) {
            textCategoryName.text = it.name
            if (it.name.isEmpty()) {
                finish()
            }
        }
    }

    private fun setupList() {
        listProducts.layoutManager = LinearLayoutManager(this)
        listProducts.adapter = adapter
    }

    private fun repopulateProducts() {
        database.searchProducts(categoryId, editSearch.text.toString()) { products ->
            adapter.clear()
            products.sortedBy { it.name }.forEach {
                adapter.add(it.name)
            }
            showError()
        }
    }

    private fun showError() {
        if (adapter.isEmpty()) {
            textNoProducts.visibility = View.VISIBLE
            textNoProducts.text = getString(R.string.no_products, if (editSearch.text.isNullOrBlank()) {
                ""
            } else {
                "for \"${editSearch.text.toString()}\""
            })
        } else {
            textNoProducts.visibility = View.GONE
        }
    }

    private fun setupClicks() {
        imageBack.setOnClickListener {
            finish()
        }
        imageEdit.setOnClickListener {
            val intent = Intent(this, EditCategoryActivity::class.java)
            intent.putExtra(INTENT_ID, categoryId)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        }
        imageSearch.setOnClickListener {
            showSearch()
            editSearch.requestFocus()
            editSearch.showKeyboard(this)
        }
        imageCancelSearch.setOnClickListener {
            handleCancelSearch()
        }
        imageAdd.setOnClickListener {
            toast("Add soon")
        }
    }

    private fun handleCancelSearch() {
        if (editSearch.text.toString().isEmpty()) {
            hideSearch()
            editSearch.clearFocus()
            hideKeyboard()
        } else {
            editSearch.setText("")
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
    }

    private fun showSearch() {
        constraintSet.clone(this, R.layout.activity_category_search)
        TransitionManager.beginDelayedTransition(layoutCategory)
        constraintSet.applyTo(layoutCategory)
        showError()
    }

    private fun hideSearch() {
        constraintSet.clone(this, R.layout.activity_category)
        TransitionManager.beginDelayedTransition(layoutCategory)
        constraintSet.applyTo(layoutCategory)
        showError()
    }
}
