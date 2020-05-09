package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    public static User currentUser;
    private final List<User> users = new LinkedList<>();

    public static boolean isLoggedIn() {
        return LoginActivity.currentUser != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        setContentView(R.layout.activity_login);

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
                currentUser = user;
            }
        }
        if (currentUser == null) {
            currentUser = new User(loginId);
            users.add(currentUser);
            usersRef.setValue(users);
        }
        Log.d(TAG, "User id=" + currentUser.id + " is logged in.");
        startActivity(new Intent(this, RankActivity.class));
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        usersRef.removeEventListener(usersListener);
    }
}
