package sonu.com.palette.ui.main;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sonu.com.palette.data.AppDataManager;
import sonu.com.palette.data.DataManager;
import sonu.com.palette.data.db.model.Palette;

/**
 * Created by sonu on 5/2/17.
 */

public class MainPresenter implements MainMvpPresenter {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private static final String regexPattern = "(.*[{])(.+)([}].*)";

    private Context mContext;

    public MainPresenter(Context context) {
        mContext = context;
    }

    private String getFileContents(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String fileData = "";
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            fileData += temp;
        }
        return fileData;
    }

    private Palette getPallete(String data) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(data);

        Log.d(TAG, "matcher group count=" + matcher.groupCount());
        if (matcher.find()) {
            int usefulGroupIndex = 2;
            String colorsKeyValues = "";
            for (int i = 0; i <= matcher.groupCount(); i++) {
                if (i == usefulGroupIndex) {
                    colorsKeyValues = matcher.group(i);
                }
                Log.d(TAG, "finding patterns group=" + i + " pattern=" + matcher.group(i));
            }

            String colorsKeyValueArray[] = colorsKeyValues.split(";");
            String colorLabels[] = new String[colorsKeyValueArray.length];
            String colorHexs[] = new String[colorsKeyValueArray.length];

            for (int i = 0; i < colorsKeyValueArray.length; i++) {
                String parts[] = colorsKeyValueArray[i].split(":");
                Log.d(TAG, "LABEL=" + parts[0].trim() + " && COLOR=" + parts[1].trim());
                colorLabels[i] = parts[0].trim();
                colorHexs[i] = parts[1].trim();
            }

            return new Palette(-1, -1, colorLabels, colorHexs, "", false);
        } else {
            return null;
        }
    }

    @Override
    public void handleReceivedFile(File file) {
        try {
            String fileData = getFileContents(file);
            Log.d(TAG, "data in file=" + fileData);

            Palette palette = getPallete(fileData);

            if (palette != null) {
                DataManager dataManager = AppDataManager.getInstance(mContext);
                long id = dataManager
                        .addPalette(
                                System.currentTimeMillis(),
                                palette.colorLabels,
                                palette.colorHexs,
                                "",
                                false);
                palette.timestamp = System.currentTimeMillis();
                palette.id = id;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
