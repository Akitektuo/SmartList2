package com.akitektuo.smartlist2.activity

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.CategoryExpandableAdapter
import com.akitektuo.smartlist2.adapter.list.CategoryExpandableModel
import com.akitektuo.smartlist2.adapter.list.ProductModel
import com.akitektuo.smartlist2.server.Database
import com.akitektuo.smartlist2.util.Constants.Companion.INTENT_ID
import com.akitektuo.smartlist2.util.hideKeyboard
import com.akitektuo.smartlist2.util.showKeyboard
import com.akitektuo.smartlist2.util.toast
import kotlinx.android.synthetic.main.activity_edit_category.*

class EditCategoryActivity : ThemeActivity() {

    private val constraintSet = ConstraintSet()
    private val adapter = CategoryExpandableAdapter()
    private var searchText = ""
    private val selectedProductIds = ArrayList<String>()
    private var categoryId = ""
    private var hideEdit = false
    private val existingCategories = ArrayList<Database.Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        loadData()

        setupClicks()
        setupSearch()
        setupList()
    }

    private fun loadData() {
        categoryId = intent?.extras?.getString(INTENT_ID) ?: ""
        database.getCategory(categoryId) { it ->
            editCategoryName.setText(it.name)
            hideEdit = it.name == "Other"
            if (hideEdit) {
                hideEdit()
            }
        }
        database.getCategories { categories ->
            existingCategories.addAll(categories.filter { it.id != categoryId })
        }
    }

    private fun hideEdit() {
        editCategoryName.visibility = View.GONE
        imageDelete.visibility = View.GONE
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
        imageDelete.setOnClickListener {
            database.deleteCategory(categoryId)
            Handler().postDelayed({
                finish()
            }, 100)
        }
    }

    private fun saveCategory() {
        val categoryName = editCategoryName.text.toString().trim()
        if (categoryName.isEmpty()) {
            toast("The category must have a name")
            return
        }
        if (existingCategories.any { it.name == categoryName }) {
            toast("The category's name is already used")
            return
        }
        database.editCategory(categoryId) {
            it.name = categoryName
            it
        }
        selectedProductIds.forEach { id ->
            database.editProduct(id) {
                it.categoryId = categoryId
                it
            }
        }
        Handler().postDelayed({
            finish()
        }, 100)
    }

    private fun setupSearch() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                val searchText = text.toString()
                if (this@EditCategoryActivity.searchText != searchText) {
                    repopulateCategories()
                    this@EditCategoryActivity.searchText = searchText
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
            categoriesWithProducts.filter { it.category.id != categoryId }.forEach { categoryWithProduct ->
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
            showError()
        }
    }

    private fun showError() {
        if (adapter.isEmpty()) {
            textNoResult.visibility = View.VISIBLE
        } else {
            textNoResult.visibility = View.GONE
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
        constraintSet.clone(this, R.layout.activity_edit_category_search)
        TransitionManager.beginDelayedTransition(layoutEditCategory)
        constraintSet.applyTo(layoutEditCategory)
        if (hideEdit) {
            hideEdit()
        }
        showError()
    }

    private fun hideSearch() {
        constraintSet.clone(this, R.layout.activity_edit_category)
        TransitionManager.beginDelayedTransition(layoutEditCategory)
        constraintSet.applyTo(layoutEditCategory)
        if (hideEdit) {
            hideEdit()
        }
        showError()
    }
}
