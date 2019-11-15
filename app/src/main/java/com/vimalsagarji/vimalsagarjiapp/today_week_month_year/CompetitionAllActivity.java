package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.competition.CompetitionFinalResultNote;
import com.vimalsagarji.vimalsagarjiapp.activity.competition.CompetitionWinner;
import com.vimalsagarji.vimalsagarjiapp.activity.competition.RecyclerWinnerListAdapter;
import com.vimalsagarji.vimalsagarjiapp.activity.competition.WinnerListActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerCompetitionCategoryAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionItem;
import com.vimalsagarji.vimalsagarjiapp.retrofit.APIClient;
import com.vimalsagarji.vimalsagarjiapp.retrofit.ApiInterface;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionAllActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerCompetitionCategoryAdapter recyclerCompetitionCategoryAdapter;
    private ProgressBar progress_load;
    private ArrayList<CompetitionItem> competitionItemArrayList = new ArrayList<>();
    private TextView txt_nodata;
    TextView txt_final_comp;
    private String title;
    private String description;
    private String start_date;
    private String end_date;
    private String attend_comp;
    private String is_visible;

//    FirebaseRemoteConfig mFirebaseRemoteConfig;

    // cache expiration in seconds
//    long cacheExpiration = 3500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competionall);

//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();


        //expire the cache immediately for development mode.
        /*if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }*/

        /*mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CompetitionAllActivity.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                            String comp_title = mFirebaseRemoteConfig.getString("comp_title");
//



                        } else {
                            Toast.makeText(CompetitionAllActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
//                        displayWelcomeMessage();


                    }
                });*/


        bindID();
        toolbarClick();
        if (CommonMethod.isInternetConnected(CompetitionAllActivity.this)) {
            getCompAlert();
        }

        new GetAllCompetitionCategory().execute();

    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        txt_title.setText("Competition");
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
                Intent intent = new Intent(CompetitionAllActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void bindID() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CompetitionAllActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_competition);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
        txt_nodata = (TextView) findViewById(R.id.txt_nodata);
        TextView txt_old_comp = (TextView) findViewById(R.id.txt_old_comp);
        txt_old_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompetitionAllActivity.this, OldCompetitionAllActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        txt_final_comp = (TextView) findViewById(R.id.txt_final_comp);
        txt_final_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompetitionAllActivity.this, CompetitionFinalResultNote.class);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("start_date", start_date);
                intent.putExtra("end_date", end_date);
                intent.putExtra("percentage", attend_comp);
                intent.putExtra("is_visible", is_visible);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);


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

            responseJSON = CommonMethod.postStringResponse(CommonURL.Main_url + "competition/gettodaycompetition", nameValuePairs, CompetitionAllActivity.this);
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
                recyclerCompetitionCategoryAdapter = new RecyclerCompetitionCategoryAdapter(CompetitionAllActivity.this, competitionItemArrayList);
                if (recyclerCompetitionCategoryAdapter.getItemCount() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    txt_nodata.setVisibility(View.GONE);
                    recyclerView.setAdapter(recyclerCompetitionCategoryAdapter);
                } else {
                    txt_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

        }

    }

    private void getCompAlert() {
        final ProgressDialog progressDialog = new ProgressDialog(CompetitionAllActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.getCompetitionAlert();
        callApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                assert response.body() != null;
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject object = jsonArray.getJSONObject(0);

                            title = object.getString("title");
                            description = object.getString("description");
                            start_date = object.getString("start_date");
                            end_date = object.getString("end_date");
                            attend_comp = object.getString("attend_comp");
                            is_visible = object.getString("is_visible");

                            if (is_visible.equalsIgnoreCase("1")) {
                                txt_final_comp.setVisibility(View.VISIBLE);
                            } else {
                                txt_final_comp.setVisibility(View.GONE);
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }

        });

    }

}
