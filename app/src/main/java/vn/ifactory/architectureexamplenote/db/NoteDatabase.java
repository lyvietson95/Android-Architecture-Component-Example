package vn.ifactory.architectureexamplenote.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by PC on 11/09/2018.
 */

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase sInstance;

    public static synchronized NoteDatabase getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return sInstance;
    }

    private static RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopularDbTask(sInstance).execute();
        }
    };

    // this task init dummy data for notes, because we don't want to start with empty table
    private static class PopularDbTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private PopularDbTask(NoteDatabase database) {
            noteDao = database.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "My Description 1", 1));
            noteDao.insert(new Note("Title 2", "My Description 2", 2));
            noteDao.insert(new Note("Title 3", "My Description 3", 3));
            return null;
        }
    }

    public abstract NoteDao noteDao();
}
