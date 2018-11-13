package vn.ifactory.architectureexamplenote.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import vn.ifactory.architectureexamplenote.R;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "NOTE_ID";
    public static final String EXTRA_TITLE = "NOTE_TITLE";
    public static final String EXTRA_DESCRIPTION = "NOTE_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "NOTE_PRIORITY";

    EditText edtTitle, edtDescription;
    NumberPicker npPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        addControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuAddNote:
                addNote();
                break;
                default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNote() {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        int priority = npPriority.getValue();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Title or Description must be input.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Note note = new Note(title, description, priority);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_PRIORITY, priority);

        int noteId = getIntent().getIntExtra(EXTRA_ID, -1); // getIntent() # intent above
        if (noteId != -1) {
            intent.putExtra(EXTRA_ID, noteId);
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    private void addControls() {
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        npPriority = findViewById(R.id.npPriority);
        npPriority.setMinValue(0);
        npPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            String title = intent.getStringExtra(EXTRA_TITLE);
            String description = intent.getStringExtra(EXTRA_DESCRIPTION);
            int priority = intent.getIntExtra(EXTRA_PRIORITY, 0);

            edtTitle.setText(title);
            edtDescription.setText(description);
            npPriority.setValue(priority);

        }else {
            setTitle("Add Note");
        }

    }
}
