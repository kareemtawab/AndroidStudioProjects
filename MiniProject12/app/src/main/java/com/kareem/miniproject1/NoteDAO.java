package com.kareem.miniproject1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNote(Notes n);

    @Query("select * from notes_table")
    List<Notes> getAllNotes();

    @Query("select * from notes_table where isFav")
    List<Notes> getOnlyFav();
}