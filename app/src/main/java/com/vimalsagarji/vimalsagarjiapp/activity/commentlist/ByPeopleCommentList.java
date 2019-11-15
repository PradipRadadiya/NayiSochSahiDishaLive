package com.vimalsagarji.vimalsagarjiapp.activity.commentlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.CommentAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommentsList;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class ByPeopleCommentList extends AppCompatActivity {
    private String id, click_action;
    private KProgressHUD loadingProgressDialog;
    RecyclerView recyclerView_comments;
    EditText edit_comment;
    ImageView img_send;
    ImageView img_comment_back;
    Sharedpreferance sharedpreferance;
    //page count
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    LinearLayoutManager linearLayoutManager;
    ArrayList<CommentsList> commentLists = new ArrayList<>();
    CommentAdapter commentAdapter;
    ImageView img_nodata;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment_list);
        Intent intent = getIntent();
        id = intent.getExtras().getString("postId");
        click_action = intent.getExtras().getString("click_action");
        sharedpreferance = new Sharedpreferance(ByPeopleCommentList.this);
        findId();
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_comment.getText().toString())) {
                    edit_comment.setError("Please enter comment.");
                    edit_comment.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(ByPeopleCommentList.this)) {
                        new CommentPost().execute(edit_comment.getText().toString());
                    } else {
                        Toast.makeText(ByPeopleCommentList.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        recyclerView_comments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (flag_scroll) {
                    Log.e("flag-Scroll", flag_scroll + "");
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
                        if (CommonMethod.isInternetConnected(ByPeopleCommentList.this)) {
                            Log.e("total count", "--------------------" + page_count);
                            page_count++;
                            new CommentList().execute(id);
                        } else {
                            Toast.makeText(ByPeopleCommentList.this, R.string.internet, Toast.LENGTH_SHORT).show();
                        }
                        loading = true;
                    }
                }
            }
        });
    }

    private void findId() {
        linearLayoutManager = new LinearLayoutManager(ByPeopleCommentList.this);
        img_comment_back = (ImageView) findViewById(R.id.img_back);
        recyclerView_comments = (RecyclerView) findViewById(R.id.recyclerView_comments);
        recyclerView_comments.setLayoutManager(linearLayoutManager);
        edit_comment = (EditText) findViewById(R.id.edit_comment);
        img_send = (ImageView) findViewById(R.id.img_send);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
        img_comment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Comment Post
    private class CommentPost extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ByPeopleCommentList.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("pid", id));
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"bypeople/comment/", nameValuePairs, ByPeopleCommentList.this);

            return responseJSON;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    edit_comment.setText("");

                    loadingProgressDialog.dismiss();

                    page_count=1;
                    commentLists=new ArrayList<>();
                    new CommentList().execute(id);
                } else {
                }
            } catch (JSONException e) {
                loadingProgressDialog.dismiss();

                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }

        }


    }

    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ByPeopleCommentList.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("pid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"bypeople/getallappcomments/?page=" + page_count + "&psize=30", nameValuePairs, ByPeopleCommentList.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() < 30 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String informationID = jsonObject1.getString("PostID");
                        String comment = jsonObject1.getString("Comment");
                        String date = jsonObject1.getString("Date");
                        String userID = jsonObject1.getString("UserID");
                        String is_approved = jsonObject1.getString("Is_Approved");
                        String name = jsonObject1.getString("Name");
                        String[] string = date.split(" ");
                        Log.e("str1", "--------" + string[0]);
                        Log.e("str2", "--------" + string[1]);

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

                        String fulldate = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year + " " + string[1];
                        commentLists.add(new CommentsList(comment, is_approved, userID, informationID, id, fulldate, name));
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }

            if (recyclerView_comments != null) {
                commentAdapter = new CommentAdapter(ByPeopleCommentList.this, commentLists);
                Log.e("response", "-----------------" + "object");

                if (commentAdapter.getItemCount() != 0) {
                    recyclerView_comments.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_comments.setAdapter(commentAdapter);
                    Log.e("response", "-----------------" + "setlayout");

                } else {
                    recyclerView_comments.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        loadingProgressDialog = KProgressHUD.create(ByPeopleCommentList.this);
        loadingProgressDialog.dismiss();
        commentLists = new ArrayList<>();
        page_count = 1;
        new CommentList().execute(id);

    }
}
