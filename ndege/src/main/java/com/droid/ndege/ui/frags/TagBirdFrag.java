package com.droid.ndege.ui.frags;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.ndege.R;
import com.droid.ndege.ui.viewutils.BorderDrawable;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Created by james on 10/02/14.
 */
public class TagBirdFrag extends Fragment {

    private static final String LOG_TAG = "TagBirdFrag";

    private Activity activity;

    ImageView tag_image;
    TextView tag_status_txt;

    private boolean RECORDING = false;

    public TagBirdFrag(){}

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.frag_tag_bird, container, false);

        //init ui here
        tag_image = (ImageView)rootView.findViewById(R.id.tap_img);
        tag_image.setOnClickListener(clickListener);
        tag_status_txt = (TextView)rootView.findViewById(R.id.tag_status);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    //change ui stuff..frag is live
    @Override
    public void onStart(){
        super.onStart();

        setFonts();
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.id.tap_img);
//        BorderDrawable drawable = new BorderDrawable(getResources(), bitmap);
//        tag_image.setImageDrawable(drawable);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tap_img:  //start recording
                    RECORDING = true;
                    tag_status_txt.setText(getString(R.string.tag_status_activated));
                    break;

                default: break;
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    //set fonts
    public void setFonts(){
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto_Light.ttf");
        tag_status_txt.setTypeface(tf);
    }
}
