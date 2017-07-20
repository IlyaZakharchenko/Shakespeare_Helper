package com.example.user.buttonbar.fragments.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.user.buttonbar.R;

/**
 * Created by User on 13.07.2017.
 */

public class CameraFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {
    public static final int CAMERA_REQUEST_CODE = 1;
    private ImageView mPhotoTaken;
    private float dX;
    private float dY;
    private ImageView mBtn_Save;
    private FrameLayout frameLayout;
    private ImageView mMustache;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        takePhoto();
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        mPhotoTaken = (ImageView) rootView.findViewById(R.id.i1);
        frameLayout = (FrameLayout) rootView.findViewById(R.id.frame);
        mBtn_Save = (ImageView) rootView.findViewById(R.id.btn_save);
        mMustache = (ImageView) rootView.findViewById(R.id.i2);
        mBtn_Save.setOnClickListener(this);
        mMustache.setOnTouchListener(this);
        return rootView;
    }

    private void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Камера");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                view.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            mPhotoTaken.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(),
                savePhoto() ? "Фото сохранено!" : "Возникла внутренняя ошибка:("
                , Toast.LENGTH_LONG)
                .show();
    }

    private boolean savePhoto() {
        frameLayout.setDrawingCacheEnabled(true);
        frameLayout.buildDrawingCache();
        Bitmap bitmap = frameLayout.getDrawingCache();
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return false;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(
                mediaStorageDir.getPath() + File.separator + "MUSTACHE_" + timeStamp + ".jpg"
        );
        try {
            FileOutputStream stream = new FileOutputStream(mediaFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
        Fragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("photo", mediaFile.getAbsolutePath());
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack("PHOTO");
        ft.replace(R.id.NavBot, fragment).commit();
        return true;
    }
}
