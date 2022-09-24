package com.kareem.miniproject1;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String notetitle;
    private String notebody;
    private boolean isFav;

    public Notes() {
    }

    public Notes(int id, String notetitle, String notebody, boolean isfav) {
        this.id = id;
        this.notetitle = notetitle;
        this.notebody = notebody;
        this.isFav = isfav;
    }

    public Notes(String notetitle, String notebody, boolean isfav) {
        this.notetitle = notetitle;
        this.notebody = notebody;
        this.isFav = isfav;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotebody() {
        return notebody;
    }

    public void setNotebody(String notebody) {
        this.notebody = notebody;
    }
}
