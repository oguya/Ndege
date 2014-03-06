package com.droid.ndege.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.droid.ndege.R;
import com.droid.ndege.model.BirdImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by james on 28/02/14.
 */
public class ImageSliderAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<BirdImage> imageURLList;
    private LayoutInflater layoutInflater;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private ProgressBar progressBar;

    public ImageSliderAdapter(Activity activity, ArrayList<BirdImage> imageURLList,
                              ImageLoader imageLoader, DisplayImageOptions displayImageOptions ){
        this.activity = activity;
        this.imageURLList = imageURLList;

        this.imageLoader = imageLoader;
        this.displayImageOptions = displayImageOptions;
    }

    @Override
    public int getCount() {
        return this.imageURLList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        ImageView imgDisplay;
        Button closeBTN;

        layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = layoutInflater.inflate(R.layout.imageslider_item, container, false);
        imgDisplay = (ImageView)viewLayout.findViewById(R.id.imgDisplay);
        closeBTN = (Button)viewLayout.findViewById(R.id.btnClose);
        progressBar = (ProgressBar)viewLayout.findViewById(R.id.progLoading);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeFile(imageURLList.get(position), options);
//        imgDisplay.setImageBitmap(bitmap);

        String imageURL = imageURLList.get(position).getImageURL();
        imageLoader.displayImage(imageURL, imgDisplay, displayImageOptions, imageLoadingListener);

        //close button click event
        closeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        ((ViewPager)container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    private ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String s, View view) {
            ((ImageView)view).setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {
            ((ImageView)view).setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            ((ImageView)view).setImageBitmap(bitmap);
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingCancelled(String s, View view) {
            if(view != null)
                ((ImageView)view).setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    };

}
