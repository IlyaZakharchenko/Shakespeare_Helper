package itis11_601.shakespearehelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import itis11_601.shakespearehelper.R;
import itis11_601.shakespearehelper.adapters.DiaryAdapter;
import itis11_601.shakespearehelper.models.Diary;
import itis11_601.shakespearehelper.models.DiaryList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DiaryActivity extends AppCompatActivity implements DiaryAdapter.OnDiaryClickListener {

    public static final String EXTRA_DIARY = "diary";
    public static final String KEY_DIARIES = "diaries";
    public static final int DIARY_CREATION_REQUEST_CODE = 1;
    public static final int DIARY_EDITING_REQUEST_CODE = 2;
    public static final String EXTRA_REQUEST_CODE = "request code";
    private final int TIME_OF_DIARY_WRITING = 21;
    private final Calendar calendar = Calendar.getInstance();

    DiaryAdapter adapter;
    RecyclerView recyclerView;

    Intent intent;
    Gson gson;
    Diary diary;
    DiaryList diaryList;
    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        intent = new Intent(this, DiaryExpandedActivity.class);
        gson = new Gson();
        calendar.set(Calendar.HOUR_OF_DAY, TIME_OF_DIARY_WRITING);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        diaryList = getDiaryList();
        adapter = new DiaryAdapter(diaryList.getDiaries(), this);

        if (Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow")).get(Calendar.HOUR_OF_DAY) >= calendar.get(Calendar.HOUR_OF_DAY) && !checkLastDiaryDate()) {
            diary = new Diary("", "");
            intent.putExtra(EXTRA_DIARY, diary);
            startActivityForResult(intent, DIARY_CREATION_REQUEST_CODE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onDiaryClick(Diary diary) {
        this.diary = diary;
        intent.putExtra(EXTRA_DIARY, diary);
        startActivityForResult(intent, DIARY_EDITING_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Diary currentDiary = (Diary) data.getExtras().getSerializable(EXTRA_DIARY);
            switch (requestCode) {
                case DIARY_CREATION_REQUEST_CODE:
                    update(currentDiary);
                    break;
                case DIARY_EDITING_REQUEST_CODE:
                    update(currentDiary, adapter.getPosition(diary));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void putDiaryList(DiaryList diaryList) {
        String json = gson.toJson(diaryList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_DIARIES, json);
        editor.apply();
    }

    private DiaryList getDiaryList() {
        String json = sharedPreferences.getString(KEY_DIARIES, "{}");
        DiaryList diaries = gson.fromJson(json, DiaryList.class);
        if (diaries.getDiaries() == null)  {
            diaries = new DiaryList(new ArrayList<Diary>());
        }
        return diaries;
    }

    private void update(Diary diary, int position) {
        diaryList.updateList(diary, position);
        adapter.notifyDataSetChanged();
        putDiaryList(diaryList);
    }

    private void update(Diary diary) {
        diaryList.updateList(diary);
        adapter.notifyDataSetChanged();
        putDiaryList(diaryList);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    private boolean checkLastDiaryDate() {
        List<Diary> diaries = diaryList.getDiaries();
        int size = diaries.size();
        return size > 0 && diaries.get(diaries.size() - 1).getCreationDate().equals(Diary.dateFormat.format(calendar.getTime()));
    }
}
