package com.example.user.buttonbar.fragments.diary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.buttonbar.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 13.07.2017.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class DiaryFragment extends Fragment implements DiaryAdapter.OnDiaryClickListener {

    /*EditText text_name;
    EditText text_surname;
    Button btn;*/

    public static final String EXTRA_DIARY = "diary";
    public static final String KEY_DIARIES = "diaries";
    public static final int DIARY_CREATION_REQUEST_CODE = 1;
    public static final int DIARY_EDITING_REQUEST_CODE = 2;
    private final int TIME_OF_DIARY_WRITING = 0;
    private final Calendar calendar = Calendar.getInstance();

    DiaryAdapter adapter;
    RecyclerView recyclerView;

    Intent intent;
    Gson gson;
    Diary diary;
    DiaryList diaryList;
    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_diary, container, false);
        return v;
    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text_name = (EditText) view.findViewById(R.id.et_name);
        text_surname = (EditText) view.findViewById(R.id.et_surname);

        btn = (Button) view.findViewById(R.id.btn);

        text_name.addTextChangedListener(mTextWatcher);
        text_surname.addTextChangedListener(mTextWatcher);

        checkFieldsForEmptyValues();
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues(){

        String s1 = text_name.getText().toString();
        String s2 = text_surname.getText().toString();

        if(s1.equals("")|| s2.equals("")){
            btn.setEnabled(false);
        } else {
            btn.setEnabled(true);
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intent = new Intent(getContext(), DiaryExpandedActivity.class);
        gson = new Gson();
        calendar.set(Calendar.HOUR, TIME_OF_DIARY_WRITING);
        sharedPreferences = this.getActivity().getPreferences(MODE_PRIVATE);
        diaryList = getDiaryList();
        adapter = new DiaryAdapter(diaryList.getDiaries(), this);

        if (Calendar.getInstance().get(Calendar.HOUR) >= calendar.get(Calendar.HOUR)) {
            diary = new Diary("", "");
            intent.putExtra(EXTRA_DIARY, diary);
            startActivityForResult(intent, DIARY_CREATION_REQUEST_CODE);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
}
