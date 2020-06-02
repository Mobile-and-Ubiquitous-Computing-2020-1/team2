package org.tensorflow.lite.examples.posenet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*


class LoginActivity : Activity() {
    private val TAG = LoginActivity::class.java.simpleName
    private var usersRef = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var valueEventListener: ValueEventListener
    private var users: ArrayList<User> = ArrayList()
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(applicationContext)
        setContentView(R.layout.activity_login)

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usersType: GenericTypeIndicator<ArrayList<User>> =
                    object : GenericTypeIndicator<ArrayList<User>>() {}
                users = dataSnapshot.getValue(usersType) as ArrayList<User>
                for (user in users) {
                    Log.d(TAG, user.id.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    databaseError.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        usersRef.addValueEventListener(valueEventListener)
    }

    fun onLogin(view: View) {
        val login = findViewById<View>(R.id.login) as EditText
        val loginId = login.text.toString()
        Log.d(TAG, loginId)
        for (user in users) {
            if (user.id.equals(loginId)) {
                currentUser = user
            }
        }
        if (!::currentUser.isInitialized) {
            currentUser = User(loginId)
            users.add(currentUser)
            usersRef.setValue(users)
        }
        Log.d(TAG, "User id=" + currentUser.id.toString() + " is logged in.")
        var intent = Intent(this, RankingActivity::class.java)
        intent.putExtra("users", users)
        startActivity(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        usersRef.removeEventListener(valueEventListener)
    }
}