package com.droid.ndege.net;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import com.droid.ndege.constants.Constants;
import com.droid.ndege.db.DBAdapter;
import com.droid.ndege.model.BirdImage;
import com.droid.ndege.model.MatchResult;
import com.droid.ndege.model.Tag;
import com.droid.ndege.utils.FirstRunInit;
import com.droid.ndege.utils.LocationUtils;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class NetOpsService extends IntentService {

    private final static String LOG_TAG = "NetOpsService";
    private DBAdapter dbAdapter;
    private MatchResult matchResult;

    public NetOpsService() {
        super("NetOpsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        Bundle bundle = intent.getExtras();
        String filePath = bundle.getString(Constants.KEY_FILEPATH);

        matchResult = new MatchResult();
        JSONObject jsonObject;

        try{
//            jsonObject = uploadTagFile(filePath, Constants.BG_SVR_URL_TAG_FILE);
//            new UploadFile().uploadWavFile(filePath, new FirstRunInit(this).getDeviceID());
            UploadFile uploader = new UploadFile();
            int request_id = uploader.uploadFile(filePath, new FirstRunInit(this).getDeviceID(),
                    Constants.BG_SVR_URL_TAG_FILE);

            if (request_id != -1){
                Log.e(LOG_TAG, "Good data");
//                String jsonStr = getMatchResults(Constants.BG_SVR_URL_GET_RESULT, request_id);
                String jsonStr = getMatchResults(request_id);

                //parse json
            parseJSONResponse(new JSONObject(jsonStr));

            }else{
                Log.e(LOG_TAG, "NO Good data");
                //net error
                matchResult.setTagID(-1);
                matchResult.setTagResults(Constants.TAG_NET_ERROR);
            }
        }catch (IOException ex){
            Log.e(LOG_TAG, "Unable to upload file: "+ex);

            //net error
            matchResult.setTagID(-1);
            matchResult.setTagResults(Constants.TAG_NET_ERROR);
        }catch (HttpServerErrorException ex){
            Log.e(LOG_TAG, "Unable to get json from flask: "+ex);
            //net error
            matchResult.setTagID(-1);
            matchResult.setTagResults(Constants.TAG_NET_ERROR);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Unable to parse json response: "+e);
            matchResult.setTagID(-1);
            matchResult.setTagResults(Constants.TAG_NET_ERROR);
        }

        //publish results
        publishResults();
    }

    //upload audio file to backend return tag
    public JSONObject uploadTagFile(String filePath, String svrURL) throws IOException{
        JSONObject jsonObject;

        MultiValueMap<String, Object> args = new LinkedMultiValueMap<String, Object>();
        args.add(Constants.BG_TAG_AUDIO_FILE, new FileSystemResource(filePath));
        args.add(Constants.BG_DEVICE_ID, new FirstRunInit(this).getDeviceID());
        // add addition args => args(argName, object val)

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//        jsonObject = restTemplate.getForObject(svrURL, JSONObject.class, args);
//        jsonObject = restTemplate.postForEntity(svrURL, args, String.class, args);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAcceptEncoding(ContentCodingType.IDENTITY);
        requestHeaders.setAcceptEncoding(ContentCodingType.GZIP); // Add the gzip Accept-Encoding header
        requestHeaders.setAcceptEncoding(ContentCodingType.ALL);
        requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
//        requestHeaders.setContentType(new MediaType("multipart", "form-data"));
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(svrURL, requestEntity, String.class, args);
//        ResponseEntity<String> response =restTemplate.exchange(svrURL, HttpMethod.POST, requestEntity, String.class, args);
        Log.e(LOG_TAG,"response:: "+response.getBody());
        return null;
    }

    //get match results
    public String getMatchResults(String svrURL, int request_id){

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String jsonStr = restTemplate.getForObject(svrURL, String.class, request_id);
        Log.e(LOG_TAG, "Got response: "+jsonStr);
        return jsonStr;
    }

    //publish results using broadcast receivers
    public void publishResults(){
        Intent intent = new Intent(Constants.RECEIVER_FILTER);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_MATCH_RESULT, matchResult);

        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        dbAdapter.close();
    }

    //parse json & add tag
    public void parseJSONResponse(JSONObject jsonObject) throws JSONException{
        Tag tag = new Tag();

        int tagResults = jsonObject.getInt(MatchResult.TAG_RESULTS);
        int tagID = -1;
        switch (tagResults){

            case Constants.TAG_RESULT_OK: //matched! parse & add to Tag model
                tag.setBirdId(jsonObject.getInt(Tag.BIRD_ID));
                tag.setEnglishName(jsonObject.getString(Tag.ENGLISH_NAME));
                tag.setGenericName(jsonObject.getString(Tag.GENERIC_NAME));
                tag.setSpecificName(jsonObject.getString(Tag.SPECIFIC_NAME));
                tag.setRecorder(jsonObject.getString(Tag.RECORDER));
                tag.setLocation(jsonObject.getString(Tag.LOCATION));
                tag.setCountry(jsonObject.getString(Tag.COUNTRY));
                tag.setLat(jsonObject.getString(Tag.LAT));
                tag.setLng(jsonObject.getString(Tag.LNG));
                tag.setXenoCantoURL(jsonObject.getString(Tag.XENOCANTOURL));
                tag.setSoundType(jsonObject.getString(Tag.SOUND_TYPE));
                tag.setSoundURL(jsonObject.getString(Tag.SOUND_URL));
                tag.setThumbnailURL(jsonObject.getString(Tag.THUMBNAIL_URL));

                //TODO add location stuff from geoCoder
                tag.setTagLocation("Nairobi, Kenya");
                tag.setTagDate(LocationUtils.currentDate());

                tagID = (int) dbAdapter.addTag(tag);

                JSONArray images = jsonObject.getJSONArray(BirdImage.IMAGES_LBL);
                addImages(images, tagID);
                break;

            case Constants.TAG_RESULT_FAILED: //matching failed
                break;

            default: break;
        }

        matchResult.setTagID(tagID);
        matchResult.setTagResults(tagResults);
    }

    public void addImages(JSONArray images, int tagID) throws JSONException{
        if(tagID == -1){
            Log.e(LOG_TAG, "wrong tagID: "+tagID);
            return;
        }

        ArrayList<BirdImage> birdImages = new ArrayList<BirdImage>();

        for(int i=0; i< images.length(); i++){
            BirdImage image = new BirdImage();

            image.setTagID(tagID);
            image.setImageURL(images.getJSONObject(i).getString(BirdImage.IMAGE_URL));
            image.setSiteURL(images.getJSONObject(i).getString(BirdImage.SITE_URL));

            birdImages.add(image);
        }

        dbAdapter.addImages(birdImages);
    }

    public String getMatchResults(int requestID) {
        String jsonURL = Constants.BG_SVR_URL_GET_RESULT.replace("{query}", String.valueOf(requestID));
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(jsonURL);

        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine= response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if(statusCode == 200){
                org.apache.http.HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while( (line = reader.readLine())!= null ){
                    builder.append(line);
                }
                Log.e(LOG_TAG, "Got response: "+builder.toString());
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
