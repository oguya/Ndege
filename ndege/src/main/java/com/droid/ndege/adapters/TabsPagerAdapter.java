package com.droid.ndege.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.droid.ndege.ui.frags.TagBirdFrag;
import com.droid.ndege.ui.frags.ViewTagsFrag;

/**
 * Created by james on 10/02/14.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index){
            case 0:
                //start tag bird frag
                return new TagBirdFrag();
            case 1:
                //start view tags frag
                return new ViewTagsFrag();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        //no of tabs
        return 2;
    }
}
