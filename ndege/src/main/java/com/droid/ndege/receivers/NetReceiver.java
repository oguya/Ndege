package com.droid.ndege.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.ndege.R;
import com.droid.ndege.constants.Constants;
import com.droid.ndege.model.MatchResult;
import com.droid.ndege.ui.TagDetailsActivity;

/**
 * Created by james on 06/03/14.
 */
//process results from backend svr & service
public class NetReceiver extends BroadcastReceiver {

    private TextView tag_results;
    private TextView tag_status;
    private Activity activity;

    public NetReceiver(Activity activity, TextView tag_results, TextView tag_status){
        this.activity = activity;
        this.tag_results = tag_results;
        this.tag_status = tag_status;
    }

    public NetReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        MatchResult matchResult = (MatchResult)bundle.getParcelable(Constants.KEY_MATCH_RESULT);

        int tagResults = matchResult.getTagResults();
        int tagID = matchResult.getTagID();

        Log.e("NetReceiver", "TagID: "+tagID+" results: "+tagResults);

        switch (tagResults){
            case Constants.TAG_RESULT_OK: //open tagDetails
                if(tagID == -1){ //db error
                    showError(Constants.TAG_DB_ERROR);
                }else{
                    Bundle data = new Bundle();
                    data.putInt(Constants.KEY_TAG_ID, tagID);

                    Intent tagDetails = new Intent(context, TagDetailsActivity.class);
                    tagDetails.putExtras(data);
//                    context.startActivity(intent);
                }
                break;

            case Constants.TAG_RESULT_FAILED: //show error
                showError(tagResults);
                break;

            case Constants.TAG_NET_ERROR: //show error
                showError(tagResults);
                break;

            default: break;
        }
    }

    private void showError(int errorCode){
        switch (errorCode){
            case Constants.TAG_RESULT_FAILED: //unknown tag
                tag_results.setVisibility(View.VISIBLE);
                tag_results.setText(activity.getString(R.string.tag_status_failed));
                tag_status.setText(activity.getString(R.string.tag_status_default));
                break;

            case Constants.TAG_NET_ERROR: //network error
                tag_results.setVisibility(View.VISIBLE);
                tag_results.setText(activity.getString(R.string.tag_status_net_err));
                tag_status.setText(activity.getString(R.string.tag_status_default));
                break;

            default:
                tag_results.setVisibility(View.VISIBLE);
                tag_status.setText(activity.getString(R.string.tag_status_activated));
                break;
        }
    }

}
