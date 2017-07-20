package com.example.user.buttonbar.fragments.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ShareActionProvider;

import com.example.user.buttonbar.R;

/**
 * Created by User on 19.07.2017.
 */

public class PhotoFragment extends Fragment implements View.OnClickListener {
    ImageView mShare;
    private static final String MIME_TYPE = "image/jpeg";
    private Uri uri;
    private ImageView mPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("aaaa","onCreateView");
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
        mShare = (ImageView) rootView.findViewById(R.id.btn_share);
        mShare.setOnClickListener(this);
        mPhoto = (ImageView) rootView.findViewById(R.id.iv_photo);
        uri = Uri.parse(getArguments().getString("photo"));
        mPhoto.setImageURI(uri);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType(MIME_TYPE);
        startActivity(shareIntent);
    }
}
