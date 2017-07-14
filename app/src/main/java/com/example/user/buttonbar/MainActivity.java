package com.example.user.buttonbar;

import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.buttonbar.fragments.CameraFragment;
import com.example.user.buttonbar.fragments.DiaryFragment;
import com.example.user.buttonbar.fragments.RhymeFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {

    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                itemSelected(menuItemId);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });

        bottomBar.mapColorForTab(0, "#4286f4");
        bottomBar.mapColorForTab(1, "#f4e241");
        bottomBar.mapColorForTab(2, "#000000");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ButtonBar_item_1:
                CameraFragment cameraFragment = new CameraFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.NavBot, cameraFragment).commit();
                break;
            case R.id.ButtonBar_item_2:
                RhymeFragment rhymeFragment = new RhymeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.NavBot, rhymeFragment).commit();
                break;
            case R.id.ButtonBar_item_3:
                DiaryFragment diaryFragment = new DiaryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.NavBot, diaryFragment).commit();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MainActivity.this);
        quitDialog.setTitle("Вы действительно хотите выйти?");

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        quitDialog.show();
    }

    private void itemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.ButtonBar_item_1:
                CameraFragment cameraFragment = new CameraFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.NavBot, cameraFragment).commit();
                break;
            case R.id.ButtonBar_item_2:
                RhymeFragment rhymeFragment = new RhymeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.NavBot, rhymeFragment).commit();
                break;
            case R.id.ButtonBar_item_3:
                DiaryFragment diaryFragment = new DiaryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.NavBot, diaryFragment).commit();
                break;

        }
    }
}
