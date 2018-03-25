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

public class TasksDAO extends SQLiteOpenHelper {
    private static  String TAG = "TasksDAO";
    private static  String TABLE_NAME = "task";
    private static  String TABLE_USER = "user";
    private static  String COL1 = "id_task";
    private static  String COL2 = "id_user";
    private static  String COL3 = "title";
    private static  String COL4 = "subject";
    private static  String COL5 = "description";
    private static  String COL6 = "estimated_time";
    private static  String COL7 = "deadline";
    private static  String COL8 = "progress";

    public TasksDAO(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    public void create() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE  IF EXISTS " +TABLE_NAME);
        String createTable = "CREATE TABLE "+TABLE_NAME+
                " ("+COL1+" INTEGER PRIMARY KEY, "+COL2 + " INTEGER REFERENCES " + TABLE_USER + ","+COL3+" VARCHAR(30) ,"+COL4+" VARCHAR(30) ,"+COL5+" VARCHAR(30) ,"+COL6+" VARCHAR(30) ,"+COL7+" VARCHAR(30) ,"+COL8+" VARCHAR(30)); ";
        db.execSQL(createTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean addData(String id_task, String id_user, String title, String subject, String description, String estimated_time, String deadline, String progress){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id_task);
        contentValues.put(COL2, id_user);
        contentValues.put(COL3, title);
        contentValues.put(COL4, subject);
        contentValues.put(COL5, description);
        contentValues.put(COL6, estimated_time);
        contentValues.put(COL7, deadline);
        contentValues.put(COL8, progress);
        Log.d(TAG, "addData: Adding "+id_task+" to "+TABLE_NAME);
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
