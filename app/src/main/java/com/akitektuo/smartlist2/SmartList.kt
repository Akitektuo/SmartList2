package com.akitektuo.smartlist2

import android.app.Application
import com.akitektuo.smartlist2.server.Database
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 15:49.
 */
class SmartList : Application() {

    companion object {
        lateinit var database: Database
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        database = Database()
    }

}