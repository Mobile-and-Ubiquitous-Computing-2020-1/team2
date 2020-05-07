package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
    private ValueEventListener usersListener;

    private User appUser = new User();
    private final List<User> users = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        setContentView(R.layout.login);

        usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<User>> usersType = new GenericTypeIndicator<List<User>>() {};
                users.addAll(Objects.requireNonNull(dataSnapshot.getValue(usersType)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        usersRef.addValueEventListener(usersListener);
    }

    public void onLogin(View view) {
        EditText login = (EditText)findViewById(R.id.login);
        String loginId = login.getText().toString();
        Log.d(TAG, loginId);

        for (User user : users) {
            if (user.id.equals(loginId)) {
                appUser = user;
            }
        }
        if (!loginId.equals(appUser.id)) {
            appUser = new User(loginId);
            users.add(appUser);
            usersRef.setValue(users);
        }
        login();
    }

    private void login() {
        Log.d(TAG, appUser.id);
        for (String friendId : appUser.friends_id) {
            Log.d(TAG, friendId);
        }
        for (Integer score : appUser.scores) {
            Log.d(TAG, score.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        usersRef.removeEventListener(usersListener);
    }
}
