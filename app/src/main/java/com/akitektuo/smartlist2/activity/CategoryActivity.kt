package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.SmartList.Companion.database
import com.akitektuo.smartlist2.adapter.list.SimpleAdapter
import com.akitektuo.smartlist2.util.Constants
import com.akitektuo.smartlist2.util.Constants.Companion.INTENT_ID
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : ThemeActivity() {

    private val adapter = SimpleAdapter()
    private var categoryId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        setupList()
        setupClicks()
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
        database.getProducts(categoryId) { products ->
            adapter.clear()
            products.sortedBy { it.name }.forEach {
                adapter.add(it.name)
            }
            if (adapter.isEmpty()) {
                textNoProducts.visibility = View.VISIBLE
            } else {
                textNoProducts.visibility = View.GONE
            }
        }
    }

    private fun setupClicks() {
        imageBack.setOnClickListener {
            finish()
        }
        imageEdit.setOnClickListener {
            val intent = Intent(this, EditCategoryActivity::class.java)
            intent.putExtra(Constants.INTENT_ID, categoryId)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
    }
}
