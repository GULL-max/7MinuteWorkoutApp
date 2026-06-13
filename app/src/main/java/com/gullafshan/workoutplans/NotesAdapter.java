package com.gullafshan.workoutplans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private ArrayList<NoteItem> notesList;
    private OnNoteDeleteListener deleteListener;

    // Interface to handle delete click
    public interface OnNoteDeleteListener {
        void onNoteDelete(int position);
    }

    public NotesAdapter(ArrayList<NoteItem> notesList, OnNoteDeleteListener listener) {
        this.notesList = notesList;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteItem note = notesList.get(holder.getAdapterPosition()); // always use holder.getAdapterPosition()
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());

        // Delete button click
        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && deleteListener != null) {
                deleteListener.onNoteDelete(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    // ViewHolder class
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        Button btnDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvContent = itemView.findViewById(R.id.tvNoteContent);

            // Add a delete button programmatically if not in item_note.xml
            btnDelete = new Button(itemView.getContext());
            btnDelete.setText("Delete");

            // Add button to the layout
            ((ViewGroup) itemView).addView(btnDelete);
        }
    }
}

