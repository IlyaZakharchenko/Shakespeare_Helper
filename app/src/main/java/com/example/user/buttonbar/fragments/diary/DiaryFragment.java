package com.example.user.buttonbar.fragments.diary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.buttonbar.R;
import com.example.user.buttonbar.ShowPopUpWindow;
import com.example.user.buttonbar.adapters.DiaryAdapter;
import com.example.user.buttonbar.models.Diary;
import com.example.user.buttonbar.models.DiaryList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DiaryFragment extends Fragment implements DiaryAdapter.OnDiaryClickListener {

    //  Ключи для Extra

    public static final String EXTRA_REQUEST_CODE = "request code";
    public static final String EXTRA_DIARY = "diary";

    // Ключи для Shared Preferences

    public static final String KEY_DIARIES = "diaries";
    public static final String KEY_HOURS = "hours";
    public static final String KEY_MINUTES = "minutes";

    // Коды запроса для Result Activity

    public static final int DIARY_CREATION_REQUEST_CODE = 1;
    public static final int DIARY_EDITING_REQUEST_CODE = 2;
    private static final int DIARY_TIME_REQUEST_CODE = 3;

    private final Calendar calendar = Calendar.getInstance();

    DiaryAdapter adapter;
    RecyclerView recyclerView;

    // Время создания дневника

    int hourOfDiaryWriting;
    int minuteOfDiaryWriting;

    Intent intent;
    Gson gson;
    Diary diary;
    DiaryList diaryList;
    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_diary, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_diary_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getContext(), ShowPopUpWindow.class);
        startActivityForResult(intent, DIARY_TIME_REQUEST_CODE);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intent = new Intent(getContext(), DiaryExpandedActivity.class);
        gson = new Gson();
        sharedPreferences = this.getActivity().getPreferences(MODE_PRIVATE);
        diaryList = getDiaryList();
        adapter = new DiaryAdapter(diaryList.getDiaries(), this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getTime();
        diaryCreation();
    }

    @Override
    public void onDiaryClick(Diary diary) {
        this.diary = diary;
        intent.putExtra(EXTRA_DIARY, diary);
        startActivityForResult(intent, DIARY_EDITING_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Diary currentDiary = (Diary) data.getExtras().getSerializable(EXTRA_DIARY);
            switch (requestCode) {
                case DIARY_CREATION_REQUEST_CODE:
                    update(currentDiary);
                    break;
                case DIARY_EDITING_REQUEST_CODE:
                    update(currentDiary, adapter.getPosition(diary));
                    break;
                case DIARY_TIME_REQUEST_CODE:
                    hourOfDiaryWriting = data.getIntExtra(ShowPopUpWindow.EXTRA_HOUR, 0);
                    minuteOfDiaryWriting = data.getIntExtra(ShowPopUpWindow.EXTRA_MINUTE, 0);
                    putTime();
                    diaryCreation();
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

    private boolean checkTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        return cal.get(Calendar.HOUR_OF_DAY) >= hourOfDiaryWriting && cal.get(Calendar.MINUTE) >= minuteOfDiaryWriting;
    }

    private void diaryCreation() {
        if (checkTime() && !checkLastDiaryDate()) {
            diary = new Diary("", "");
            intent.putExtra(EXTRA_DIARY, diary);
            startActivityForResult(intent, DIARY_CREATION_REQUEST_CODE);
        }
    }

    private void putTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HOURS, hourOfDiaryWriting);
        editor.putInt(KEY_MINUTES, minuteOfDiaryWriting);
        editor.apply();
    }

    private void getTime() {
        hourOfDiaryWriting = sharedPreferences.getInt(KEY_HOURS, 23);
        minuteOfDiaryWriting = sharedPreferences.getInt(KEY_MINUTES, 59);
    }
}
