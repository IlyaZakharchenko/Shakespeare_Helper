package itis11_601.shakespearehelper.models;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;

public class Diary implements Serializable {

    private String tittle;
    private String creationDate;
    private String content;
    private String editingDate;
    private boolean isFilled;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Diary(String tittle, String content) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        creationDate = dateFormat.format(c.getTime());
        editingDate = "";
        this.tittle = tittle;
        this.content = content;
        isFilled = false;
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

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEditingDate(String editingDate) {
        this.editingDate = editingDate;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    public String getEditingDate() {
        return editingDate;
    }
}
