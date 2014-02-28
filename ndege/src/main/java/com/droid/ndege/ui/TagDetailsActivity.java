package com.droid.ndege.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.droid.ndege.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private ImageView bird_img_thumbnail;
    private Button moreInfo_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagdetails);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initUI();
        initImageLoader();

        String imageURL = "http://ibc.lynxeds.com/files/pictures/Crested_Guineafowl_LHarding_med.JPG";
        imageLoader.displayImage(imageURL, bird_img_thumbnail, displayImageOptions);
    }

    public void initUI(){
        bird_img_thumbnail = (ImageView)findViewById(R.id.bird_img_thumbnail);
        moreInfo_BTN = (Button)findViewById(R.id.tagdetail_MoreInfo_BTN);

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

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEE dd, MMM yyyy");
        SimpleDateFormat tagDateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        Date now = new Date();
        Date tagDate;
        String tagDateStr = "";
        try {
            tagDate = tagDateFormat.parse(strDate);

            if(tagDate.getYear() != now.getYear()){
                dateFormat = new SimpleDateFormat("EEEEE dd, MMM yyyy");
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
