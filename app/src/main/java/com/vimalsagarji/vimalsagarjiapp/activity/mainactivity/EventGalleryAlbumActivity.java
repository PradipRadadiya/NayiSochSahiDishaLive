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

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.EventGalleryAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.model.EventGalleryItem;
import com.vimalsagarji.vimalsagarjiapp.util.AllOurApiName;
import com.vimalsagarji.vimalsagarjiapp.util.AllOurCommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class EventGalleryAlbumActivity extends AppCompatActivity {
    private RecyclerView recycleviewVideo;
    private GridLayoutManager linearLayoutManager;
    EventGalleryAdapter eventGalleryAdapter;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_gallery_album);
        toolbarClick();

        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        linearLayoutManager = new GridLayoutManager(EventGalleryAlbumActivity.this, 2);
        recycleviewVideo = (RecyclerView) findViewById(R.id.recycleviewVideo);
        recycleviewVideo.setLayoutManager(linearLayoutManager);

        eventGalleryAdapter = new EventGalleryAdapter(EventGalleryAlbumActivity.this, eventGalleryItems);
        recycleviewVideo.setAdapter(eventGalleryAdapter);


        recycleviewVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                 @Override
                                                 public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                     super.onScrolled(recyclerView, dx, dy);

                                                     visibleItemCount = recyclerView.getChildCount();
                                                     totalItemCount = linearLayoutManager.getItemCount();
                                                     firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                                                     if (flag_scroll) {
//                                                                 Log.e("flag-Scroll", flag_scroll + "");
                                                     } else {
                                                         if (loading) {
                                                             Log.e("flag-Loading", loading + "");
                                                             if (totalItemCount > previousTotal) {
                                                                 loading = false;
                                                                 previousTotal = totalItemCount;
                                                                 //Log.e("flag-IF", (totalItemCount > previousTotal) + "");
                                                             }
                                                         }
                                                         if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                                                             // End has been reached
                                                             // Do something
                                                             Log.e("flag-Loading_second_if", loading + "");
                                                             if (CommonMethod.isInternetConnected(EventGalleryAlbumActivity.this)) {

                                                                 Log.e("total count", "--------------------" + page_count);

                                                                 page_count++;
                                                                 new GetAllEventAlbum().execute();
                                                             } else {
                                                                 //internet not connected
                                                             }
                                                             loading = true;


                                                         }

                                                     }
                                                 }

                                             }

        );


        if (CommonMethod.isInternetConnected(EventGalleryAlbumActivity.this)) {
            new GetAllEventAlbum().execute();
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

    private class GetAllEventAlbum extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("page",String.valueOf(page_count)));
            nameValuePairs.add(new BasicNameValuePair("psize","15"));

            responseJson = CommonMethod.postStringResponse(AllOurCommonUrl.commonUrl + AllOurApiName.eventgalleryalbums ,nameValuePairs,EventGalleryAlbumActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (object.getString("Photo").equalsIgnoreCase("null")) {

                    } else {
                        String eid = object.getString("ID");
                        String title = object.getString("Title");
                        String photo = object.getString("Photo");
                        String[] photoarray = photo.split(",");
                        eventGalleryItems.add(new EventGalleryItem(eid, title, CommonUrl.Main_url+"static/eventimage/" + photoarray[0]));

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
