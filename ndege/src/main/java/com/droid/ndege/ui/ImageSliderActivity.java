package com.droid.ndege.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.ndege.R;
import com.droid.ndege.adapters.ImageSliderAdapter;
import com.droid.ndege.constants.Constants;
import com.droid.ndege.db.DBAdapter;
import com.droid.ndege.model.BirdImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by james on 28/02/14.
 */
public class ImageSliderActivity extends ActionBarActivity {

    private static final String LOG_TAG = "ImageSliderActivity";
    private ActionBar actionBar;
    private ViewPager viewPager;
    private ImageSliderAdapter adapter;
    private TextView no_pics_TXT;
    private ArrayList<BirdImage> imageURLList;

    private ImageLoader imageLoader;
    private ImageLoaderConfiguration imageLoaderConfig;
    private DisplayImageOptions displayImageOptions;
    private DBAdapter dbAdapter;

    private static int TAG_ID = 0;
    private int IMAGE_POS = 0;
    private static final String KEY_IMAGE_POS = "IMAGE_POS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_imageslider);

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

        imageURLList = dbAdapter.getImages(TAG_ID);
        initImageLoader();
        initUI();

        if(imageURLList.size() <= 0){
            noPics();
        }
    }

    private void initUI(){

        viewPager = (ViewPager)findViewById(R.id.pager);
        adapter = new ImageSliderAdapter(this, imageURLList, imageLoader, displayImageOptions);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(IMAGE_POS);
        no_pics_TXT = (TextView)findViewById(R.id.no_items_txt);
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
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.ic_launcher)
                .build();
    }

    private ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String s, View view) {
            ((ImageView)view).setImageResource(R.drawable.img_loading);
        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {
            ((ImageView)view).setImageResource(R.drawable.failed);
        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            ((ImageView)view).setImageBitmap(bitmap);
        }

        @Override
        public void onLoadingCancelled(String s, View view) {
            ((ImageView)view).setImageResource(R.drawable.failed);
        }
    };

    public void noPics(){
        no_pics_TXT.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.imageslider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPause(){
        super.onPause();
        IMAGE_POS = viewPager.getCurrentItem();
        dbAdapter.close();
    }


    @Override
    public void onResume(){
        super.onResume();
        viewPager.setCurrentItem(IMAGE_POS);
        dbAdapter.open();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_IMAGE_POS, IMAGE_POS);
        outState.putInt(Constants.KEY_TAG_ID, TAG_ID);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        IMAGE_POS = savedInstanceState.getInt(KEY_IMAGE_POS);
        TAG_ID = savedInstanceState.getInt(Constants.KEY_TAG_ID);
    }

    public int[] getScreenSize(){
        Display display = getWindowManager().getDefaultDisplay();
        return new int[]{display.getWidth(), display.getHeight()};
    }

}
