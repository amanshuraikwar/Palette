package sonu.com.palette.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sonu.com.palette.R;
import sonu.com.palette.data.AppDataManager;
import sonu.com.palette.data.DataManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainMvpView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainMvpPresenter mainMvpPresenter;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainMvpPresenter = new MainPresenter();

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        Log.d(TAG, "ACTION=" + action + " TYPE=" + type);

        if ((Intent.ACTION_VIEW.equals(action)) && type != null) {
            Log.i(TAG, "intent extras=" + intent.getExtras() + "");
            Log.i(TAG, "path to file=" + intent.getData().getPath());
            mainMvpPresenter.handleRecievedFile(new File(intent.getData().getPath()));
        } else if (Intent.ACTION_SEND.equals(action) && type != null) {
            Uri uri = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
            String path = getPath(uri);
            Log.i(TAG, "intent extras=" + intent.getExtras());
            Log.i(TAG, "path to file=" + path);
            if (path != null) {
                mainMvpPresenter.handleRecievedFile(new File(path));
            }
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void hideToolbar() {

    }

    @Override
    public void showToolbar() {

    }

    @Override
    public void hideFab() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
        floatingActionButton.animate()
                .translationY(floatingActionButton.getHeight() + lp.bottomMargin)
                .setInterpolator(new AccelerateInterpolator(2))
                .start();
    }

    @Override
    public void showFab() {
        floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
}
