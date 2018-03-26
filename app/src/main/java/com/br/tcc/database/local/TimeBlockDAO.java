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

public class TimeBlockDAO extends SQLiteOpenHelper {
    private static  String TAG = "TimeBlockDAO";
    private static  String TABLE_NAME = "time_block";
    private static  String TABLE_TIME = "time";
    private static  String COL1 = "id_time_block";
    private static  String COL2 = "id_time";
    private static  String COL3 = "time_start";
    private static  String COL4 = "time_end";
    private static String COL5 = "part";
    private static String COL6 = "availability";

    public TimeBlockDAO(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    public void create() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        String createTable = "CREATE TABLE "+TABLE_NAME+
                " ("+COL1+" INTEGER PRIMARY KEY, "+COL2 + " INTEGER" + TABLE_TIME + "TIME ," +COL3+" TIME ," + COL4 + " TIME ,"+ COL5 + " INTEGER ," + COL6 + " INTEGER); ";
        db.execSQL(createTable);
        db.execSQL("DELETE FROM " +TABLE_NAME);

    }
    public void drop() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " +TABLE_NAME);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean addData(String id_time_block, String id_time, String time_start, String time_end, String part, String availability){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id_time_block);
        contentValues.put(COL2, id_time);
        contentValues.put(COL3, time_start);
        contentValues.put(COL4, time_end);
        contentValues.put(COL5, part);
        contentValues.put(COL6, availability);
        Log.d(TAG, "addData: Adding "+id_time_block+" to "+TABLE_NAME);
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
    public Cursor getDataId(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE id_time="+id;
        Cursor data = db.rawQuery(query, null);
        return data;

    }


}
