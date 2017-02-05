package sonu.com.palette.data.db;

import sonu.com.palette.data.db.model.Palette;

/**
 * Created by sonu on 5/2/17.
 */

public interface DbHelper {
    public void addPalette(long timestamp,
                           String labels[],
                           String hexs[],
                           String label,
                           boolean marked) throws Exception;
    public Palette[] getPalettes();
}
