package com.chaudhary.flickrapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrjsonData.OnDataAvailable,
                                             RecyclerItemClickListerner.OnRecyclerClickListener{
    private static final String TAG = "MainActivity";
    private FlickerRecyclerViewAdapter mFlickerRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListerner(this,recyclerView,this));

        mFlickerRecyclerViewAdapter = new FlickerRecyclerViewAdapter(new ArrayList<Photos>(),this);
        recyclerView.setAdapter(mFlickerRecyclerViewAdapter);

//        GetRawData rawData = new GetRawData(this);
//        rawData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android&format=json&nojsoncallback=1");

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: started");
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = sharedPreferences.getString(FLICKR_QUERY,"");
        if (queryResult.length() > 0){
            GetFlickrjsonData getFlickrjsonData = new GetFlickrjsonData("https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true,this);
//        getFlickrjsonData.executeOnSameThread("android, nougat");
            getFlickrjsonData.execute(queryResult);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDataAvailable(List<Photos> data, DownloadStatus status){
        if (status == DownloadStatus.OK){
//            Log.d(TAG, "onDownloadComplete: download complete :" + data);
             mFlickerRecyclerViewAdapter.loadNewData(data);
        }
        else{
            Log.d(TAG, "onDownloadComplete: download failed with status :"+status);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(MainActivity.this,"Normal tap at position " + position,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
//        Toast.makeText(MainActivity.this,"Long tap at position "+ position,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER,mFlickerRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
    }
}
