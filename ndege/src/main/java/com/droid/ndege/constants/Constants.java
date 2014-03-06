package com.droid.ndege.constants;

/**
 * Created by james on 21/02/14.
 *
 * Hold all constants
 *
 */

public class Constants {

    //main activity
    public static final String[] TABS_TITLE = {"Tag", "My Tags"};

    //DB consts
    public static final String DB_NAME = "ndege.sqlite";
    public static final int DB_VERSION = 1;
    public static final String DB_DIR = "/data/data/com.droid.ndege/databases/";
    public static final String FORCE_FOREIGN_KEY_CHECKS = "PRAGMA foreign_keys = ON";
    public static final String TBL_IMAGES = "images";
    public static final String TBL_TAGS = "tags";

    //viewTag frags
    public static final String KEY_TAG_ID = "TAG_ID";

    //net services & receivers
    public static final String RECEIVER_FILTER = "com.droid.ndege.ui.TagDetailsActivity";
    public static final String KEY_FILEPATH = "filePath";
    public static final int RESULT_OK = 1;
    public static final int RESULT_FAILED = 0;

    public static final String KEY_MATCH_RESULT = "matchResult";
    public static final int TAG_RESULT_OK = 1;
    public static final int TAG_RESULT_FAILED = 0;
    public static final int TAG_NET_ERROR = -1;
    public static final int TAG_DB_ERROR = -100;

    //backend server url args
    public static final String BG_SVR_URL_TAG_FILE = "http://127.0.0.1/";
    public static final String BG_TAG_AUDIO_FILE = "audiofile";
    public static final String BG_DEVICE_ID = "deviceid";

}
