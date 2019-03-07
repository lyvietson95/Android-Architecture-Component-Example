package vn.ifactory.architectureexamplenote.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.ifactory.architectureexamplenote.adapter.NoteAdapter;
import vn.ifactory.architectureexamplenote.R;
import vn.ifactory.architectureexamplenote.db.Note;

public class MainActivity extends AppCompatActivity implements NoteAdapter.IOnClickListener {

    public static final int REQUEST_CODE_ADD_NOTE = 69;
    public static final int REQUEST_CODE_EDIT_NOTE = 113;

    NoteViewModel noteViewModel;
    RecyclerView rvNote;
    NoteAdapter adapterNote;
    List<Note> listNotes;
    FloatingActionButton fbAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuDeleteAllNote:
                deleteAllNote();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);
        } else if (requestCode == REQUEST_CODE_EDIT_NOTE && resultCode == RESULT_OK) {
            int noteId = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (noteId == -1) {
                Toast.makeText(this, "can't update note", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0);

            Note note = new Note(title, description, priority);
            note.setId(noteId);
            noteViewModel.update(note);
            Toast.makeText(this, "Note edited", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note canceled", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addEvents() {
        fbAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

    }

    private void addNote() {
        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
    }


    private void deleteAllNote() {
        noteViewModel.deleteAllNotes();
        Toast.makeText(this, "All note deleted", Toast.LENGTH_SHORT).show();
    }

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            noteViewModel.delete(adapterNote.getNoteAt(position));
            Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
        }
    });


    private void addControls() {
        listNotes = new ArrayList<>();
        rvNote = findViewById(R.id.rvNote);
        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setHasFixedSize(true);
        adapterNote = new NoteAdapter();
        adapterNote.setOnClickListener(this); // set callback for adapter listener
        rvNote.setAdapter(adapterNote);
        // setup for case swipe to delete note item
        itemTouchHelper.attachToRecyclerView(rvNote);

        fbAddNote = findViewById(R.id.fbAddNote);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                // update recyclerview at here
                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
                listNotes.clear();
                listNotes.addAll(notes);
                //adapterNote.notifyDataSetChanged();
                adapterNote.submitList(notes); // say goodbye notifydatasetchange() because notifydatasetchange costly and instead of using ListAdapter, which build on top DiffUtil class
            }
        });


    }

    @Override
    public void onItemClick(Note note) {
        String title = note.getTitle();
        String description = note.getDescription();
        int priority = note.getPriority();
        int noteId = note.getId();

        Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, title);
        intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, description);
        intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, priority);
        intent.putExtra(AddEditNoteActivity.EXTRA_ID, noteId);

        startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
    }
}
