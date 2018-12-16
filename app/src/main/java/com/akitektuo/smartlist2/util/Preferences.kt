package com.akitektuo.smartlist2.util

import android.content.Context
import com.akitektuo.smartlist2.util.Constants.Companion.KEY_INIT
import com.akitektuo.smartlist2.util.Constants.Companion.NOT_SET

class Preferences(val context: Context) {

    private val sharedPreferences = context.getSharedPreferences(KEY_INIT, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun set(key: String, int: Int) {
        editor.putInt(key, int)
        editor.commit()
    }

    fun set(key: String, long: Long) {
        editor.putLong(key, long)
        editor.commit()
    }

    fun getInt(key: String) = sharedPreferences.getInt(key, NOT_SET)

    fun getLong(key: String) = sharedPreferences.getLong(key, NOT_SET.toLong())

}