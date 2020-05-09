package com.example.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddFriendPopupActivity extends Activity {
    TextView txtText;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_layout);

        //UI 객체생성
        txtText = (TextView)findViewById(R.id.txtText);
        editText = (EditText)findViewById(R.id.friendID);
        //데이터 가져오기
        //Intent intent = getIntent();
        //String data = intent.getStringExtra("data");
        //txtText.setText(data);
    }

    //확인 버튼 클릭
    public void mOnClose(View v){

        String friendID_str = editText.getText().toString();
        //데이터 전달하기
        //intent로 stringd을 전달해서 rankactivity에서 파이어베이스에 데이터를 넣거나 아니면 바로 여기서 넣거나.
        Intent intent = new Intent();
        intent.putExtra("result", friendID_str);
        setResult(RESULT_OK, intent);


        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
