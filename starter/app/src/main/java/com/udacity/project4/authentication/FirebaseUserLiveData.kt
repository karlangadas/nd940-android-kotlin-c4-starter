package com.udacity.project4.authentication


import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.LiveData

/**
 * Extracted from:
 * https://github.com/udacity/android-kotlin-login/blob/master/app/src/main/java/com/example/android/firebaseui_login_sample/FirebaseUserLiveData.kt
 * This class observes the current FirebaseUser. If there is no logged in user, FirebaseUser will
 * be null.
 *
 * Note that onActive() and onInactive() will get triggered when the configuration changes (for
 * example when the device is rotated). This may be undesirable or expensive depending on the
 * nature of your LiveData object, but is okay for this purpose since we are only adding and
 * removing the authStateListener.
 */
class FirebaseUserLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Use the FirebaseAuth instance instantiated at the beginning of the class to get an entry
    // point into the Firebase Authentication SDK the app is using.
    // With an instance of the FirebaseAuth class, you can now query for the current user.
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
    }

    // When this object has an active observer, start observing the FirebaseAuth state to see if
    // there is currently a logged in user.
    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    // When this object no longer has an active observer, stop observing the FirebaseAuth state to
    // prevent memory leaks.
    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}