package com.chaudhary.flickrapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrjsonData extends AsyncTask<String, Void,List<Photos>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrjsonData";

    private List<Photos> mPhotosList = null;
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvailable mCallBack;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable{
        void onDataAvailable(List<Photos> data,DownloadStatus status);
    }

    public GetFlickrjsonData(String baseURL, String language, boolean matchAll, OnDataAvailable callBack) {
        mBaseURL = baseURL;
        mCallBack = callBack;
        mLanguage = language;
        mMatchAll = matchAll;
        }

    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        String destinationUri = createUri(searchCriteria,mLanguage,mMatchAll);
        runningOnSameThread = true;
        GetRawData getData = new GetRawData(this);
        getData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(List<Photos> photos) {
        Log.d(TAG, "onPostExecute: starts");
        if (mCallBack!= null){
            mCallBack.onDataAvailable(mPhotosList,DownloadStatus.OK);
        }
//        super.onPostExecute(photos);
    }

    @Override
    protected List<Photos> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: start");
        String destinationUri = createUri(strings[0],mLanguage,mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mPhotosList;
    }

    private String createUri(String searchCriteria , String lang, boolean matchAll){
        Log.d(TAG, "createUri: starts");

        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode",matchAll ? "All": "ANY")
                .appendQueryParameter("lang",lang)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }


    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts with status"+status);

        if (status == DownloadStatus.OK){
            mPhotosList = new ArrayList<>();

            try{
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i< itemsArray.length();i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.","_b.");

                    Photos photoObject = new Photos(title,author,authorId,link,tags,photoUrl);
                    mPhotosList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete" + photoObject.toString());
                 }
            }catch (JSONException jsone){
                jsone.printStackTrace();
                Log.d(TAG, "onDownloadComplete: error processing json data"+jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if (runningOnSameThread && mCallBack!= null){
            mCallBack.onDataAvailable(mPhotosList,status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}


