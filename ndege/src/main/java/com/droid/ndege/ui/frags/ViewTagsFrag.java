package com.droid.ndege.ui.frags;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.ndege.R;
import com.droid.ndege.adapters.TagListAdapter;
import com.droid.ndege.constants.Constants;
import com.droid.ndege.db.DBAdapter;
import com.droid.ndege.model.BirdImage;
import com.droid.ndege.model.Tag;
import com.droid.ndege.ui.TagDetailsActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import roboguice.fragment.RoboFragment;

/**
 * Created by james on 10/02/14.
 */
public class ViewTagsFrag extends Fragment {

    private final static String LOG_TAG = "ViewTagsFrag";
    private Activity context;

    private ListView listView;
    private TextView noTags_TXT;
    private DBAdapter dbAdapter;
    private ArrayList<Tag> tagList;
    private ArrayList<BirdImage> imageList;

    public ViewTagsFrag(){}

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        this.context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.frag_view_tags, container, false);

        //init ui here
        listView = (ListView)rootView.findViewById(R.id.tagList);
        noTags_TXT = (TextView)rootView.findViewById(R.id.no_items_txt);
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

        dbAdapter = new DBAdapter(context);
        dbAdapter.open();

        tagList = dbAdapter.getTagList();

        //check for empty lists
        if(tagList.size() <= 0){
            noTags_TXT.setVisibility(View.VISIBLE);
        }else{
            listView.setAdapter(new TagListAdapter(context, tagList));
        }
        listView.setOnItemClickListener(listViewListener);

    }

    private AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), "item click: ", Toast.LENGTH_SHORT).show();

            //store tagID and birdID in bundle..start tagdetails activity
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_TAG_ID, tagList.get(position).getTagID());
            Intent intent = new Intent(context, TagDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };

    @Override
    public void onResume(){
        super.onResume();

        dbAdapter.open();
    }

    @Override
    public void onPause(){
        super.onPause();

        dbAdapter.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }
}
