package com.droid.ndege.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.droid.ndege.constants.Constants;

/**
 * Created by james on 21/02/14.
 */
public class DBAdapter {

    private static final String LOG_TAG = "dbAdapter";

    private Context context;
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public DBAdapter(Context context){
        this.context = context;

        dbHelper = new DBHelper(context);
    }

    public DBAdapter open(){
        db = dbHelper.getWritableDatabase();

        //force foreign key checks
        try{
            db.execSQL(Constants.FORCE_FOREIGN_KEY_CHECKS);
        }catch (NullPointerException npe){
            Log.e(LOG_TAG, "foreign key checks:: " + npe.getMessage());
        }
        return this;
    }

    public void close(){
        db.close();
    }



    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context){
            super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

