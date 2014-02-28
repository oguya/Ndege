package com.droid.ndege.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.ndege.R;
import com.droid.ndege.constants.Constants;
import com.droid.ndege.db.DBAdapter;
import com.droid.ndege.model.Tag;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by james on 27/02/14.
 */
public class TagDetailsActivity extends ActionBarActivity {

    private static final String LOG_TAG = "TagDetailsActivity";

    private ActionBar actionBar;
    private ShareActionProvider shareActionProvider;

    private ImageLoader imageLoader;
    private ImageLoaderConfiguration imageLoaderConfig;
    private DisplayImageOptions displayImageOptions;

    private static int TAG_ID = 0;
    private ArrayList<Tag> tagDetails;
    private DBAdapter dbAdapter;

    private ImageView bird_img_thumbnail;
    private Button moreInfo_BTN;
    private TextView bird_engName_TXT;
    private TextView tag_genspecName_TXT;
    private TextView tagdetail_recorder_TXT;
    private TextView tagdetail_country_TXT;
    private TextView tagdetail_location_TXT;
    private TextView tagdetail_soundtype_TXT;
    private TextView tagdetail_tagdate;
    private TextView tagdetail_tagloc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagdetails);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        try{
            Intent intent = getIntent();
            TAG_ID = intent.getExtras().getInt(Constants.KEY_TAG_ID);
            Log.e(LOG_TAG, "Tag ID: getintent:" + TAG_ID);
        }catch (NullPointerException npe){
            Log.e(LOG_TAG, "TAG_ID null: "+npe.getMessage());
        }

        //activity resume
        if(savedInstanceState != null){
            TAG_ID = savedInstanceState.getInt(Constants.KEY_TAG_ID);
            Log.e(LOG_TAG, "Tag ID: savedInstance:" + TAG_ID);
        }

        tagDetails = dbAdapter.getTag(TAG_ID);

        initUI();
        initImageLoader();
        setData();
    }

    public void initUI(){
        bird_img_thumbnail = (ImageView)findViewById(R.id.bird_img_thumbnail);
        moreInfo_BTN = (Button)findViewById(R.id.tagdetail_MoreInfo_BTN);
        bird_engName_TXT = (TextView)findViewById(R.id.bird_engName);
        tag_genspecName_TXT = (TextView)findViewById(R.id.tag_genspecName_TXT);
        tagdetail_recorder_TXT = (TextView)findViewById(R.id.tagdetail_recorder_TXT);
        tagdetail_country_TXT = (TextView)findViewById(R.id.tagdetail_country_TXT);
        tagdetail_location_TXT = (TextView)findViewById(R.id.tagdetail_location_TXT);
        tagdetail_soundtype_TXT = (TextView)findViewById(R.id.tagdetail_soundtype_TXT);
        tagdetail_tagdate = (TextView)findViewById(R.id.tagdetail_tagdate);
        tagdetail_tagloc = (TextView)findViewById(R.id.tagdetail_tagloc);
    }
    public void setData(){
        String imageURL = tagDetails.get(0).getThumbnailURL();
        String engName = tagDetails.get(0).getEnglishName();
        String genSpecName = tagDetails.get(0).getGenericName() + " "+tagDetails.get(0).getSpecificName();
        String recorder = tagDetails.get(0).getRecorder();
        String country = tagDetails.get(0).getCountry();
        String location = tagDetails.get(0).getLocation();
        String soundType = tagDetails.get(0).getSoundType();
        String tagDate = tagDetails.get(0).getTagDate();

        imageLoader.displayImage(imageURL, bird_img_thumbnail, displayImageOptions);
        bird_engName_TXT.setText(engName);
        tag_genspecName_TXT.setText(genSpecName);
        tagdetail_recorder_TXT.setText(recorder);
        tagdetail_country_TXT.setText(country);
        tagdetail_location_TXT.setText(location);
        tagdetail_soundtype_TXT.setText(soundType);
        tagdetail_tagdate.setText(formatDate(tagDate));

    }

    public void initImageLoader(){
        int[] screenSize = getScreenSize();
        int screenWidth = screenSize[0];
        int screenHeight = screenSize[1];

        imageLoader  = ImageLoader.getInstance();
        imageLoaderConfig = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(screenSize[0], screenSize[1])
                .discCacheExtraOptions(screenWidth, screenHeight, Bitmap.CompressFormat.JPEG, 75, null)
                .writeDebugLogs()
                .build();
        imageLoader.init(imageLoaderConfig);

        displayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.bird_brown_cute)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .showImageOnFail(R.drawable.ic_launcher)
                .build();
    }

    public void handleClickEvents(View view){
        switch (view.getId()){
            case R.id.tagdetail_MoreInfo_BTN:
                String xenoCantoURL = "http://xeno-canto.org/98676";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(xenoCantoURL));
                startActivity(intent);
                break;

            default: break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tagdetails, menu);

        //shareAction provider
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        shareActionProvider.setShareIntent(getDefaultShareIntent());

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "settings...comming soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_share:
                Toast.makeText(this, "share...comming soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_delete:
                Toast.makeText(this, "delete...comming soon", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();

        dbAdapter.close();
    }

    @Override
    public void onResume(){
        super.onResume();

        dbAdapter.open();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        TAG_ID = savedInstanceState.getInt(Constants.KEY_TAG_ID);
        Log.e(LOG_TAG, "restoring tagID: "+TAG_ID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.e(LOG_TAG, "saving tagID: "+TAG_ID);
        outState.putInt(Constants.KEY_TAG_ID, TAG_ID);
    }

    private Intent getDefaultShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        return intent;
    }

    public int[] getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();

        return new int[]{display.getWidth(), display.getHeight()};
    }

    //Thursday 27, Feb 2014
    public String formatDate(String strDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd, MMM yyyy");
        SimpleDateFormat tagDateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        Date now = new Date();
        Date tagDate;
        String tagDateStr = "";
        try {
            tagDate = tagDateFormat.parse(strDate);

            if(tagDate.getYear() != now.getYear()){
                dateFormat = new SimpleDateFormat("EEEE dd, MMM yyyy");
                tagDateStr = dateFormat.format(tagDate);
            }else{
                tagDateStr = dateFormat.format(tagDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            tagDateStr = dateFormat.format(now);
        }
        return tagDateStr;
    }
}
