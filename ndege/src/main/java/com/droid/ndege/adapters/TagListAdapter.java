package com.droid.ndege.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.ndege.R;
import com.droid.ndege.model.Tag;

import java.util.ArrayList;

/**
 * Created by james on 27/02/14.
 */
public class TagListAdapter extends ArrayAdapter<Tag> {

    private Activity context;
    private ArrayList<Tag> tagList;

    private static class ViewHolder{
        ImageView birdImg;
        TextView englishName_TXT;
        TextView genSpecName_TXT;
    }

    public TagListAdapter(Activity context, ArrayList<Tag> tagList){
        super(context, R.layout.tags_row_layout, tagList);

        this.context = context;
        this.tagList = tagList;
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

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder)rowView.getTag();

        //TODO get imageURL

        String englishName = tagList.get(position).getEnglishName();
        String genericName = tagList.get(position).getGenericName();
        String specificName = tagList.get(position).getSpecificName();

        viewHolder.birdImg.setImageResource(R.drawable.bird_brown_cute);
        viewHolder.englishName_TXT.setText(englishName);
        viewHolder.genSpecName_TXT.setText(genericName +" "+specificName);

        //TODO add list scroll animation

        return rowView;
    }
}
