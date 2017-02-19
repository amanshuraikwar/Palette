package sonu.com.palette.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

import sonu.com.palette.data.db.contract.DBContract;
import sonu.com.palette.data.db.model.Palette;

/**
 * Created by sonu on 1/13/2017.
 */

public class AppDbHelper extends SQLiteOpenHelper implements DbHelper {
    private static final String TAG = AppDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Palette.db";

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.PrimaryRelation.SQL_CREATE_ENTRIES);
        db.execSQL(DBContract.PaletteRelation.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.PrimaryRelation.SQL_DELETE_ENTRIES);
        db.execSQL(DBContract.PaletteRelation.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public long addPalette(long timestamp,
                           String labels[],
                           String hexs[],
                           String label,
                           boolean marked) throws Exception {
        Log.i(TAG, "addPalette:labels=" + Arrays.toString(labels) + ",hexs=" + Arrays.toString(hexs));

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.PrimaryRelation.COLUMN_NAME_TIMESTAMP, timestamp);
        contentValues.put(DBContract.PrimaryRelation.COLUMN_NAME_LABEL, label);
        if (marked) {
            contentValues.put(DBContract.PrimaryRelation.COLUMN_NAME_MARKED, 1);
        } else {
            contentValues.put(DBContract.PrimaryRelation.COLUMN_NAME_MARKED, 0);
        }
        long newRowId_1 = db.insert(DBContract.PrimaryRelation.TABLE_NAME, null, contentValues);

        if (labels.length != hexs.length) {
            throw new Exception("Labels Array and Hexs Array should be of same length");
        }

        int noOfColors = labels.length;
        for (int i = 0; i < noOfColors; i++) {
            contentValues = new ContentValues();
            contentValues.put(DBContract.PaletteRelation.COLUMN_NAME_PRIMARY_ID, newRowId_1);
            contentValues.put(DBContract.PaletteRelation.COLUMN_NAME_COLOR_LABEL, labels[i]);
            contentValues.put(DBContract.PaletteRelation.COLUMN_NAME_COLOR_HEX, hexs[i]);
            long newRowId_2 = db.insert(DBContract.PaletteRelation.TABLE_NAME, null, contentValues);
        }

        return newRowId_1;
    }

    @Override
    public Palette[] getPalettes() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection_1 = {
                DBContract.PrimaryRelation._ID,
                DBContract.PrimaryRelation.COLUMN_NAME_TIMESTAMP,
                DBContract.PrimaryRelation.COLUMN_NAME_LABEL,
                DBContract.PrimaryRelation.COLUMN_NAME_MARKED
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DBContract.PrimaryRelation._ID + " DESC";

        Cursor cursor_1 = db.query(
                DBContract.PrimaryRelation.TABLE_NAME,      // The table to query
                projection_1,                               // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        Palette palettes[] = new Palette[cursor_1.getCount()];

        while (cursor_1.moveToNext()) {
            long itemId = cursor_1.getLong(cursor_1.getColumnIndexOrThrow(
                    DBContract.PrimaryRelation._ID));

            String[] projection_2 = {
                    DBContract.PaletteRelation._ID,
                    DBContract.PaletteRelation.COLUMN_NAME_PRIMARY_ID,
                    DBContract.PaletteRelation.COLUMN_NAME_COLOR_LABEL,
                    DBContract.PaletteRelation.COLUMN_NAME_COLOR_HEX
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = DBContract.PaletteRelation.COLUMN_NAME_PRIMARY_ID + " = ?";
            String[] selectionArgs = {itemId + ""};

            Cursor cursor_2 = db.query(
                    DBContract.PaletteRelation.TABLE_NAME,                     // The table to query
                    projection_2,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            String colorLabels[] = new String[cursor_2.getCount()];
            String colorHexs[] = new String[cursor_2.getCount()];
            while (cursor_2.moveToNext()) {
                colorLabels[cursor_2.getPosition()] = cursor_2.getString(
                        cursor_2.getColumnIndexOrThrow(
                                DBContract.PaletteRelation.COLUMN_NAME_COLOR_LABEL));

                colorHexs[cursor_2.getPosition()] = cursor_2.getString(
                        cursor_2.getColumnIndexOrThrow(
                                DBContract.PaletteRelation.COLUMN_NAME_COLOR_HEX));
            }

            cursor_2.close();

            long timestamp = cursor_1.getLong(
                    cursor_1.getColumnIndexOrThrow(
                            DBContract.PrimaryRelation.COLUMN_NAME_TIMESTAMP));
            String label = cursor_1.getString(
                    cursor_1.getColumnIndexOrThrow(
                            DBContract.PrimaryRelation.COLUMN_NAME_LABEL));

            boolean marked;
            marked = cursor_1.getInt(
                    cursor_1.getColumnIndexOrThrow(
                            DBContract.PrimaryRelation.COLUMN_NAME_MARKED)) != 0;

            Palette palette = new Palette(itemId, timestamp, colorLabels, colorHexs, label, marked);
            palettes[cursor_1.getPosition()] = palette;
        }

        cursor_1.close();

        return palettes;
    }
}
