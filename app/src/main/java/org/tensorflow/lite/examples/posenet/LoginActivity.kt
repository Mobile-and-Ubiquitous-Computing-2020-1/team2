package org.tensorflow.lite.examples.posenet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText


class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLogin(view: View) {
        var intent = Intent(this, RankingActivity::class.java)
        val login = findViewById<View>(R.id.login) as EditText
        intent.putExtra("currentUserName", login.text.toString())
        startActivity(intent)
        finish()
    }
}