package com.example.user.buttonbar.fragments.diary;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 16.07.2017.
 */

public class DiaryList implements Serializable {
    private List<Diary> diaries;

    public List<Diary> getDiaries() {
        return diaries;
    }

    public DiaryList(List<Diary> diaries) {

        this.diaries = diaries;
    }

    public void updateList(Diary diary) {
        diaries.add(diary);
    }

    public void updateList(Diary diary, int position) {
        diaries.set(position, diary);
    }
}