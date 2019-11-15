package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.annotation.SuppressLint;
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

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerCompetitionCategoryAdapter;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerOldCompetitionCategoryAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionItem;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class OldCompetitionAllActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerOldCompetitionCategoryAdapter recyclerCompetitionCategoryAdapter;
    private ProgressBar progress_load;
    private ArrayList<CompetitionItem> competitionItemArrayList = new ArrayList<>();
    private TextView txt_nodata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competionall);
        bindID();
        toolbarClick();
        new GetAllCompetitionCategory().execute();
    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        txt_title.setText("Last Competition");
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
                Intent intent = new Intent(OldCompetitionAllActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void bindID() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OldCompetitionAllActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_competition);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
        txt_nodata = (TextView) findViewById(R.id.txt_nodata);
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayout lin_comp = (LinearLayout) findViewById(R.id.lin_comp);
        lin_comp.setVisibility(View.GONE);
        TextView txt_old_comp = (TextView) findViewById(R.id.txt_old_comp);
        txt_old_comp.setVisibility(View.GONE);

    }

    @SuppressLint("StaticFieldLeak")
    private class GetAllCompetitionCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("page", "1"));
            nameValuePairs.add(new BasicNameValuePair("psize", "1000"));

            responseJSON = CommonMethod.postStringResponse(CommonURL.Main_url + "competition/getcompletedcompetition", nameValuePairs, OldCompetitionAllActivity.this);
            return responseJSON;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response competition", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                competitionItemArrayList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                   /* if (jsonArray.length() < 30 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }*/
                    Log.e("json array", "-------------------" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e("id", "---------------" + id);
                        String title = jsonObject1.getString("title");
                        String rules = jsonObject1.getString("rules");
                        String date = jsonObject1.getString("start_time");
                        String time = jsonObject1.getString("end_time");
                        String total_question = jsonObject1.getString("total_question");
                        String total_minute = jsonObject1.getString("total_minute");
                        String is_open = jsonObject1.getString("status");
                        String participated_users = jsonObject1.getString("participated_users");
                        String status = jsonObject1.getString("status");
                        String description = jsonObject1.getString("description");
                        competitionItemArrayList.add(new CompetitionItem(id, title, rules, date, time, total_question, total_minute, is_open, participated_users, false, status, description));

                    }

                    Log.e("size", "--------------------" + competitionItemArrayList.size());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }

            progress_load.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (recyclerView != null) {
                recyclerCompetitionCategoryAdapter = new RecyclerOldCompetitionCategoryAdapter(OldCompetitionAllActivity.this, competitionItemArrayList);
                if (recyclerCompetitionCategoryAdapter.getItemCount() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    txt_nodata.setVisibility(View.GONE);
                    recyclerView.setAdapter(recyclerCompetitionCategoryAdapter);
                } else {
                    txt_nodata.setVisibility(View.VISIBLE);
                    txt_nodata.setText("Competition Result \n Not Found");
                    recyclerView.setVisibility(View.GONE);
                }

            }

        }
    }
}
