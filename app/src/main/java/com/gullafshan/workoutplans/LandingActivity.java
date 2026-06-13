package com.gullafshan.workoutplans;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnStartWorkout;
    private Button btnLogout;
    // --- FIX: Uncommented the buttons for API Data and Notes ---
    private Button btnApiData;
    private Button btnNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // --- FIX: Find the new buttons by their ID from your XML ---
        tvWelcome = findViewById(R.id.tvWelcome);
        btnStartWorkout = findViewById(R.id.btnStartWorkout);
        btnLogout = findViewById(R.id.btnLogout);
        btnApiData = findViewById(R.id.btnApiData); // Make sure this ID exists in activity_landing.xml
        btnNotes = findViewById(R.id.btnNotes);     // Make sure this ID exists in activity_landing.xml

        // Welcome message
        tvWelcome.setText("Welcome!");

        // Start Workout button
        if (btnStartWorkout != null) {
            btnStartWorkout.setOnClickListener(v -> {
                // Navigate to the workout timer screen
                Intent intent = new Intent(LandingActivity.this, WorkoutTimerActivity.class);
                startActivity(intent);
            });
        }

        // --- FIX: Added OnClickListener for API Data button ---
        if (btnApiData != null) {
            btnApiData.setOnClickListener(v -> {
                // Navigate to the ApiDataActivity
                Intent intent = new Intent(LandingActivity.this, ApiDataActivity.class);
                startActivity(intent);
            });
        }

        // --- FIX: Added OnClickListener for Notes button ---
        if (btnNotes != null) {
            btnNotes.setOnClickListener(v -> {
                // Navigate to the NotesActivity
                Intent intent = new Intent(LandingActivity.this, NotesActivity.class);
                startActivity(intent);
            });
        }

        // Logout button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                // Navigate back to the Login screen
                Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Close the LandingActivity
            });
        }
    }
}
