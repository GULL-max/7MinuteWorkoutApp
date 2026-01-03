package com.gullafshan.workoutplans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity
 * - Shows splash for 2 seconds
 * - Checks login state
 * - Redirects to Login or Landing accordingly
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 2000L;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Runnable navigateRunnable = () -> {

        // SharedPreferences to check login state
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        Intent intent;
        if (isLoggedIn) {
            // User already logged in
            intent = new Intent(SplashActivity.this, LandingActivity.class);
        } else {
            // User not logged in
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        // Clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide ActionBar for full-screen splash
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mainHandler.postDelayed(navigateRunnable, SPLASH_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        mainHandler.removeCallbacks(navigateRunnable);
        super.onDestroy();
    }
}


