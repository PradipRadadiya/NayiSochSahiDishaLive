package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.JainismListAdapter;
import com.vimalsagarji.vimalsagarjiapp.adpter.VisharMessageListAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.model.JainismItem;
import com.vimalsagarji.vimalsagarjiapp.model.VicharItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;


public class JainismActivity extends AppCompatActivity {
    private RecyclerView recycleview_notification;
    LinearLayoutManager linearLayoutManager;
    //    NotificationListAdapter notificationListAdapter;
//    private KProgressHUD loadingProgressDialog;
    private ImageView img_nodata;
    private ProgressBar progressbar;
    private int page_count = 1;
    private int psize = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    JainismListAdapter jainismListAdapter;
    ArrayList<JainismItem> vicharItems;
    private TextView txt_title;

    private ImageView imgarrorback, img_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jainism);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Jainism");

        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        vicharItems = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(JainismActivity.this);
        fndId();
        if (CommonMethod.isInternetConnected(JainismActivity.this)) {
            new GetJainismList().execute();
        } else {
            Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
        }

        jainismListAdapter = new JainismListAdapter(JainismActivity.this, vicharItems);
        recycleview_notification.setVisibility(View.VISIBLE);
        recycleview_notification.setAdapter(jainismListAdapter);


        recycleview_notification.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        if (CommonMethod.isInternetConnected(JainismActivity.this)) {

                            Log.e("total count", "--------------------" + page_count);

                            page_count++;
                            new GetJainismList().execute();
                        } else {
                            //internet not connected
                        }
                        loading = true;

                    }

                }
            }

        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void fndId() {

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        recycleview_notification = (RecyclerView) findViewById(R.id.recycleview_notification);
        recycleview_notification.setLayoutManager(linearLayoutManager);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
    }

    @SuppressLint("StaticFieldLeak")
    private class GetJainismList extends AsyncTask<String, Void, String> {

        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(NotificationActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait..")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("page", String.valueOf(page_count)));
            nameValuePairs.add(new BasicNameValuePair("psize", "20"));

            responseString = CommonMethod.postStringResponse(CommonUrl.Main_url + "jainaism/display",nameValuePairs,JainismActivity.this);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String date = jsonObject1.getString("date");
                        String Audio = jsonObject1.getString("Audio");
                        String total_likes = jsonObject1.getString("total_likes");
                        String total_comments = jsonObject1.getString("total_comments");
                        String total_views = jsonObject1.getString("total_views");

                        Date dt = CommonMethod.convert_date(date);
                        Log.e("Convert date is", "------------------" + dt);
                        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", dt);//Thursday
                        String stringMonth = (String) android.text.format.DateFormat.format("MMM", dt); //Jun
                        String intMonth = (String) android.text.format.DateFormat.format("MM", dt); //06
                        String year = (String) android.text.format.DateFormat.format("yyyy", dt); //2013
                        String day = (String) android.text.format.DateFormat.format("dd", dt); //20

                        Log.e("dayOfTheWeek", "-----------------" + dayOfTheWeek);
                        Log.e("stringMonth", "-----------------" + stringMonth);
                        Log.e("intMonth", "-----------------" + intMonth);
                        Log.e("year", "-----------------" + year);
                        Log.e("day", "-----------------" + day);

                        String fulldate = dayOfTheWeek+", "+day + "/" + intMonth + "/" + year;

                        String[] time = date.split("\\s+");

                        Log.e("time", "-----------------------" + time[1]);

                        vicharItems.add(new JainismItem(id,title, description, fulldate,Audio,total_likes,total_comments,total_views));
//                        vicharItems.add(new VicharItem(id,quote, fulldate + ", " + time[1]));
                    }
                    jainismListAdapter.notifyDataSetChanged();

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressbar.setVisibility(View.GONE);

        /*    if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
*/


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
