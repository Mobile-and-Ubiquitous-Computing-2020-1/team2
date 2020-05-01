package com.example.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RankActivity extends AppCompatActivity {
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

    }
}
