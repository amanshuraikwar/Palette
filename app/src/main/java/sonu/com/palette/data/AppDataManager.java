package sonu.com.palette.data;

import android.content.Context;

import sonu.com.palette.data.db.AppDbHelper;
import sonu.com.palette.data.db.DbHelper;
import sonu.com.palette.data.db.model.Palette;

/**
 * Created by sonu on 5/2/17.
 */

public class AppDataManager implements DataManager{

    private static AppDataManager appDataManager;

    private DbHelper mDbHelper;

    public static AppDataManager getInstance(Context context) {
        if(appDataManager == null) {
            appDataManager = new AppDataManager(context);
        }
        return appDataManager;
    }

    private AppDataManager(Context context) {
        mDbHelper = new AppDbHelper(context);
    }

    @Override
    public void addPalette(long timestamp, String[] labels, String[] hexs, String label, boolean marked) throws Exception {
        mDbHelper.addPalette(timestamp, labels, hexs, label, marked);
    }

    @Override
    public Palette[] getPalettes() {
        return mDbHelper.getPalettes();
    }
}
