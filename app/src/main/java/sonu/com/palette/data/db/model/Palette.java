package sonu.com.palette.data.db.model;

import java.util.Arrays;

/**
 * Created by sonu on 1/13/2017.
 */

public class Palette {
    public long id;
    public long timestamp;
    public String[] colorLabels;
    public String[] colorHexs;
    public String label;
    public boolean marked;

    public Palette(long id,
                   long timestamp,
                   String[] colorLabels,
                   String[] colorHexs,
                   String label,
                   boolean marked) {
        this.id = id;
        this.timestamp = timestamp;
        this.colorLabels = colorLabels;
        this.colorHexs = colorHexs;
        this.label = label;
        this.marked = marked;
    }

    @Override
    public String toString() {
        return "Pallete("
                + id
                + ","
                + timestamp
                + ","
                + Arrays.toString(colorLabels)
                + ","
                + Arrays.toString(colorHexs)
                + ","
                + label
                + ","
                + marked
                + ")";
    }
}
