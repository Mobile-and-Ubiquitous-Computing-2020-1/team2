package com.example.android;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RankActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private RecyclerViewAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecyclerView view = (RecyclerView) findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new RecyclerViewAdapter();
        view.setLayoutManager(layoutManager);
        view.setAdapter(recyclerViewAdapter);
        updateScore();

        scrollView = (ScrollView) findViewById(R.id.ranking_scrollbar);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        layoutTheView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        layoutTheView();
    }

    private void layoutTheView() {
        ActionBar actionBar = this.getSupportActionBar();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) scrollView.getLayoutParams();
        int actionBarHeight = actionBar.getHeight();
        params.setMargins(20, actionBarHeight + 30, 20, 0);
        scrollView.setLayoutParams(params);
    }

    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, AddFriendPopupActivity.class);
        //intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);
    }

    public void onStartButtonClcik(View v){
        //Intent intent = new Intent(this, AddFriendPopupActivity.class);
        //startActivity(intent);
    }

    public void updateScore(){
        recyclerViewAdapter.memberDTOs.clear();
        recyclerViewAdapter.memberDTOs.add(new MemberDTO(R.drawable.testimage, 1, "김지수", "100"));
        recyclerViewAdapter.memberDTOs.add(new MemberDTO(R.drawable.testimage2, 2, "누군가1", "50"));
        recyclerViewAdapter.memberDTOs.add(new MemberDTO(R.drawable.testimage3, 3, "누군자2", "30"));
        recyclerViewAdapter.memberDTOs.add(new MemberDTO(R.drawable.testimage4, 4, "누군가3", "10"));

        // According to sorted users by score, put the data into memberDTOs sequentially
        // Current user shown first and then Top 5 user shown
        /***
        this code should be implemented as iteration form to get all user's data
        mDatabase.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.getValue(User.class) != null){
                    User post = dataSnapshot.getValue(User.class);
                    memberDTOs.add(new MemberDTO(....)); // need to change MemberDTO's member variables
                } else {
                    Toast.makeText(MainActivity.this, "데이터 없음...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
         ***/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String test = data.getStringExtra("result");

                Log.d("TAG", test);
            }
        }
    }

}
