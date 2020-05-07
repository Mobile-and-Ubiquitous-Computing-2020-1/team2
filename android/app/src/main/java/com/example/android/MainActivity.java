package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LoginActivity.isLoggedIn()) {
            startActivity(new Intent(this, RankActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
