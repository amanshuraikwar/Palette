package sonu.com.palette.ui.main;

import android.content.Context;

import sonu.com.palette.data.AppDataManager;
import sonu.com.palette.data.DataManager;
import sonu.com.palette.data.db.model.Palette;

/**
 * Created by sonu on 5/2/17.
 */

public class MainFragmentPresenter implements MainMvpFragmentPresenter {

    private Context mContext;
    private DataManager dataManager;

    public MainFragmentPresenter(Context context) {
        mContext = context;
        dataManager = AppDataManager.getInstance(context);
    }

    @Override
    public Palette[] getPalettesFromDb() {
        return dataManager.getPalettes();
    }
}
