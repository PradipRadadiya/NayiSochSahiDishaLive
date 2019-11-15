package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.SearchListAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.model.SearchItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class SearchActivity extends AppCompatActivity {

    private ImageView img_nosearch;
    private RecyclerView recycleview_search;
    private EditText edittext_search;
    private KProgressHUD loadingProgressDialog;
    LinearLayoutManager linearLayoutManager;
    private ProgressBar progressbar;
    SearchListAdapter searchListAdapter;
    ArrayList<SearchItem> searchItems;
    private ImageView img_datasearch, img_close;
    private TextView txt_title;
    private ImageView imgarrorback, img_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Search");

        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        fndId();
        idClick();
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext_search.setText("");
            }
        });
        img_datasearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethod.isInternetConnected(SearchActivity.this)) {
                    new Search().execute(edittext_search.getText().toString());
                } else {
                    Toast.makeText(SearchActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    new Search().execute(edittext_search.getText().toString());
//                    new Search().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void idClick() {
//        imgHome.setVisibility(View.GONE);
        img_close = (ImageView) findViewById(R.id.img_close);
    }

    private void fndId() {
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        img_datasearch = (ImageView) findViewById(R.id.img_datasearch);
        img_nosearch = (ImageView) findViewById(R.id.img_nosearch);
        recycleview_search = (RecyclerView) findViewById(R.id.recycleview_search);
        recycleview_search.setLayoutManager(linearLayoutManager);
        edittext_search = (EditText) findViewById(R.id.edittext_search);
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.home_menu, menu);


            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_home) {
                finish();
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_left);
                return true;
            }


            return super.onOptionsItemSelected(item);
        }
    */
    private class Search extends AsyncTask<String, Void, String> {
        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
            recycleview_search.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("SearchTitle", params[0]));
            responseString = CommonMethod.postStringResponse(CommonUrl.Main_url + "search/searchdata/", nameValuePairs, SearchActivity.this);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------" + s);
            progressbar.setVisibility(View.GONE);
            recycleview_search.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    searchItems = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String table = object.getString("Table");
                        String title = object.getString("Title");
                        String id = object.getString("ID");
                        String date = object.getString("Date");
                        String description = object.getString("Description");
                        if (description.equalsIgnoreCase("null")) {
                            description = "Description not available";
                        } else {

                        }

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

                        searchItems.add(new SearchItem(table, title, id, fulldate + ", " + time[1], description));
                    }
                }
                if (jsonObject.getString("status").equalsIgnoreCase("error")) {
                    searchItems = new ArrayList<>();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (recycleview_search != null) {
                searchListAdapter = new SearchListAdapter(SearchActivity.this, searchItems);
                if (searchListAdapter.getItemCount() != 0) {
                    recycleview_search.setVisibility(View.VISIBLE);
                    img_nosearch.setVisibility(View.GONE);
                    recycleview_search.setAdapter(searchListAdapter);
                } else {
                    img_nosearch.setVisibility(View.VISIBLE);
                    recycleview_search.setVisibility(View.GONE);
                }
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
