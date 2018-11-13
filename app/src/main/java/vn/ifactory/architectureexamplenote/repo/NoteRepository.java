package vn.ifactory.architectureexamplenote.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import vn.ifactory.architectureexamplenote.db.Note;
import vn.ifactory.architectureexamplenote.db.NoteDao;
import vn.ifactory.architectureexamplenote.db.NoteDatabase;

/**
 * Created by PC on 11/09/2018.
 */

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getsInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note){
        new InsertNoteTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    // insert task
    private static class InsertNoteTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        private InsertNoteTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        private UpdateNoteTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        private DeleteNoteTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;
        private DeleteAllNotesTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
