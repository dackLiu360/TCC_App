package com.br.tcc.database.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Victor on 1/10/2018.
 */

public class UserDAO extends SQLiteOpenHelper {
    private static final String TAG = "UserDAO";
    private static final String TABLE_NAME = "user";
    private static final String COL1 = "user_id";
    private static final String COL2 = "username";
    private static final String COL3 = "email";

    public UserDAO(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_NAME+
                " ("+COL1+" INTEGER PRIMARY KEY, "+COL2+" VARCHAR(30) ,"+ COL2+" VARCHAR(30) ,"+COL3+" VARCHAR(30));";
        db.execSQL(createTable);

    }
    public void create() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        String createTable = "CREATE TABLE "+TABLE_NAME+
                " ("+COL1+" INTEGER PRIMARY KEY, "+COL2+" VARCHAR(30) ,"+COL3+" VARCHAR(30));";
        db.execSQL(createTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String user_id, String username, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, user_id);
        contentValues.put(COL2, username);
        contentValues.put(COL3, email);
        Log.d(TAG, "addData: Adding "+user_id+" to "+TABLE_NAME);
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
