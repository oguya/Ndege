package com.droid.ndege.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by james on 03/03/14.
 *
 * A parcelable model to handle audio match results from service to activity
 * contains:
 *          - tagID of the new id'd tag
 *          - tagResults of the audio matching  op.
 *              - 1 => success
 *
 */
public class MatchResult implements Parcelable {

    public static final String TAG_RESULTS = "tagResults";

    private int tagID;
    private int tagResults;

    public MatchResult(){}

    public MatchResult(int tagID, int tagResults){
        this.tagID = tagID;
        this.tagResults = tagResults;
    }

    public MatchResult(Parcel inParcel){
        this.tagID = inParcel.readInt();
        this.tagResults = inParcel.readInt();
    }

    public int getTagID(){
        return this.tagID;
    }

    public void setTagID(int tagID){
        this.tagID = tagID;
    }

    public int getTagResults(){
        return this.tagResults;
    }

    public void setTagResults(int tagResults){
        this.tagResults = tagResults;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int flags) {
        outParcel.writeInt(tagID);
        outParcel.writeInt(tagResults);
    }

    public static final Creator<MatchResult> CREATOR = new Creator<MatchResult>() {
        @Override
        public MatchResult createFromParcel(Parcel source) {
            return new MatchResult(source);
        }

        @Override
        public MatchResult[] newArray(int size) {
            return new MatchResult[size];
        }
    };
}
