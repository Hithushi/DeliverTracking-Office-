package com.example.delivertracking.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.delivertracking.Model.ListItem;

import java.util.ArrayList;
import java.util.HashSet;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "myTable";
    public static final String DATABASE_NAME = "qrDb.db";

    public static final String COL_CODE = "code";
    public static final String COL_TIME = "time";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+" code TEXT, time TEXT)");
        db.execSQL("create unique index idx_code on "+TABLE_NAME+"(code)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String code, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CODE,code);
        contentValues.put(COL_TIME,time);

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_NAME,null,contentValues);

        return result != -1;
    }

    public ArrayList<ListItem> getAllInformation() {
        ArrayList<ListItem> arrayList = new ArrayList<>();
        HashSet<ListItem> hashSet = new HashSet<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from "+TABLE_NAME,null);

        if (cursor!=null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String code = cursor.getString(1);
                String time = cursor.getString(2);

                ListItem listItem = new ListItem(id,code,time);


                arrayList.add(listItem);
                hashSet.addAll(arrayList);
                arrayList.clear();
                arrayList.addAll(hashSet);
            }
        }
        assert cursor != null;
        cursor.close();
        return arrayList;
    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
    }
}
