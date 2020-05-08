package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int MY_PERMISSION_REQUEST_CAMERA = 1234;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestCameraPermission();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "requestCameraPermission: Request camera permission.");
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA},
                    MY_PERMISSION_REQUEST_CAMERA);
        } else {
            Log.d(TAG, "requestCameraPermission: Camera permission is granted.");
            redirect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionResult");
        if (requestCode == MY_PERMISSION_REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "requestCameraPermission: Camera permission is granted.");
            redirect();
        } else {
            finish();
            System.exit(0);
        }
    }

    private void redirect() {
        if (LoginActivity.isLoggedIn()) {
            startActivity(new Intent(this, RankActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
