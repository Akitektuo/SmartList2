package com.akitektuo.smartlist2.server

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 16:28.
 */
class Database {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid ?: ""
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
            val id: String = ""
    )

    data class UserList(
            val userId: String = "",
            val listId: String = "",
            val type: Int = 0,
            val id: String = ""
    )

    data class List(
            val name: String = "",
            val id: String = ""
    )

    data class Connection(
            val fromListId: String = "",
            val toListId: String = "",
            val id: String = ""
    )

    data class Condition(
            val connectionId: String = "",
            val categoryId: String = "",
            val type: Int = 0,
            val id: String = ""
    )

    data class ListItem(
            val listId: String = "",
            val name: String = "",
            val price: Double = 0.0,
            val currency: String = "",
            val categoryId: String = "",
            val timestamp: Long = 0,
            val id: String = ""
    )

    data class Product(
            val userId: String = "",
            val name: String = "",
            val categoryId: String = "",
            val id: String = ""
    )

    data class Prices(
            val price: Double = 0.0,
            val currency: String = "",
            val productId: String = "",
            val appearances: Long = 0,
            val id: String = ""
    )

    data class Category(
            val name: String = "",
            val id: String = ""
    )

    data class UserListExcel(
            val userId: String = "",
            val excelId: String = "",
            val type: Int = 0,
            val id: String = ""
    )

    data class Excel(
            val listId: String = "",
            val timestamp: Long = 0,
            val products: Int = 0,
            val id: String = ""
    )

    private val databaseUsers = database.child("Users")

    fun isUserSignedIn() = auth.currentUser != null

    fun isNewUser(afterResult: (Boolean) -> Unit) {
        databaseUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(data: DataSnapshot) {
                val users = ArrayList<User>()
                data.children.mapNotNullTo(users, {
                    it.getValue<User>(User::class.java)
                })
                afterResult(users.none { it.id == currentUserId })
            }
        })
    }
}