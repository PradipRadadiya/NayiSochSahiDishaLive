package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.annotation.SuppressLint;
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
import com.vimalsagarji.vimalsagarjiapp.adpter.NotificationListAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.NotificationItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class NotificationActivity extends AppCompatActivity {
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
    NotificationListAdapter notificationListAdapter;
    ArrayList<NotificationItem> notificationItems;
    private TextView txt_title;
    private ImageView imgarrorback, img_search;
    Sharedpreferance sharedpreferance;
    String mainurl;
    TextView txt_nodata;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        sharedpreferance = new Sharedpreferance(NotificationActivity.this);

        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            mainurl = CommonUrl.Main_url + "notificationcount/countnotification/?page=";
        } else {
            mainurl = CommonUrl.Main_url + "notificationcount/countnotification/?uid=" + sharedpreferance.getId() + "&page=";
        }
        txt_nodata = findViewById(R.id.txt_nodata);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Latest Posts");

        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        notificationItems = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        fndId();
        if (CommonMethod.isInternetConnected(NotificationActivity.this)) {
            new GetNotificationList().execute();
        } else {
            Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
        }


        notificationListAdapter = new NotificationListAdapter(NotificationActivity.this, notificationItems);
        recycleview_notification.setVisibility(View.VISIBLE);
        recycleview_notification.setAdapter(notificationListAdapter);

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

                        Log.e("flag-Loading_second_if", loading + "");
                        if (CommonMethod.isInternetConnected(NotificationActivity.this)) {

                            Log.e("total count", "--------------------" + page_count);

                            page_count++;
                            new GetNotificationList().execute();
                        } else {
                            //internet not connected
                        }
                        loading = true;

                    }

                }
            }

        });
    }


    private void fndId() {
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        recycleview_notification = (RecyclerView) findViewById(R.id.recycleview_notification);
        recycleview_notification.setLayoutManager(linearLayoutManager);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
    }


    @SuppressLint("StaticFieldLeak")
    private class GetNotificationList extends AsyncTask<String, Void, String> {

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

            responseString = CommonMethod.getStringResponse(mainurl + page_count + "&psize=20");
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
                        String table = jsonObject1.getString("Table");
                        Log.e("table", "--------------" + table);
                        String id = jsonObject1.getString("ID");
                        String title = jsonObject1.getString("Title");
                        String description = jsonObject1.getString("Description");
                        String date = jsonObject1.getString("Date");
                        String nid = jsonObject1.getString("NID");

                        if (description.equalsIgnoreCase("null")) {
                            description = "Description not available";
                        } else {

                        }

                        String is_viewed = "";

                       /* if (sharedpreferance.getId().equalsIgnoreCase("")) {
                            String flag = "true";
                            is_viewed = flag;
                        } else {
                            is_viewed = jsonObject1.getString("is_viewed");
                        }*/

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

                        String fulldate = day + "/" + intMonth + "/" + year;

                        String[] time = date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);

                        notificationItems.add(new NotificationItem(table, id, title, description, fulldate, nid, "true"));

//                        notificationItems.add(new NotificationItem(table, id, title, description, fulldate + ", " + time[1]));

                    }
                    notificationListAdapter.notifyDataSetChanged();

                } else {

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressbar.setVisibility(View.GONE);

            if (notificationItems.isEmpty()) {
                txt_nodata.setVisibility(View.VISIBLE);
            }

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
