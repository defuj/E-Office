package com.waditra.app.Alat;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by defuj on 23/08/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "notifikasi.db";
    public static final String TABLE_NAME = "tabel_notifikasi";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "judul";
    public static final String COL_3 = "isi";
    public static final String COL_4 = "tanggal";
    public static final String COL_5 = "jam";
    public static final String COL_6 = "status_terbaca";

    public static final String KEY_ID = "ID";
    public static final String KEY_judul = "judul";
    public static final String KEY_isi = "isi";
    public static final String KEY_waktu = "waktu";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,judul TEXT,isi TEXT,waktu TEXT,jam TEXT,status_terbaca TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String judul, String isi, String tanggal, String jam, String status_terbaca){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,judul);
        contentValues.put(COL_3,isi);
        contentValues.put(COL_4,tanggal);
        contentValues.put(COL_5,jam);
        contentValues.put(COL_6,status_terbaca);
        long result =  db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {
            return false;
        }else {
            return true;
        }
    }
}
