package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Calendar;

public class WorkoutActivity extends AppCompatActivity {
    private static final String TAG = WorkoutActivity.class.getSimpleName();
    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate savedInstanceState: " + savedInstanceState);

        // Set layout
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide the title
//        getSupportActionBar().hide(); // Hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // Enable full screen
        setContentView(R.layout.activity_workout);

        // Start camera preview
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        // Process frame images
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            long prevTimeInMillies = 0;
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {

                // TODO: Put "data" into the model and set "count"
                int count = (int) prevTimeInMillies;
                setCountText(count);

                Log.d(TAG, "onPreviewFrame: data length: " + data.length
                        + ", time interval: " + (Calendar.getInstance().getTimeInMillis() - prevTimeInMillies) + "ms");
                prevTimeInMillies = Calendar.getInstance().getTimeInMillis();
            }
    });
}

    private void setCountText(int countText) {
        TextView countTextView = findViewById(R.id.workout_count);
        countTextView.setText(Integer.toString(countText));
    }

    public void onStopButtonClick(View v) {
        finish();
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, "getCameraInstance: No available camera");
        }
        return c;
    }
}
