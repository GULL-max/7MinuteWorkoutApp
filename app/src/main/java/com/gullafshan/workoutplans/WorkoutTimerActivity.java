package com.gullafshan.workoutplans;

import android.os.Bundle;
import android.os.CountDownTimer;import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale; // <-- Import Locale

public class WorkoutTimerActivity extends AppCompatActivity {

    // Config: change if you like
    private static final int EXERCISE_COUNT = 12;
    private static final long WORK_MS = 30_000L; // 30 seconds
    private static final long REST_MS = 5_000L;  // 5 seconds
    private static final long GET_READY_MS = 5_000L; // optional ready period

    // Exercises (change names as you like)
    private final String[] exercises = {
            "Jumping Jacks", "Wall Sit", "Push-ups", "Abdominal Crunch",
            "Step-up onto Chair", "Squats", "Triceps Dip on Chair", "Plank",
            "High Knees Running", "Lunges", "Push-up & Rotation", "Side Plank"
    };

    // UI
    private TextView tvPhase, tvExercise, tvTimer, tvNext, tvProgress;
    private Button btnStartPause, btnSkip;

    // State
    private int index = -1;          // exercise index (0..EXERCISE_COUNT-1)
    private boolean inWork = false;  // true => currently in work phase; false => rest or get-ready
    private boolean running = false; // is timer running?
    private CountDownTimer timer;
    private long endTimeMillis = 0L; // system time in millis when current phase should end (for resume)
    private long remainingMs = 0L;   // remaining ms when paused

