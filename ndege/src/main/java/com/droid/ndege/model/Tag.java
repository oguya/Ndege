package com.droid.ndege.model;

/**
 * Created by james on 27/02/14.
 */
public class Tag {

    public static final String TAG_ID = "tagID";
    public static final String BIRD_ID  = "birdID";
    public static final String ENGLISH_NAME = "englishName";
    public static final String GENERIC_NAME = "genericName";
    public static final String SPECIFIC_NAME = "specificName";
    public static final String RECORDER = "Recorder";
    public static final String LOCATION = "Location";
    public static final String COUNTRY = "Country";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String XENOCANTOURL = "xenoCantoURL";
    public static final String SOUND_TYPE = "soundType";
    public static final String SOUND_URL = "soundURL";
    public static final String THUMBNAIL_URL = "thumbnailURL";
    public static final String TAG_DATE = "tagDate";
    public static final String TAG_LOCATION = "tagLocation";

    private int tagID;
    private int birdID;
    private String englishName;
    private String genericName;
    private String specificName;
    private String recorder;
    private String location;
    private String country;
    private String lat;
    private String lng;
    private String xenoCantoURL;
    private String soundType;
    private String soundURL;
    private String thumbnailURL;
    private String tagDate;
    private String tagLocation;

    public Tag(){}

    public Tag(int tagID, int birdID, String englishName, String genericName, String thumbnailURL,
               String specificName, String recorder, String location, String country,
               String lat, String lng, String xenoCantoURL, String soundType, String soundURL,
               String tagDate, String tagLocation ){

        this.tagID = tagID;
        this.birdID = birdID;
        this.englishName = englishName;
        this.genericName = genericName;
        this.specificName = specificName;
        this.recorder = recorder;
        this.location = location;
        this.country = country;
        this.lat = lat;
        this.lng = lng;
        this.xenoCantoURL = xenoCantoURL;
        this.soundType = soundType;
        this.soundURL = soundURL;
        this.thumbnailURL = thumbnailURL;
        this.tagDate = tagDate;
        this.tagLocation = tagLocation;
    }

    public int getTagID(){
        return this.tagID;
    }

    public void setTagId(int tagID){
        this.tagID  = tagID;
    }

    public int getBirdID(){
        return this.birdID;
    }

    public void setBirdId(int birdId){
        this.birdID = birdId;
    }

    public String getEnglishName(){
        return this.englishName;
    }

    public void setEnglishName(String englishName){
        this.englishName = englishName;
    }

    public String getGenericName(){
        return this.genericName;
    }

    public void setGenericName(String genericName){
        this.genericName = genericName;
    }

    public String getSpecificName(){
        return this.specificName;
    }

    public void setSpecificName(String specificName){
        this.specificName = specificName;
    }

    public String getRecorder(){
        return this.recorder;
    }

    public void setRecorder(String recorder){
        this.recorder = recorder;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getCountry(){
        return this.country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getLat(){
        return this.lat;
    }

    public void setLat(String lat){
        this.lat = lat;
    }

    public String getLng(){
        return this.lng;
    }

    public void setLng(String lng){
        this.lng = lng;
    }

    public String getXenoCantoURL(){
        return this.xenoCantoURL;
    }

    public void setXenoCantoURL(String xenoCantoURL){
        this.xenoCantoURL = xenoCantoURL;
    }

    public String getSoundType(){
        return this.soundType;
    }

    public void setSoundType(String soundType){
        this.soundType = soundType;
    }

    public String getSoundURL(){
        return this.soundURL;
    }

    public void setSoundURL(String soundURL){
        this.soundURL = soundURL;
    }

    public String getThumbnailURL(){
        return this.thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL){
        this.thumbnailURL = thumbnailURL;
    }

    public String getTagDate(){
        return this.tagDate;
    }

    public void setTagDate(String tagDate){
        this.tagDate = tagDate;
    }

    public String getTagLocation(){
        return this.tagLocation;
    }

    public void setTagLocation(String tagLocation){
        this.tagLocation = tagLocation;
    }

}

