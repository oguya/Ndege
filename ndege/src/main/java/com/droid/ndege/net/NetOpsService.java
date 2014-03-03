package com.droid.ndege.net;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import com.droid.ndege.constants.Constants;
import com.droid.ndege.model.Tag;
import com.droid.ndege.utils.FirstRunInit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NetOpsService extends IntentService {

    private final static String LOG_TAG = "NetOpsService";

    public NetOpsService() {
        super("NetOpsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();

        String filePath = bundle.getString(Constants.KEY_FILEPATH);

        Tag tag = null;

        try{
            tag = uploadTagFile(filePath, Constants.BG_SVR_URL_TAG_FILE);

        }catch (Exception ex){
            Log.e(LOG_TAG, "Unable to upload file: "+ex);
        }

        if(tag != null){
            //publish results
            publishResults(tag, Constants.RESULT_OK);
        }else{
            publishResults(tag, Constants.RESULT_FAILED);
        }

    }

    //upload audio file to backend return tag
    public Tag uploadTagFile(String filePath, String svrURL) throws IOException{
        Tag tag = null;

        MultiValueMap<String, Object> args = new LinkedMultiValueMap<String, Object>();
        args.add(Constants.BG_TAG_AUDIO_FILE, new FileSystemResource(filePath));
        args.add(Constants.BG_DEVICE_ID, new FirstRunInit(this).getDeviceID());
        // add addition args => args(argName, object val)

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        tag = restTemplate.getForObject(svrURL, Tag.class, args);

        return tag;
    }

    //publish results using broadcast receivers
    public void publishResults(Tag results, int resultCode){
        Intent intent = new Intent(Constants.RECEIVER_FILTER);
        Bundle bundle = new Bundle();
//        bundle.putParcelable(results);

        intent.putExtras(bundle);

        sendBroadcast(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    public String connect2Net() {
        String jsonURL = "";
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(jsonURL);

        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine= response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while( (line = reader.readLine())!= null ){
                    builder.append(line);
                }
            }else{
                Log.i(LOG_TAG,"Failed to dl json file! Server Response: "+statusCode);
            }

        }catch(ClientProtocolException cpe){
            cpe.printStackTrace();
            Log.i(LOG_TAG,"Failed to dl json file!"+cpe.getMessage());
        }catch(IOException e){
            e.printStackTrace();
            Log.i(LOG_TAG,"Failed to dl json file!"+e.getMessage());
        }

        return builder.toString();
    }
}
