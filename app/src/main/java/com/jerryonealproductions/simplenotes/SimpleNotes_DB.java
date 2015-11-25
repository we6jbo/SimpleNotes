package com.jerryonealproductions.simplenotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by we6jbo2015 on 11/23/15.
 */
public class SimpleNotes_DB extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 20;
    private static final String DATABASE_NAME = "simpleNotes.db";
    private static final String TABLE_SIMPLENOTES = "TABLE_SIMPLENOTES";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LASTROW = "COLUMN_LASTROW";
    private static final String COLUMN_NOTES = "COLUMN_NOTES";

    public SimpleNotes_DB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_SIMPLENOTES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_LASTROW + " TEXT, " + COLUMN_NOTES + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIMPLENOTES);
        onCreate(db);
    }

    public String notes(int fromLocation, int toLocation, String message)
    {
        if (fromLocation >= 0 && toLocation > 0) {
            try {

                ContentValues values = new ContentValues();
                SQLiteDatabase db = getWritableDatabase();

                /* I'm storing the last row location on row 0 of the table. This checks to see if the row exists and returns
                that information.
                 */

                String query = "SELECT * FROM " + TABLE_SIMPLENOTES + " WHERE 1";

                Cursor c = db.rawQuery(query, null);
                c.moveToFirst();

                if(!c.isAfterLast())
                {
                    if(c.getString(c.getColumnIndex(COLUMN_LASTROW)) != null) {
                        if (fromLocation == 0) {
                            String newFromLocation;
                            newFromLocation = c.getString(c.getColumnIndex(COLUMN_LASTROW));
                            c.close();
                            db.close();
                            return newFromLocation;
                        }
                        values.put(COLUMN_LASTROW,fromLocation);
                        values.put(COLUMN_NOTES,"");
                        db.update(TABLE_SIMPLENOTES, values, COLUMN_ID + "=\"0\"", null);
                    }
                }
                else
                {
                    values.put(COLUMN_LASTROW, "0");
                    values.put(COLUMN_NOTES, "");
                    db.insert(TABLE_SIMPLENOTES, null, values);
                    c.close();
                    db.close();
                    return "0";
                }
                /* The following block of code is used to determine how many rows are in the database and to add new
                blank rows if the row that we want to add does not yet exist.
                 */
                query = "SELECT * FROM " + TABLE_SIMPLENOTES + " WHERE 1";
                Cursor c2 = db.rawQuery(query, null);
                c2.moveToFirst();
                int maxVal = c2.getCount();
                c2.close();
                Log.d("we6jbo", String.valueOf(maxVal));
                int count = 0;
                if (toLocation <= fromLocation)
                    count = toLocation + 1;
                else
                    count = fromLocation + 1;

                while(maxVal < count) {
                    maxVal++;
                    values.put(COLUMN_LASTROW, "");
                    values.put(COLUMN_NOTES, "Blank");
                    db.insert(TABLE_SIMPLENOTES, null, values);
                }
                query = "SELECT * FROM " + TABLE_SIMPLENOTES + " WHERE " + COLUMN_ID + " = " + toLocation;
                Cursor c3 = db.rawQuery(query,null);
                c3.moveToFirst();
                String note = c3.getString(c3.getColumnIndex(COLUMN_NOTES));
                values.put(COLUMN_NOTES, message);
                db.update(TABLE_SIMPLENOTES, values, COLUMN_ID + " = \"" + fromLocation + "\"", null);
                db.close();
                return note;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return message + " -- Error 11/24/15";
        }
        return "-99";
    }
}