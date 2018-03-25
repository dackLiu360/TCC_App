package com.br.tcc.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.br.tcc.controllers.Time;

import java.util.List;

/**
 * Created by Victor on 1/10/2018.
 */

public class DataDAO extends SQLiteOpenHelper {
    private static  String TAG = "DataDAO";
    private static  String TABLE_NAME = "time";
    private static  String TABLE_USER = "user";
    private static  String COL1 = "id_time";
    private static  String COL2 = "id_user";
    private static  String COL3 = "day";
    private static  String COL4 = "time_start";
    private static String COL5 = "time_end";

    public DataDAO(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_NAME+
                " ("+COL1+" INTEGER PRIMARY KEY, "+COL2 + " INTEGER REFERENCES " + TABLE_USER + "," +COL3+" DATE ," + COL4 + " TIME ," + COL5 + " TIME); ";
        db.execSQL(createTable);

    }
    public void create() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        String createTable = "CREATE TABLE "+TABLE_NAME+
                " ("+COL1+" INTEGER PRIMARY KEY, "+COL2 + " INTEGER REFERENCES " + TABLE_USER + "," +COL3+" DATE ," + COL4 + " TIME ," + COL5 + " TIME); ";
        db.execSQL(createTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean addData(String id_time, String id_user, String day, String time_start, String time_end){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id_time);
        contentValues.put(COL2, id_user);
        contentValues.put(COL3, day);
        contentValues.put(COL4, time_start);
        contentValues.put(COL5, time_end);
        Log.d(TAG, "addData: Adding "+id_time+" to "+TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;

    }


}
