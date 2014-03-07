package com.droid.ndege.ui.frags;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.ndege.R;
import com.droid.ndege.audio.RecordBirdSound;
import com.droid.ndege.constants.Constants;
import com.droid.ndege.net.NetOpsService;
import com.droid.ndege.receivers.NetReceiver;
import com.droid.ndege.ui.viewutils.BorderDrawable;

import org.w3c.dom.Text;

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
    TextView tag_results_txt;

    private boolean RECORDING = false;
    private BReceiver bReceiver;
    private AudioRecorder audioRecorder;
    private Timer timer;

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
        tag_results_txt = (TextView)rootView.findViewById(R.id.tag_results);

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
        bReceiver = new BReceiver();
        audioRecorder = new AudioRecorder(activity);
        timer = new Timer(Constants.RECORD_TIME_MILLIS, Constants.RECORD_TIME_INTERVAL_MILLIS);

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.id.tap_img);
//        BorderDrawable drawable = new BorderDrawable(getResources(), bitmap);
//        tag_image.setImageDrawable(drawable);
    }

    //start service to upload file
    public void fireUpService(String wavFile){
        Intent intent = new Intent(activity, NetOpsService.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_FILEPATH, wavFile);

        intent.putExtras(bundle);
        activity.startService(intent);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tap_img:  //start recording, timer
                    tag_results_txt.setVisibility(View.GONE);
                    tag_status_txt.setText(getString(R.string.tag_status_activated));
                    tag_image.setEnabled(false);
                    if(!RECORDING){
                        RECORDING = true;
                        lockOrientation();
                        audioRecorder.startRecorder();
                        timer.start();
                    }



//                    fireUpService("");
                    break;

                default: break;
            }
        }
    };

    public void lockOrientation(){
        int orientation = activity.getResources().getConfiguration().orientation;

        switch (orientation){
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            default:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        activity.registerReceiver(bReceiver, new IntentFilter(Constants.RECEIVER_FILTER));
    }

    @Override
    public void onPause(){
        super.onPause();
        activity.unregisterReceiver(bReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    //set fonts
    public void setFonts(){
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto_Light.ttf");
        tag_status_txt.setTypeface(tf);
        tag_results_txt.setTypeface(tf);
    }

    public class BReceiver extends NetReceiver{

        public BReceiver(){
            super(activity, tag_results_txt, tag_status_txt, tag_image);
        }

    }

    public class AudioRecorder extends RecordBirdSound{

        public AudioRecorder(Activity activity) {
            super(activity);
        }

        public void startRecorder(){
            super.startRecording();
        }

        public void stopRecorder(){
            super.stopRecording();
        }
    }

    private class Timer extends CountDownTimer{

        public Timer(long millisInFuture, long intervalMillis){
            super(millisInFuture, intervalMillis);
        }

        @Override
        public void onTick(long remainingMillis){
            Log.d(LOG_TAG, "Timer: "+remainingMillis);
        }

        @Override
        public void onFinish(){
            audioRecorder.stopRecorder();
            Log.e(LOG_TAG, "Finished Recording: "+audioRecorder.WAV_FILE);
            fireUpService(audioRecorder.WAV_FILE);
        }
    }
}
