package com.vimalsagarji.vimalsagarjiapp.categoryactivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerEventCategoryAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.EventCategoryItem;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class EventCategory extends AppCompatActivity implements View.OnClickListener {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh_information;
    private RecyclerView recyclerView_audio_category;
    private GridLayoutManager linearLayoutManager;
    private ArrayList<EventCategoryItem> allAudioCategoryItems = new ArrayList<>();
    private RecyclerEventCategoryAdapter recyclerAudioCategoryAdapter;
    private ImageView img_nodata;
    ProgressBar progress_load;
    private Sharedpreferance sharedpreferance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_category);
        sharedpreferance=new Sharedpreferance(EventCategory.this);

        linearLayoutManager = new GridLayoutManager(EventCategory.this,2);
        findID();
        idClick();
        toolbarClick();

        if (CommonMethod.isInternetConnected(EventCategory.this)) {

            new GetAllEventCategory().execute();
        }
        swipe_refresh_information.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonMethod.isInternetConnected(EventCategory.this)) {
                    refreshContent();
                } else {
                    swipe_refresh_information.setRefreshing(false);
                }
            }
        });

    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        txt_title.setText("Event");
        img_search.setVisibility(View.VISIBLE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCategory.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void refreshContent() {
        swipe_refresh_information.setRefreshing(false);
        new GetAllEventCategory().execute();
    }

    private void findID() {
        TextView txtCategory=findViewById(R.id.txtCategory);
        txtCategory.setText("Event Category");

        swipe_refresh_information = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_information);
        recyclerView_audio_category = (RecyclerView) findViewById(R.id.recyclerView_audio_category);
        recyclerView_audio_category.setLayoutManager(linearLayoutManager);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
    }

    private void idClick() {

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    private class GetAllEventCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_audio_category.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + "event/getallcategory");
            }else{
                responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + "event/getallcategory/"+"?uid="+sharedpreferance.getId());
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                allAudioCategoryItems = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e("id", "---------------" + id);
                        String name = jsonObject1.getString("Name");
                        String categoryIcon =  CommonURL.ImagePath + "eventcategory/" + jsonObject1.getString("CategoryIcon").replaceAll(" ","%20");

                        Log.e("imagepath","---------------"+categoryIcon);

                        String newdata="";

                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            newdata="0";
                        }else{
                            newdata = jsonObject1.getString("new_event");
                        }
                        allAudioCategoryItems.add(new EventCategoryItem(id, name, categoryIcon,newdata,"event"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }

            progress_load.setVisibility(View.GONE);
            recyclerView_audio_category.setVisibility(View.VISIBLE);

            if (recyclerView_audio_category != null) {
                recyclerAudioCategoryAdapter = new RecyclerEventCategoryAdapter(EventCategory.this, allAudioCategoryItems);
                if (recyclerAudioCategoryAdapter.getItemCount() != 0) {
                    recyclerView_audio_category.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_audio_category.setAdapter(recyclerAudioCategoryAdapter);

                } else {
                    recyclerView_audio_category.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }

            }


        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

    }


}
