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
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_layout)

        //UI 객체생성
        txtText  = findViewById<View>(R.id.txtText) as TextView
        editText = findViewById<View>(R.id.friendID) as EditText
    }

    //확인 버튼 클릭
    fun mOnClose(v: View?) {
        val intent = Intent()
        intent.putExtra("result", editText.getText().toString())
        setResult(RESULT_OK, intent)
        val friendID_str: String = editText.getText().toString()
        //액티비티(팝업) 닫기
        finish()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean { //바깥레이어 클릭시 안닫히게
        return if (event.action == MotionEvent.ACTION_OUTSIDE) {
            false
        } else true
    }

    override fun onBackPressed() { //안드로이드 백버튼 막기
        return
    }
}