package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.ImageItemSplash;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.EventAllImageGalleryAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.EventGalleryItem;
import com.vimalsagarji.vimalsagarjiapp.util.CommonAPI_Name;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vimalsagarji.vimalsagarjiapp.today_week_month_year.GalleryCategory.itemSplashArrayList;

public class EventGalleryAllImagesActivity extends AppCompatActivity {
    private RecyclerView recycleviewVideo;
    private GridLayoutManager linearLayoutManager;
    EventAllImageGalleryAdapter eventGalleryAdapter;
    private ArrayList<EventGalleryItem> eventGalleryItems = new ArrayList<>();
    //page count
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private ProgressBar progressbar;
    //    private ImageView imgarrorback;
//    private TextView txt_title;
//    private ImageView img_search;
    private TextView txt_nodata_today;
    String eid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_gallery_album);
        toolbarClick();

        eid = getIntent().getStringExtra("eid");

        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        linearLayoutManager = new GridLayoutManager(EventGalleryAllImagesActivity.this, 2);
        recycleviewVideo = (RecyclerView) findViewById(R.id.recycleviewVideo);
        recycleviewVideo.setLayoutManager(linearLayoutManager);

        eventGalleryAdapter = new EventAllImageGalleryAdapter(EventGalleryAllImagesActivity.this, eventGalleryItems);
        recycleviewVideo.setAdapter(eventGalleryAdapter);


        if (CommonMethod.isInternetConnected(EventGalleryAllImagesActivity.this)) {
            new GetAllEventImage().execute();
        } else {
            //internet not connected
            Toast.makeText(this, R.string.internet, Toast.LENGTH_LONG).show();
        }


    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        txt_title.setText("Event Images");
        img_search.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class GetAllEventImage extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            responseJson = CommonMethod.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getEventDetail + "?eid=" + eid);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray1 = jsonObject.getJSONArray("Photo");
                itemSplashArrayList = new ArrayList<>();

                if (jsonArray1 != null) {
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject object = jsonArray1.getJSONObject(i);
                        String photo = object.getString("Photo");

                        itemSplashArrayList.add(new ImageItemSplash(CommonURL.ImagePath + CommonAPI_Name.eventimage + photo, CommonURL.ImagePath + CommonAPI_Name.eventimage + photo));
                        eventGalleryItems.add(new EventGalleryItem("1","test",CommonURL.ImagePath + CommonAPI_Name.eventimage + photo));

                    }
                }
                eventGalleryAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            eventGalleryAdapter.notifyDataSetChanged();
            progressbar.setVisibility(View.GONE);

            if (eventGalleryItems.isEmpty()) {

                txt_nodata_today.setVisibility(View.VISIBLE);
            }

        }


    }
}
