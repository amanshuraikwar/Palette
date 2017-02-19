package sonu.com.palette.data.db.contract;

import android.provider.BaseColumns;

/**
 * Created by sonu on 1/13/2017.
 */

public class DBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DBContract() {
    }

    /* Inner class that defines the table contents */
    public static class PrimaryRelation implements BaseColumns {
        public static final String TABLE_NAME = "primary_relation";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_MARKED = "marked";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME
                        + " ("
                        + _ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_NAME_TIMESTAMP + " INTEGER,"
                        + COLUMN_NAME_LABEL + " VARCHAR(255),"
                        + COLUMN_NAME_MARKED + " BOOLEAN"
                        + ")";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /* Inner class that defines the table contents */
    public static class PaletteRelation implements BaseColumns {
        public static final String TABLE_NAME = "palette_relation";
        public static final String COLUMN_NAME_PRIMARY_ID = "primary_id";
        public static final String COLUMN_NAME_COLOR_LABEL = "color_label";
        public static final String COLUMN_NAME_COLOR_HEX = "color_hex";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME
                        + " ("
                        + _ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_NAME_PRIMARY_ID + " INTEGER,"
                        + COLUMN_NAME_COLOR_LABEL + " VARCHAR(255),"
                        + COLUMN_NAME_COLOR_HEX + " VARCHAR(255)"
                        + ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
