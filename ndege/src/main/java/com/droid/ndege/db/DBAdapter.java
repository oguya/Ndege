package com.droid.ndege.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.droid.ndege.constants.Constants;
import com.droid.ndege.model.BirdImage;
import com.droid.ndege.model.Tag;

import java.text.BreakIterator;
import java.util.ArrayList;

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

    //get all tags
    public ArrayList<Tag> getTagList(){
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        String[] cols = {Tag.TAG_ID, Tag.ENGLISH_NAME, Tag.GENERIC_NAME, Tag.SPECIFIC_NAME};
        Cursor cursor;

        try{
            cursor = db.query(Constants.TBL_TAGS, cols, null, null, null, null, null);
        }catch (SQLiteException ex){
            Log.e(LOG_TAG, "exception "+ex.getMessage());
            return tagList;
        }

        if(cursor.moveToFirst()){
            do{
                Tag tag = new Tag();

                tag.setTagId(cursor.getInt(cursor.getColumnIndex(Tag.TAG_ID)));
                tag.setEnglishName(cursor.getString(cursor.getColumnIndex(Tag.ENGLISH_NAME)));
                tag.setGenericName(cursor.getString(cursor.getColumnIndex(Tag.GENERIC_NAME)));
                tag.setSpecificName(cursor.getString(cursor.getColumnIndex(Tag.SPECIFIC_NAME)));

                tagList.add(tag);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return tagList;
    }

    //get details abt a specific tag
    public ArrayList<Tag> getTag(int tagID){
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        Cursor cursor;
        String[] cols = {Tag.TAG_ID, Tag.BIRD_ID, Tag.ENGLISH_NAME, Tag.GENERIC_NAME, Tag.SPECIFIC_NAME,
                Tag.RECORDER, Tag.LOCATION, Tag.COUNTRY, Tag.LAT, Tag.LNG, Tag.XENOCANTOURL,
                Tag.SOUND_TYPE, Tag.SOUND_URL };

        try{
            cursor = db.query(Constants.TBL_TAGS, cols, Tag.TAG_ID+" =? ", new String[]{String.valueOf(tagID)},
                    null, null, null);
        }catch (SQLiteException ex){
            Log.e(LOG_TAG, "exception "+ex.getMessage());
            return tagList;
        }

        if(cursor.moveToFirst()){

            Tag tag = new Tag();

            tag.setTagId(cursor.getInt(cursor.getColumnIndex(Tag.TAG_ID)));
            tag.setEnglishName(cursor.getString(cursor.getColumnIndex(Tag.ENGLISH_NAME)));
            tag.setGenericName(cursor.getString(cursor.getColumnIndex(Tag.GENERIC_NAME)));
            tag.setSpecificName(cursor.getString(cursor.getColumnIndex(Tag.SPECIFIC_NAME)));
            tag.setRecorder(cursor.getString(cursor.getColumnIndex(Tag.RECORDER)));
            tag.setLocation(cursor.getString(cursor.getColumnIndex(Tag.LOCATION)));
            tag.setCountry(cursor.getString(cursor.getColumnIndex(Tag.COUNTRY)));
            tag.setLat(cursor.getString(cursor.getColumnIndex(Tag.LAT)));
            tag.setLng(cursor.getString(cursor.getColumnIndex(Tag.LNG)));
            tag.setXenoCantoURL(cursor.getString(cursor.getColumnIndex(Tag.XENOCANTOURL)));
            tag.setSoundType(cursor.getString(cursor.getColumnIndex(Tag.SOUND_TYPE)));
            tag.setSoundURL(cursor.getString(cursor.getColumnIndex(Tag.SOUND_URL)));

            tagList.add(tag);
        }
        cursor.close();
        return tagList;
    }

    public ArrayList<BirdImage> getImages(int tagID){
        ArrayList<BirdImage> imageList = new ArrayList<BirdImage>();
        String[] cols = {BirdImage.TAG_ID, BirdImage.IMAGE_URL, BirdImage.SITE_URL};
        Cursor cursor;

        try{
            cursor = db.query(Constants.TBL_IMAGES, cols, BirdImage.TAG_ID+" =? ",
                    new String[]{String.valueOf(tagID)}, null, null, null);
        }catch (SQLiteException ex){
            Log.e(LOG_TAG, "exception "+ex.getMessage());
            return imageList;
        }

        if(cursor.moveToFirst()){
            do{
                BirdImage image = new BirdImage();

                image.setTagID(cursor.getInt(cursor.getColumnIndex(BirdImage.TAG_ID)));
                image.setImageURL(cursor.getString(cursor.getColumnIndex(BirdImage.IMAGE_URL)));
                image.setSiteURL(cursor.getString(cursor.getColumnIndex(BirdImage.TAG_ID)));

                imageList.add(image);

            }while (cursor.moveToNext());
        }

        cursor.close();
        return imageList;
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

