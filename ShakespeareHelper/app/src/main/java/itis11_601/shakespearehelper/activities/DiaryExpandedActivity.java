package itis11_601.shakespearehelper.activities;

import android.content.Intent;
import java.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import itis11_601.shakespearehelper.R;
import itis11_601.shakespearehelper.models.Diary;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DiaryExpandedActivity extends AppCompatActivity {
    Diary diary;
    Intent intent;

    EditText tittle;
    EditText content;
    Button okBtn;
    TextView creationDate;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanded_diary_layout);

        tittle = (EditText) findViewById(R.id.et_tittle);
        creationDate = (TextView) findViewById(R.id.tv_creation_date);
        content = (EditText) findViewById(R.id.et_content);
        okBtn = (Button) findViewById(R.id.ok_btn);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);

        intent = new Intent();
        diary = (Diary) getIntent().getExtras().getSerializable(DiaryActivity.EXTRA_DIARY);

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


    }

    private boolean isFilled() {
        return !tittle.getText().toString().equals("") && !content.getText().toString().equals("");
    }

    private void onOKClick() {
        if (isFilled()) {
            diary.setContent(content.getText().toString());
            diary.setTittle(tittle.getText().toString());
            diary.setEditingDate(Diary.dateFormat.format(Calendar.getInstance().getTime()));
            setResult(RESULT_OK, intent);
            intent.putExtra(DiaryActivity.EXTRA_DIARY, diary);
            finish();
        } else {
            Toast.makeText(DiaryExpandedActivity.this, "Заполните все поля.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getIntExtra(DiaryActivity.EXTRA_REQUEST_CODE, 0) == DiaryActivity.DIARY_CREATION_REQUEST_CODE) {
                Toast.makeText(this, "Нужно заполнить дневник на сегодня.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return super.onKeyDown(keyCode, keyEvent);
    }
}
