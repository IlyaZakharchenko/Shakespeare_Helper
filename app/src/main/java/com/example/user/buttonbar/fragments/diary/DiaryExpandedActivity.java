package com.example.user.buttonbar.fragments.diary;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.buttonbar.R;
import com.example.user.buttonbar.models.Diary;

public class DiaryExpandedActivity extends AppCompatActivity {
    Diary diary;
    Intent intent;

    EditText tittle;
    EditText content;
    Button okBtn;
    TextView creationDate;
    RelativeLayout relativeLayout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_expanded);

        tittle = (EditText) findViewById(R.id.et_tittle);
        creationDate = (TextView) findViewById(R.id.tv_creation_date);
        content = (EditText) findViewById(R.id.et_content);
        okBtn = (Button) findViewById(R.id.ok_btn);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        tittle.addTextChangedListener(mTextWatcher);
        content.addTextChangedListener(mTextWatcher);

        intent = new Intent();
        diary = (Diary) getIntent().getExtras().getSerializable(DiaryFragment.EXTRA_DIARY);

        creationDate.setText(diary.getCreationDate());
        tittle.setText(diary.getTittle());
        content.setText(diary.getContent());

        okBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                onOKClick();
            }
        });

        checkFieldsForEmptyValues();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onOKClick() {
            diary.setContent(content.getText().toString());
            diary.setTittle(tittle.getText().toString());
            diary.setEditingDate(Diary.dateFormat.format(Calendar.getInstance().getTime()));
            setResult(RESULT_OK, intent);
            intent.putExtra(DiaryFragment.EXTRA_DIARY, diary);
            finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getIntExtra(DiaryFragment.EXTRA_REQUEST_CODE, 0) == DiaryFragment.DIARY_CREATION_REQUEST_CODE) {
                Toast.makeText(this, "Нужно заполнить дневник на сегодня.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, keyEvent);
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

    public void checkFieldsForEmptyValues(){

        String s1 = tittle.getText().toString();
        String s2 = content.getText().toString();

        if(s1.equals("")|| s2.equals("")){
            okBtn.setEnabled(false);
        } else {
            okBtn.setEnabled(true);
        }
    }
}