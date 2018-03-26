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
    private static final String TABLE_NAME = "userinfo";
    private static final String COL1 = "user_id";
    private static final String COL2 = "name";
    private static final String COL3 = "username";
    private static final String COL4 = "email";
    private static final String COL5 = "password";

    public UserDAO(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+TABLE_NAME+
                " ("+COL1+" INTEGER PRIMARY KEY, "+COL2+" VARCHAR(20) ,"+COL3+" VARCHAR(16) , " +COL4+" VARCHAR(30) ," +COL5+" VARCHAR(16));";
        db.execSQL(createTable);

    }
    public void create() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE  IF EXISTS " +TABLE_NAME);
        String createTable = "CREATE TABLE "+TABLE_NAME+
                " ("+COL1+" INTEGER PRIMARY KEY , "+COL2+" VARCHAR(20) ,"+COL3+" VARCHAR(16), " +COL4+" VARCHAR(30) ," +COL5+" VARCHAR(16));";
        db.execSQL(createTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String user_id, String username, String email, String name, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, user_id);
        contentValues.put(COL2, name);
        contentValues.put(COL3, username);
        contentValues.put(COL4, email);
        contentValues.put(COL5, password);
        Log.d(TAG, "addData: Adding "+user_id+" to "+TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }

    public boolean findByUsername(String username){
        boolean exists=false;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM "+ TABLE_NAME +" WHERE "+ COL3 +" = " + username + ";";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor!=null && cursor.getCount()>0) exists=true;
        cursor.close();
        db.close();

        return exists;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;

    }


}
