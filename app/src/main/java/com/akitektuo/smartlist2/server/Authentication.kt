package com.akitektuo.smartlist2.server

import android.app.Activity
import android.content.Intent
import com.akitektuo.smartlist2.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Created by AoD Akitektuo on 29-Jul-18 at 16:13.
 */
class Authentication(private val activity: Activity, private val onError: (errorMessage: String) -> Unit = {}) {

    companion object {
        private const val REQUEST_CODE = 400
    }

    private val database = Database()

    fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            if (it.isSuccessful) {
                activity.startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE)
            } else {
                error(it.exception?.message)
            }
        }
    }

    fun processResult(requestCode: Int, intent: Intent?, onSuccess: () -> Unit) {
        if (requestCode == REQUEST_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent)
            if (result.isSuccess) {
                val credential = GoogleAuthProvider.getCredential(result.signInAccount?.idToken, null)
                database.auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        database.isNewUser {
                            database.createUser()
                        }
                        onSuccess
                    } else {
                        error(it.exception?.message)
                    }
                }
            } else {
                error(result.status.statusMessage)
            }
        }
    }

    private fun error(errorMessage: String?) = onError(errorMessage
            ?: "Network error, try again later")

}