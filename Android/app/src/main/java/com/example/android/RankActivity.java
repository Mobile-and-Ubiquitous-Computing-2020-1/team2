package com.example.android;

import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        RecyclerView view = (RecyclerView) findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter();
        view.setLayoutManager(layoutManager);
        view.setAdapter(recyclerViewAdapter);


        scrollView = (ScrollView) findViewById(R.id.ranking_scrollbar);

        ImageButton imageButton = (ImageButton) findViewById(R.id.toolbar_plus_friend);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RankActivity.this, "아직 친구 추가 activity가 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
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
}
