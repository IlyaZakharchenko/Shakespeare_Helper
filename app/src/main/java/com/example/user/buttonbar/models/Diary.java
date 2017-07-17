package com.example.user.buttonbar.models;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;

/**
 * Created by User on 16.07.2017.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class Diary implements Serializable {

    private String tittle;
    private String creationDate;
    private String content;
    private String editingDate;

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public Diary(String tittle, String content) {
        Calendar c = Calendar.getInstance();
        creationDate = dateFormat.format(c.getTime());
        editingDate = "";
        this.tittle = tittle;
        this.content = content;
    }

    public String getTittle() {

        return tittle;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getContent() {
        return content;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEditingDate(String editingDate) {
        this.editingDate = editingDate;
    }

    public String getEditingDate() {
        return editingDate;
    }
}