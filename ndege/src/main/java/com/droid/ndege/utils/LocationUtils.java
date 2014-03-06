package com.droid.ndege.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by james on 06/03/14.
 */
public class LocationUtils {


    public LocationUtils(){}

    //2014-03-06 11:45:18
    public static String currentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        Date now = new Date();
        return dateFormat.format(now);
    }
}
