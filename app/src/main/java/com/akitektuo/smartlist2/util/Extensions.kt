package com.akitektuo.smartlist2.util

import android.content.Context
import android.widget.Toast

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 17:25.
 */
fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}