package com.droid.ndege.net;

import android.util.Log;

import com.droid.ndege.constants.Constants;

import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

/**
 * Created by james on 1/04/14.
 */
public class UploadFile {
    public static final String LOG_TAG = "UploadFile";
    private int serverResponseCode = 0;

    public UploadFile(){}

    public int uploadWavFile(String fileName, String deviceID){
        String sourceFileUri = fileName;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e(LOG_TAG, "Source File not exist :" +fileName);
            return 0;
        }else{
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(Constants.BG_SVR_URL_TAG_FILE);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty(Constants.BG_TAG_AUDIO_FILE, fileName);
                conn.setRequestProperty(Constants.BG_DEVICE_ID, deviceID);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name="+Constants.BG_TAG_AUDIO_FILE+";filename="
                        + fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i(LOG_TAG, "HTTP Response is : "+serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    Log.e(LOG_TAG, "file upload successful");
                }else{
                    Log.e(LOG_TAG, "file NOT upload successful Code: "+serverResponseMessage);
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e(LOG_TAG, "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;

        }
    }

    public int uploadFile(String filePath, String deviceID, String serverURL) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        int request_id = -1;

        //post request to send file
        HttpPost httpPost = new HttpPost(serverURL);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        FileBody uploadFile = new FileBody(new File(filePath));
        MultipartEntity reqEntity = new MultipartEntity();
//        reqEntity.addPart("the args. the server takes", uploadFile);
        reqEntity.addPart(Constants.BG_TAG_AUDIO_FILE, uploadFile);
        reqEntity.addPart(Constants.BG_DEVICE_ID, new StringBody(deviceID));
        httpPost.setEntity(reqEntity);

        //debugging
        Log.e(LOG_TAG, "request: " + httpPost.getRequestLine());

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity httpEntity = response.getEntity();

        //debugging
        Log.e(LOG_TAG, "status line: " + response.getStatusLine());
        if (httpEntity != null) {
            String responseStr = EntityUtils.toString(httpEntity);
            Log.e(LOG_TAG, "Server response: "+responseStr);
            try{
                request_id = Integer.parseInt(responseStr);
            }catch (NumberFormatException ex){
                ex.printStackTrace();
                Log.e(LOG_TAG, "Unable to format response: "+responseStr+"\n"+ex);
            }
            Log.e(LOG_TAG, "Request id: " + request_id);
            httpEntity.consumeContent();
        }else{
            Log.e(LOG_TAG, "unable to upload wavfile!");
        }
        httpClient.getConnectionManager().shutdown();
        return request_id;
    }


}
