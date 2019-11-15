package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerTopListAdapter;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.EventCategory;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.model.TopTenItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class TopTenCompetitionList extends AppCompatActivity {

    private RecyclerView recycleview_top_list;
    private ProgressBar progressbar;
    LinearLayoutManager linearLayoutManager;
    ArrayList<TopTenItem> topTenItems = new ArrayList<>();
    RecyclerTopListAdapter recyclerTopListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topten_comp_list);
        String cid = getIntent().getStringExtra("cid");
        bindID();
        toolbarClick();
        new GetTopList().execute(cid);
    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        txt_title.setText("Winner List");
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
                Intent intent = new Intent(TopTenCompetitionList.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void bindID() {
        recycleview_top_list = (RecyclerView) findViewById(R.id.recycleview_top_list);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(TopTenCompetitionList.this);
        recycleview_top_list.setLayoutManager(linearLayoutManager);
    }

    private class GetTopList extends AsyncTask<String, Void, String> {

        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", strings[0]));
            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "competition/getrankwiselist", nameValuePairs, TopTenCompetitionList.this);

            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String user_marks = object.getString("user_marks");
                        String total_mark = object.getString("total_mark");
                        String user_id = object.getString("user_id");
                        String Name = object.getString("Name");
                        Log.e("name", "---------------" + Name);

//                        topTenItems.add(new TopTenItem(user_marks, total_mark, user_id, Name));

                        if (Integer.parseInt(user_marks)>0){
                            topTenItems.add(new TopTenItem(user_marks, total_mark, user_id, Name));
                        }
//
//                        if (user_marks.equalsIgnoreCase("1")) {
//                            topTenItems.add(new TopTenItem(user_marks, total_mark, user_id, Name));
//                        }

                    }

                    /*

                    if (jsonArray.length() > 10) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String user_marks = object.getString("user_marks");
                            String total_mark = object.getString("total_mark");
                            String user_id = object.getString("user_id");
                            String Name = object.getString("Name");
                            Log.e("name", "---------------" + Name);

                            if (user_marks.equalsIgnoreCase("1")){
                                topTenItems.add(new TopTenItem(user_marks, total_mark, user_id, Name));
                            }

                        }
                    }else{
                        for (int i = 0; i < 9; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String user_marks = object.getString("user_marks");
                            String total_mark = object.getString("total_mark");
                            String user_id = object.getString("user_id");
                            String Name = object.getString("Name");
                            Log.e("name", "---------------" + Name);
                            topTenItems.add(new TopTenItem(user_marks, total_mark, user_id, Name));

                        }
                    }*/
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressbar.setVisibility(View.GONE);
//            recycleview_top_list.setVisibility(View.VISIBLE);
            if (recycleview_top_list != null) {
                recyclerTopListAdapter = new RecyclerTopListAdapter(TopTenCompetitionList.this, topTenItems);
                recycleview_top_list.setAdapter(recyclerTopListAdapter);
                if (recyclerTopListAdapter.getItemCount() != 0) {
                    recycleview_top_list.setVisibility(View.VISIBLE);
                    recycleview_top_list.setAdapter(recyclerTopListAdapter);
                } else {
                    recycleview_top_list.setVisibility(View.GONE);
                    Toast.makeText(TopTenCompetitionList.this, "List not found", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}
