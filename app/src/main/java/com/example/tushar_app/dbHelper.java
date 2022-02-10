package com.example.tushar_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.tushar_app.names.*;

import androidx.annotation.Nullable;

import java.time.DayOfWeek;

public class dbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="finalReminder.db";
    public static final int DATABASE_VERSION=1;
    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_REMINDERS="CREATE TABLE "+
                Entries.TABLE_NAME+" ("+
                Entries._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Entries.COLUMN_TITLE+" TEXT NOT NULL, "+
                Entries.COLUMN_DATE+" TEXT NOT NULL, "+
                Entries.COLUMN_TIME+" TEXT NOT NULL"+
                ");";
        db.execSQL(CREATE_TABLE_REMINDERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Entries.TABLE_NAME);
        onCreate(db);
    }
   public Cursor getallreminders() {
       SQLiteDatabase database = this.getWritableDatabase();
        String query = "select * from tbl_reminder order by id desc";                               //Sql query to  retrieve  data from the database
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
   }
    public Cursor getAllItems(){
        SQLiteDatabase database;
        database = this.getWritableDatabase();
        return database.query(
                names.Entries.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
