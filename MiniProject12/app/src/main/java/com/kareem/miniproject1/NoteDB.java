package com.kareem.miniproject1;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Notes.class, version = 2, exportSchema = false)
public abstract class NoteDB extends RoomDatabase {

    public abstract NoteDAO getNoteDao();

    private static NoteDB instance;

    public static NoteDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            NoteDB.class, "noteDB")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}