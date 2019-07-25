package com.akitektuo.smartlist2.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.akitektuo.smartlist2.R
import com.akitektuo.smartlist2.adapter.list.SimpleAdapter
import kotlinx.android.synthetic.main.activity_currency.*

class CurrencyActivity : ThemeActivity() {

    private val adapter = SimpleAdapter() //TODO change this
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        setupClicks()
        setupList()
        setupSearch()
    }

    private fun setupClicks() {
        imageBack.setOnClickListener {
            finish()
        }
        imageCancelSearch.setOnClickListener {
            editSearch.setText("")
        }
    }

    private fun setupList() {
        listCurrencies.layoutManager = LinearLayoutManager(this)
        listCurrencies.adapter = adapter
    }

    private fun setupSearch() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                val searchText = text.toString()
                if (this@CurrencyActivity.searchText != searchText) {
                    repopulateCategories()
                    this@CurrencyActivity.searchText = searchText
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun repopulateCategories() {
        //textNoResult.visibility = VISIBLE
        adapter.clear()
        adapter.add("Romanian Leu")
        adapter.add("Euro")
        adapter.add("United States Dollar")
        adapter.add("Pound Sterling")
    }

    override fun onResume() {
        super.onResume()
        repopulateCategories()
    }

    override fun finish() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
        super.finish()
    }
}
