package com.akitektuo.smartlist2.server

import com.akitektuo.smartlist2.activity.CategoryWithProducts
import com.akitektuo.smartlist2.util.Constants.Companion.MODE_ADAPTIVE
import com.akitektuo.smartlist2.util.Themes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 16:28.
 */
class Database {

    val auth = FirebaseAuth.getInstance()
    val theme = Themes()
    private val database = FirebaseDatabase.getInstance().reference
    private var errorHandler: ((String) -> Unit)? = null

    init {
        database.keepSynced(true)
    }

    data class User(
            val name: String = "",
            val email: String = "",
            var mode: Int = MODE_ADAPTIVE,
            var lightStart: Long = 25200000,
            var darkStart: Long = 75600000,
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
            val write: Boolean = false,
            val id: String = ""
    )

    data class List(
            val name: String = "",
            val timestamp: Long = 0,
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
            var categoryId: String = "",
            var id: String = ""
    )

    data class Prices(
            val price: Double = 0.0,
            val currency: String = "",
            val productId: String = "",
            val appearances: Long = 0,
            val id: String = ""
    )

    data class Category(
            val userId: String = "",
            val name: String = "",
            var id: String = ""
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

    fun getCurrentUserId() = auth.currentUser?.uid!!

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
            result(users.none { it.id == auth.currentUser?.uid!! })
        }
    }

    fun createUser() {
        setUser(User(auth.currentUser?.displayName!!, auth.currentUser?.email!!, id = auth.currentUser?.uid!!))
        setCategory(Category(auth.currentUser?.uid!!, "Other"))
    }

    fun setUser(user: User): User {
        if (user.id == "") {
            user.id = databaseUsers.push().key.toString()
        }
        databaseUsers.child(user.id).setValue(user)
        return user
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

    fun getCurrentUser(result: (User) -> Unit) {
        getUser(auth.currentUser?.uid!!, result)
    }

    fun editCurrentUser(edit: (User) -> User) {
        getCurrentUser {
            setUser(edit(it))
        }
    }

    fun setCategory(category: Category): Category {
        if (category.id == "") {
            category.id = databaseCategories.push().key.toString()
        }
        databaseCategories.child(category.id).setValue(category)
        return category
    }

    fun setProduct(product: Product): Product {
        if (product.id == "") {
            product.id = databaseProducts.push().key.toString()
        }
        databaseProducts.child(product.id).setValue(product)
        return product
    }

    fun searchCategories(search: String, result: (ArrayList<Category>) -> Unit) {
        databaseCategories.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                val categories = ArrayList<Category>()
                data.children.mapNotNullTo(categories) {
                    it.getValue(Category::class.java)
                }
                result(categories.filter { it.userId == auth.currentUser?.uid && it.name.contains(search, true) } as ArrayList<Category>)
            }
        })
    }

    fun getCategories(result: (ArrayList<Category>) -> Unit) {
        databaseCategories.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                val categories = ArrayList<Category>()
                data.children.mapNotNullTo(categories) {
                    it.getValue(Category::class.java)
                }
                result(categories.filter { it.userId == auth.currentUser?.uid } as ArrayList<Category>)
            }
        })
    }

    fun getProducts(result: (ArrayList<Product>) -> Unit) {
        databaseProducts.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                val products = ArrayList<Product>()
                data.children.mapNotNullTo(products) {
                    it.getValue(Product::class.java)
                }
                result(products.filter { it.userId == auth.currentUser?.uid } as ArrayList<Product>)
            }
        })
    }

    fun getProducts(categoryId: String, result: (ArrayList<Product>) -> Unit) {
        databaseProducts.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                val products = ArrayList<Product>()
                data.children.mapNotNullTo(products) {
                    it.getValue(Product::class.java)
                }
                result(products.filter { it.userId == auth.currentUser?.uid && it.categoryId == categoryId } as ArrayList<Product>)
            }
        })
    }

    fun getProduct(id: String, result: (Product) -> Unit) {
        databaseProducts.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                result(data.getValue(Product::class.java) ?: Product())
            }
        })
    }

    fun searchCategoriesAndProducts(search: String, result: (ArrayList<CategoryWithProducts>) -> Unit) {
        getCategories { categories ->
            getProducts { products ->
                val categoriesWithItems = ArrayList<CategoryWithProducts>()
                val filteredProducts = (products.filter { it.name.contains(search, true) } as ArrayList<Product>)
                categories.filter { category ->
                    (category.name.contains(search, true) ||
                            filteredProducts.any { it.categoryId == category.id }) &&
                            products.any { it.categoryId == category.id }
                }.forEach { category ->
                    if (filteredProducts.none { it.categoryId == category.id }) {
                        categoriesWithItems.add(CategoryWithProducts(category, products.filter { it.categoryId == category.id } as ArrayList<Product>))
                    } else {
                        categoriesWithItems.add(CategoryWithProducts(category, filteredProducts.filter { it.categoryId == category.id } as ArrayList<Product>))
                    }
                }
                result(categoriesWithItems)
            }
        }
    }

    fun editProduct(id: String, edit: (Product) -> Product) {
        getProduct(id) {
            setProduct(edit(it))
        }
    }

}