package ru.shakespearetools.rhms;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

import static android.view.KeyEvent.KEYCODE_ENTER;

/**
 * Created by al on 16.07.17.
 */

public class RhymesFragment extends Fragment {

    View tView;

    @BindView(R.id.rhymes_text_word) EditText word;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tView=inflater.inflate(R.layout.rhymes_fragment,container,false);
        return tView; //super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        //TODO
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        //TODO Перевод строки
//        EditText word = (EditText) view.findViewById(R.id.rhymes_text_word);
//        word.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (keyCode == KEYCODE_ENTER) {
//                    return true;
//                }
//                return false;
//            }
//        });

        Button btn = (Button) view.findViewById(R.id.rhymes_buttin_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().findViewById(R.id.rhymes_list).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.rhymes_progress_bar).setVisibility(View.VISIBLE);
                EditText word = (EditText) view.findViewById(R.id.rhymes_text_word);
                RhymeTask rhymeTask = new RhymeTask(getActivity());
                rhymeTask.execute(word.getText() + "");

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @OnEditorAction(R.id.rhymes_text_word) boolean onEnterClicK(int actionIme) {
        switch (actionIme) {
            case EditorInfo.IME_ACTION_DONE :


                getActivity().findViewById(R.id.rhymes_list).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.rhymes_progress_bar).setVisibility(View.VISIBLE);
                //EditText word = (EditText) view.findViewById(R.id.rhymes_text_word);
                RhymeTask rhymeTask = new RhymeTask(getActivity());
                rhymeTask.execute(word.getText() + "");


                return true;
        }

        return false;
    }

    @Override
    public void onDestroyView() {

        EventBus.getDefault().unregister(this);

        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArrayList<String> words) {
        Log.e("ALm", "onEvent");
        ListView lvMain = (ListView) tView.findViewById(R.id.rhymes_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, words);

        lvMain.setAdapter(adapter);
        getActivity().findViewById(R.id.rhymes_list).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.rhymes_progress_bar).setVisibility(View.INVISIBLE);
    }
}
