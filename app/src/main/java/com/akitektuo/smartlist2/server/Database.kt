package com.akitektuo.smartlist2.server

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 16:28.
 */
class Database {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    init {
        database.keepSynced(true)
    }

    data class User(
            val name: String = "",
            val email: String = "",
            val mode: Int = 0,
            val recommendations: Boolean = false,
            val fill: Boolean = false,
            val preferredCurrency: String = "eur",
            val graphColumns: Int = 7,
            val id: String = "")

    fun isUserSignedIn() = auth.currentUser != null

}