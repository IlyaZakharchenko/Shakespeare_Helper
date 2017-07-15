package com.example.user.buttonbar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.buttonbar.R;

/**
 * Created by User on 13.07.2017.
 */

public class DiaryFragment extends Fragment {

    EditText text_name;
    EditText text_surname;
    Button btn;

    MenuItem item_camera;
    MenuItem item_rhyme;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.diary_bar, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text_name = (EditText) view.findViewById(R.id.et_name);
        text_surname = (EditText) view.findViewById(R.id.et_surname);

        item_camera = (MenuItem) view.findViewById(R.id.ButtonBar_item_1);
        item_rhyme = (MenuItem) view.findViewById(R.id.ButtonBar_item_2);
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
    }
}
