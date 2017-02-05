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
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sonu.com.palette.R;
import sonu.com.palette.data.AppDataManager;
import sonu.com.palette.data.DataManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainMvpView{

    private static final String TAG = MainActivity.class.getSimpleName();

    Toolbar toolbar;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        final Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        Log.d(TAG,"ACTION="+action+" TYPE="+type);

        if((Intent.ACTION_VIEW.equals(action)) && type != null) {
            Log.d(TAG,"got intent data="+intent.getData());

            File recievedFile = new File(intent.getData().getPath()+"");

            handleFile(recievedFile);
        }else if(Intent.ACTION_SEND.equals(action) && type != null) {
            Log.d(TAG,intent.getExtras()+"");
            Uri uri = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
            Log.d(TAG,getPath(uri)+"");
            handleFile(new File(getPath(uri)));
        }
        
    }

    private void handleFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String fileData = "";
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                fileData += temp;
            }

            Log.d(TAG,"data in file="+fileData);

            Pattern pattern = Pattern.compile("(.*[{])(.+)([}].*)");
            Matcher matcher = pattern.matcher(fileData);

            Log.d(TAG,"matcher group count="+matcher.groupCount());
            if(matcher.find()){
                int usefulGroupIndex = 2;
                String colorsKeyValues ="";
                for (int i = 0;i <= matcher.groupCount();i++) {
                    if(i == usefulGroupIndex) {
                        colorsKeyValues = matcher.group(i);
                    }
                    Log.d(TAG,"finding patterns group="+i+" pattern="+matcher.group(i));
                }

                String colorsKeyValueArray[] = colorsKeyValues.split(";");
                String colorLabels[] = new String[colorsKeyValueArray.length];
                String colorHexs[] = new String[colorsKeyValueArray.length];

                for (int i = 0;i < colorsKeyValueArray.length; i++) {
                    String parts[] = colorsKeyValueArray[i].split(":");
                    Log.d(TAG, "LABEL=" + parts[0].trim() + " && COLOR=" + parts[1].trim());
                    colorLabels[i] = parts[0].trim();
                    colorHexs[i] = parts[1].trim();
                }

                DataManager dataManager = AppDataManager.getInstance(this);

                dataManager.addPalette(System.currentTimeMillis(),colorLabels,colorHexs,"",false);

//                    palettesAdapter = new PalettesAdapter(
//                            MainActivity.this,
//                            MainActivity.this,
//                            new DBHelper(MainActivity.this).getPalettes());
//
//                    palettesRecyclerView.setAdapter(palettesAdapter);
//                    palettesRecyclerView.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
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
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        fab.animate()
                .translationY(fab.getHeight()+lp.bottomMargin)
                .setInterpolator(new AccelerateInterpolator(2))
                .start();
    }

    @Override
    public void showFab() {
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
}