    // Keys for savedInstanceState
    private static final String KEY_INDEX = "key_index";
    private static final String KEY_IN_WORK = "key_in_work";
    private static final String KEY_RUNNING = "key_running";
    private static final String KEY_END_TIME = "key_end_time";
    private static final String KEY_REMAINING = "key_remaining";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);

        // find views (local variables)
        tvPhase = findViewById(R.id.tvPhase);
        tvExercise = findViewById(R.id.tvExercise);
        tvTimer = findViewById(R.id.tvTimer);
        tvNext = findViewById(R.id.tvNext);
        tvProgress = findViewById(R.id.tvProgress);
        btnStartPause = findViewById(R.id.btnStartPause);
        btnSkip = findViewById(R.id.btnSkip);

        // Restore state if it exists, otherwise start fresh
        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(KEY_INDEX, -1);
            inWork = savedInstanceState.getBoolean(KEY_IN_WORK, false);
            running = savedInstanceState.getBoolean(KEY_RUNNING, false);
            endTimeMillis = savedInstanceState.getLong(KEY_END_TIME, 0L);
            remainingMs = savedInstanceState.getLong(KEY_REMAINING, 0L);

            updateUiForCurrentState();
            if (running) {
                long remain = endTimeMillis - SystemClock.elapsedRealtime();
                if (remain > 0) {
                    startCountDown(remain);
                } else {
                    onPhaseFinished(); // Phase finished while app was in background
                }
            }
        } else {
            prepareNextExercise(); // First-time start
        }

        btnStartPause.setOnClickListener(v -> {
            if (running) {
                pauseTimer();
            } else {
                startPhaseOrResume();
            }
        });

        btnSkip.setOnClickListener(v -> skipPhase());
    }

    private void prepareNextExercise() {
        index++;
        if (index >= EXERCISE_COUNT) {
            finishWorkout();
            return;
        }

        inWork = false;
        running = false;
        cancelTimerIfAny();

        // UI setup for the "GET READY" state
        tvPhase.setText(getString(R.string.get_ready_text));
        tvExercise.setText(exercises[index]);
        tvTimer.setText(formatMs(GET_READY_MS));
        String nextExerciseText = (index + 1 < EXERCISE_COUNT) ?
                getString(R.string.next_exercise_label) + " " + exercises[index + 1] : "";
        tvNext.setText(nextExerciseText);
        tvProgress.setText(getString(R.string.exercise_progress, index + 1, EXERCISE_COUNT));
        btnStartPause.setText(getString(R.string.start_button_text));
    }

    private void startPhaseOrResume() {
        if (index >= EXERCISE_COUNT) return;

        if (remainingMs > 0 && !running) {
            startCountDown(remainingMs); // Resume from a paused state
        } else if (!inWork) {
            inWork = true; // Start a new work phase
            tvPhase.setText(getString(R.string.work_phase_text));
            startCountDown(WORK_MS);
        }
    }

    private void startCountDown(long durationMs) {
        cancelTimerIfAny();
        running = true;
        btnStartPause.setText(getString(R.string.pause_button_text));
        endTimeMillis = SystemClock.elapsedRealtime() + durationMs;
        remainingMs = 0L; // Clear remainingMs as the timer is now active

        timer = new CountDownTimer(durationMs, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(formatMs(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                running = false;
                onPhaseFinished();
            }
        }.start();
    }

    private void onPhaseFinished() {
        if (inWork) {
            // Work phase finished -> start rest phase automatically
            inWork = false;
            tvPhase.setText(getString(R.string.rest_phase_text));
            startCountDown(REST_MS);
        } else {
            // Rest phase or "GET READY" phase finished -> prepare the next exercise
            prepareNextExercise();
        }
    }

    private void pauseTimer() {
        if (!running) return; // Cannot pause if not running
        cancelTimerIfAny();
        remainingMs = Math.max(0, endTimeMillis - SystemClock.elapsedRealtime());
        running = false;
        btnStartPause.setText(getString(R.string.resume_button_text));
    }

    private void skipPhase() {
        cancelTimerIfAny();
        prepareNextExercise();
    }

    private void cancelTimerIfAny() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void finishWorkout() {
        cancelTimerIfAny();
        index = EXERCISE_COUNT; // Set index to a finished state
        tvPhase.setText(getString(R.string.workout_finished));
        tvExercise.setText("");
        tvTimer.setText(formatMs(0));
        tvNext.setText("");
        btnStartPause.setEnabled(false);
        btnSkip.setEnabled(false);
        Toast.makeText(this, getString(R.string.workout_finished), Toast.LENGTH_LONG).show();
    }

    private void updateUiForCurrentState() {
        if (index >= EXERCISE_COUNT) {
            finishWorkout();
            return;
        }

        tvExercise.setText(exercises[index]);
        tvProgress.setText(getString(R.string.exercise_progress, index + 1, EXERCISE_COUNT));
        String nextExerciseText = (index + 1 < EXERCISE_COUNT) ?
                getString(R.string.next_exercise_label) + " " + exercises[index + 1] : "";
        tvNext.setText(nextExerciseText);

        if (running) {
            tvPhase.setText(inWork ? getString(R.string.work_phase_text) : getString(R.string.rest_phase_text));
            btnStartPause.setText(getString(R.string.pause_button_text));
        } else {
            if (remainingMs > 0) { // Paused state
                tvPhase.setText(inWork ? getString(R.string.work_phase_text) : getString(R.string.rest_phase_text));
                btnStartPause.setText(getString(R.string.resume_button_text));
                tvTimer.setText(formatMs(remainingMs));
            } else { // "GET READY" state
                tvPhase.setText(getString(R.string.get_ready_text));
                btnStartPause.setText(getString(R.string.start_button_text));
                tvTimer.setText(formatMs(GET_READY_MS));
            }
        }
    }

    /**
     * UPDATED METHOD:
     * Formats milliseconds into a mm:ss string, explicitly using Locale.US
     * to prevent locale-related formatting bugs.
     */
    private String formatMs(long ms) {
        long totalSec = (ms + 999) / 1000; // Round up to the nearest second
        long m = totalSec / 60;
        long s = totalSec % 60;
        // Use Locale.US to ensure consistent formatting (e.g., "01:05")
        return String.format(Locale.US, "%02d:%02d", m, s);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (running) {
            pauseTimer();
            running = true; // Set it back to true so the state restoration logic knows to restart the timer
        }
        outState.putInt(KEY_INDEX, index);
        outState.putBoolean(KEY_IN_WORK, inWork);
        outState.putBoolean(KEY_RUNNING, running);
        outState.putLong(KEY_END_TIME, endTimeMillis);
        outState.putLong(KEY_REMAINING, remainingMs);
    }

    @Override
    protected void onDestroy() {
        cancelTimerIfAny();
        super.onDestroy();
    }
}


