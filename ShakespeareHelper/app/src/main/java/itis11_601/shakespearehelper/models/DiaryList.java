package itis11_601.shakespearehelper.models;

import java.io.Serializable;
import java.util.List;

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
