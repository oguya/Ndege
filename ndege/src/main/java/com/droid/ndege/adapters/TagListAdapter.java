package com.droid.ndege.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.ndege.R;
import com.droid.ndege.lazylist.ImageLoader;
import com.droid.ndege.model.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by james on 27/02/14.
 */
public class TagListAdapter extends ArrayAdapter<Tag> {

    private Activity context;
    private ArrayList<Tag> tagList;
    public com.droid.ndege.lazylist.ImageLoader imageLoader;
    private int lastPosition = -1;

    private static class ViewHolder{
        ImageView birdImg;
        TextView englishName_TXT;
        TextView genSpecName_TXT;
        TextView tagDate_TXT;
    }

    public TagListAdapter(Activity context, ArrayList<Tag> tagList){
        super(context, R.layout.tags_row_layout, tagList);

        this.context = context;
        this.tagList = tagList;

        imageLoader = new ImageLoader(context.getApplicationContext());
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View rowView  = convertView;

        if(rowView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.tags_row_layout, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.birdImg = (ImageView)rowView.findViewById(R.id.bird_img);
            viewHolder.englishName_TXT = (TextView)rowView.findViewById(R.id.taglist_english_name);
            viewHolder.genSpecName_TXT = (TextView)rowView.findViewById(R.id.taglist_genus_spec_name);
            viewHolder.tagDate_TXT = (TextView)rowView.findViewById(R.id.taglist_tagDate);
            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder)rowView.getTag();

        //TODO get imageURL

        String englishName = tagList.get(position).getEnglishName();
        String genericName = tagList.get(position).getGenericName();
        String specificName = tagList.get(position).getSpecificName();
        String tagDate = tagList.get(position).getTagDate();
        String thumbnailURL = tagList.get(position).getThumbnailURL();

        viewHolder.birdImg.setImageResource(R.drawable.bird_brown_cute);
        imageLoader.DisplayImage(tagList.get(position).getThumbnailURL(), viewHolder.birdImg);
        viewHolder.englishName_TXT.setText(englishName);
        viewHolder.genSpecName_TXT.setText(genericName +" "+specificName);
        viewHolder.tagDate_TXT.setText(formatDate(tagDate));

        //TODO add list scroll animation
        Animation animation = AnimationUtils.loadAnimation(this.context, (position > lastPosition) ?
                R.anim.up_from_bottom : R.anim.down_from_top);
        rowView.startAnimation(animation);
        lastPosition = position;
        return rowView;
    }

    //Thur 27, Feb
    public String formatDate(String strDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd, MMM");
        SimpleDateFormat tagDateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        Date now = new Date();
        Date tagDate;
        String tagDateStr = "";
        try {
            tagDate = tagDateFormat.parse(strDate);

            if(tagDate.getYear() != now.getYear()){
                dateFormat = new SimpleDateFormat("EEE dd, MMM yyyy");
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
