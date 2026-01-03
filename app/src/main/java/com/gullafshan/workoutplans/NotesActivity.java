package com.gullafshan.workoutplans;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private RecyclerView rvNotes;
    private Button btnAddNote;
    private NotesAdapter adapter;
    private ArrayList<NoteItem> notesList;

    private SharedPreferences sp;
    private static final String PREFS = "notes_prefs";
    private static final String KEY_NOTES = "saved_notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        rvNotes = findViewById(R.id.rvNotes);
        btnAddNote = findViewById(R.id.btnAddNote);

        // SharedPreferences
        sp = getSharedPreferences(PREFS, MODE_PRIVATE);

        // Load notes
        notesList = new ArrayList<>();
        loadNotes();

        // Set up RecyclerView with delete listener
        adapter = new NotesAdapter(notesList, position -> {
            // Delete note from list
            NoteItem deletedNote = notesList.get(position);

            notesList.remove(position);
            adapter.notifyItemRemoved(position);

            // Save updated list
            saveNotes();

            Toast.makeText(NotesActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
        });

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setAdapter(adapter);

        // Add Note button
        btnAddNote.setOnClickListener(v -> showAddNoteDialog());
    }

    // Add Note Dialog
    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
        EditText etTitle = view.findViewById(R.id.etNoteTitle);
        EditText etContent = view.findViewById(R.id.etNoteContent);

        builder.setView(view);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            NoteItem note = new NoteItem(title, content);
            notesList.add(0, note); // add at top
            adapter.notifyItemInserted(0);
            rvNotes.scrollToPosition(0);

            saveNotes();
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Load notes from SharedPreferences
    private void loadNotes() {
        String json = sp.getString(KEY_NOTES, null);
        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                notesList.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String title = obj.getString("title");
                    String content = obj.getString("content");
                    notesList.add(new NoteItem(title, content));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Save notes to SharedPreferences
    private void saveNotes() {
        JSONArray array = new JSONArray();
        try {
            for (NoteItem note : notesList) {
                JSONObject obj = new JSONObject();
                obj.put("title", note.getTitle());
                obj.put("content", note.getContent());
                array.put(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sp.edit().putString(KEY_NOTES, array.toString()).apply();
    }
}


