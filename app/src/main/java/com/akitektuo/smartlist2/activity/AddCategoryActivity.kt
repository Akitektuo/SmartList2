package com.akitektuo.smartlist2.activity

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.View
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.CategoryExpandableAdapter
import com.akitektuo.smartlist2.adapter.list.CategoryExpandableModel
import com.akitektuo.smartlist2.adapter.list.ProductModel
import com.akitektuo.smartlist2.server.Database
import com.akitektuo.smartlist2.util.hideKeyboard
import com.akitektuo.smartlist2.util.showKeyboard
import com.akitektuo.smartlist2.util.toast
import kotlinx.android.synthetic.main.activity_add_category.*

data class CategoryWithProducts(val category: Database.Category, val products: ArrayList<Database.Product>)

class AddCategoryActivity : ThemeActivity() {

    private val constraintSet = ConstraintSet()
    private val adapter = CategoryExpandableAdapter()
    private var searchText = ""
    private val selectedProductIds = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        setupClicks()
        setupSearch()
        setupList()
    }

    override fun onResume() {
        super.onResume()
        repopulateCategories()
    }

    private fun setupClicks() {
        imageBack.setOnClickListener {
            finish()
        }
        imageSave.setOnClickListener {
            saveCategory()
        }
        imageSearch.setOnClickListener {
            showSearch()
            editSearch.requestFocus()
            editSearch.showKeyboard(this)
        }
        imageCancelSearch.setOnClickListener {
            handleCancelSearch()
        }
    }

    private fun saveCategory() {
        val categoryName = editCategoryName.text.toString().trim()
        if (categoryName.isEmpty()) {
            toast("The category must have a name")
            return
        }
        val category = database.setCategory(Database.Category(database.getCurrentUserId(), categoryName))
        selectedProductIds.forEach { id ->
            database.editProduct(id) {
                it.categoryId = category.id
                it
            }
        }
        finish()
    }

    private fun setupSearch() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                val searchText = text.toString()
                if (this@AddCategoryActivity.searchText != searchText) {
                    repopulateCategories()
                    this@AddCategoryActivity.searchText = searchText
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun setupList() {
        listCategories.layoutManager = LinearLayoutManager(this)
        listCategories.adapter = adapter
    }

    private fun repopulateCategories() {
        database.searchCategoriesAndProducts(editSearch.text.toString()) { categoriesWithProducts ->
            adapter.clear()
            categoriesWithProducts.forEach { categoryWithProduct ->
                val products = ArrayList<ProductModel>()
                categoryWithProduct.products.forEach {
                    products.add(ProductModel(it.id, it.name) { id, checked ->
                        if (checked) {
                            selectedProductIds.add(id)
                        } else {
                            selectedProductIds.remove(id)
                        }
                    })
                }
                adapter.add(CategoryExpandableModel(categoryWithProduct.category.name, products))
            }
            if (adapter.empty()) {
                textNoResult.visibility = View.VISIBLE
            } else {
                textNoResult.visibility = View.GONE
            }
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
        constraintSet.clone(this, R.layout.activity_add_category_search)
        TransitionManager.beginDelayedTransition(layoutAddCategory)
        constraintSet.applyTo(layoutAddCategory)
    }

    private fun hideSearch() {
        constraintSet.clone(this, R.layout.activity_add_category)
        TransitionManager.beginDelayedTransition(layoutAddCategory)
        constraintSet.applyTo(layoutAddCategory)
    }
}
