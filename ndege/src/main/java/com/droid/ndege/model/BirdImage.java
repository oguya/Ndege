package com.droid.ndege.model;

/**
 * Created by james on 27/02/14.
 */
public class BirdImage {

    public static final String IMAGES_LBL = "images";
    public static final String IMAGE_ID = "imageID";
    public static final String TAG_ID = "tagID";
    public static final String IMAGE_URL = "imageURL";
    public static final String SITE_URL = "siteURL";

    private int imageID;
    private int tagID;
    private String imageURL;
    private String siteURL;

    public BirdImage(){}

    public BirdImage(int imageID, int tagID, String imageURL, String siteURL){
        this.imageID = imageID;
        this.tagID = tagID;
        this.imageURL = imageURL;
        this.siteURL = siteURL;
    }

    public int getImageID(){
        return this.imageID;
    }

    public void setImageID(int imageID){
        this.imageID = imageID;
    }

    public int getTagID(){
        return this.tagID;
    }

    public void setTagID(int tagID){
        this.tagID = tagID;
    }

    public String getImageURL(){
        return this.imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }

    public String getSiteURL(){
        return this.siteURL;
    }

    public void setSiteURL(String siteURL){
        this.siteURL = siteURL;
    }
}
