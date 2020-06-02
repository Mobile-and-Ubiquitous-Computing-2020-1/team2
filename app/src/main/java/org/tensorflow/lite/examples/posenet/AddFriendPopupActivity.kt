package org.tensorflow.lite.examples.posenet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView

class AddFriendPopupActivity : Activity() {
    private lateinit var txtText : TextView
    private lateinit var editText : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Delete TitleBar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_layout)

        // Instantiate UI instance
        txtText  = findViewById<View>(R.id.txtText) as TextView
        editText = findViewById<View>(R.id.friendID) as EditText
    }

    // On Click "OK"
    fun mOnClose(v: View?) {
        val intent = Intent()
        intent.putExtra("result", editText.text.toString())
        setResult(RESULT_OK, intent)
        // Close Popup Activity
        finish()
    }

    // Prevent click outside of popup
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return event.action != MotionEvent.ACTION_OUTSIDE
    }

    // Prevent Back button
    override fun onBackPressed() {
        return
    }
}