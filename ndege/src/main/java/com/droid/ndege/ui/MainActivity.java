package com.droid.ndege.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.droid.ndege.R;
import com.droid.ndege.adapters.TabsPagerAdapter;
import com.droid.ndege.constants.Constants;
import com.droid.ndege.db.DBAdapter;
import com.droid.ndege.utils.FirstRunInit;

public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = "MainActivity";

    private ViewPager viewPager;
    private TabsPagerAdapter tabsPagerAdapter;
    private ActionBar actionBar;

    public DBAdapter dbAdapter;
    private FirstRunInit firstRunInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check firstrun
        checkFirstRun();

        //init pagers
        viewPager = (ViewPager)findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabsPagerAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String tab: Constants.TABS_TITLE)
            actionBar.addTab(actionBar.newTab().setText(tab).setTabListener(tabListener));

        viewPager.setOnPageChangeListener(pageChangeListener);
    }

    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    };

    //select the respective tab
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int position) {
            actionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    private void checkFirstRun(){
        SharedPreferences firstRunPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstRun = firstRunPrefs.getBoolean("FirstRun", true);

        if(firstRun){
            //copy db
            Log.e(LOG_TAG, "First Run! initializing resources...");
            firstRunInit = new FirstRunInit(this);
            firstRunInit.copyDBFile();

            firstRunPrefs.edit().putBoolean("FirstRun", false).commit();
        }else{
            Log.e(LOG_TAG, "First Run! all assets GREEN...");

        }
    }

}
