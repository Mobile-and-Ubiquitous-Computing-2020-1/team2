package com.example.android;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ScrollView;

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
