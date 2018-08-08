package com.akitektuo.smartlist2.server

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 16:28.
 */
class Database {

    val auth = FirebaseAuth.getInstance()
    var currentUser: FirebaseUser? = auth.currentUser
    var currentUserId = currentUser?.uid
    private val database = FirebaseDatabase.getInstance().reference
    private var errorHandler: ((String) -> Unit)? = null

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
            var id: String = ""
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

    data class UserExcel(
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
    private val databaseUsersLists = database.child("UsersLists")
    private val databaseLists = database.child("Lists")
    private val databaseConnections = database.child("Connections")
    private val databaseConditions = database.child("Conditions")
    private val databaseListsItems = database.child("ListsItems")
    private val databaseProducts = database.child("Products")
    private val databasePrices = database.child("Prices")
    private val databaseCategories = database.child("Categories")
    private val databaseUsersExcels = database.child("UsersExcels")
    private val databaseExcels = database.child("Excels")

    fun setOnErrorHandler(onErrorHandler: (message: String) -> Unit) {
        errorHandler = onErrorHandler
    }

    fun onError(error: String) = errorHandler?.invoke(error)

    fun isUserSignedIn() = auth.currentUser != null

    fun signOut() = auth.signOut()

    fun getAllUsers(result: (ArrayList<User>) -> Unit) {
        databaseUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                val users = ArrayList<User>()
                data.children.mapNotNullTo(users) {
                    it.getValue(User::class.java)
                }
                result(users)
            }
        })
    }

    fun isNewUser(result: (Boolean) -> Unit) {
        getAllUsers { users ->
            result(users.none { it.id == currentUserId })
        }
    }

    fun createUser() = addUser(User(currentUser?.displayName!!, currentUser?.email!!, id = currentUserId!!))

    fun addUser(user: User, result: () -> Unit = {}) {
        if (user.id == "") {
            user.id = databaseUsers.push().key.toString()
        }
        databaseUsers.child(user.id).setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                result()
            } else {
                onError(it.exception?.message!!)
            }
        }
    }

    fun refreshUser() {
        currentUser = auth.currentUser
        currentUserId = currentUser?.uid!!
    }

    fun getUser(id: String, result: (User) -> Unit) {
        databaseUsers.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                val user = data.getValue(User::class.java)
                if (user != null) {
                    result(user)
                } else {
                    onError("User is null")
                }
            }
        })
    }
}