package com.vimalsagarji.vimalsagarjiapp.fragment.video_fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerVideoAllAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.VideoItem;
import com.vimalsagarji.vimalsagarjiapp.util.AllOurCommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static com.vimalsagarji.vimalsagarjiapp.today_week_month_year.VideoCategoryItem.video_cat_id;


@SuppressWarnings("ALL")
public class VideoMonthFragment extends Fragment {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView_video;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<VideoItem> videoItemArrayList = new ArrayList<>();
    private RecyclerVideoAllAdapter recyclerVideoAllAdapter;
    //page count
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private ProgressDialog progressDialog;
    private ImageView img_nodata;
    private ProgressBar progress_load;
    private TextView txt_totaluser;
    private SearchView user_searchview;
    private Sharedpreferance sharedpreferance;
    private Button btnAskQuestions;
    private Dialog dialog;
    private String mainurl;
    private TextView txt_nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.question_fragment, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        sharedpreferance = new Sharedpreferance(getActivity());

        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            mainurl = CommonUrl.Main_url+"video/getvideobycategorymonth/?cid="+video_cat_id+"&page=";

        } else {
            mainurl = CommonUrl.Main_url+"video/getvideobycategorymonth/?cid="+video_cat_id+"&uid=" + sharedpreferance.getId() + "&page=";
        }

        findID();
//        linearLayoutManager=new LinearLayoutManager(getActivity());


        recyclerView_video = (RecyclerView) rootview.findViewById(R.id.recyclerView_qa);
        recyclerView_video.setLayoutManager(linearLayoutManager);

        recyclerVideoAllAdapter = new RecyclerVideoAllAdapter(getActivity(), videoItemArrayList);
        recyclerView_video.setAdapter(recyclerVideoAllAdapter);


        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipe_refresh.setRefreshing(false);
                if (CommonMethod.isInternetConnected(getActivity())) {
                    swipe_refresh.setRefreshing(false);
                    //refreshContent();
                } else {
                    swipe_refresh.setRefreshing(false);
                }
            }
        });

        recyclerView_video.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                                               if (CommonMethod.isInternetConnected(getActivity())) {

                                                                   Log.e("total count", "--------------------" + page_count);

                                                                   page_count++;
                                                                   new GetAllVideo().execute();
                                                               } else {
                                                                   //internet not connected
                                                               }
                                                               loading = true;


                                                           }

                                                       }
                                                   }

                                               }

        );


        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetAllVideo().execute();
        }
        return rootview;
    }

    private void findID() {
        swipe_refresh = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh);
        txt_nodata = rootview.findViewById(R.id.txt_nodata);
//        recyclerView_users = (RecyclerView) rootview.findViewById(R.id.recyclerView_users);
//        recyclerView_users.setLayoutManager(linearLayoutManager);
        img_nodata = (ImageView) rootview.findViewById(R.id.img_nodata);
        progress_load = (ProgressBar) rootview.findViewById(R.id.progress_load);
        btnAskQuestions = rootview.findViewById(R.id.btnAskQuestions);
        btnAskQuestions.setVisibility(View.GONE);


    }

    //Method to show the snackbar
    private void showSnackbar(View v) {
        //Creating snackbar
        Snackbar snackbar = Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_LONG);

        //Adding action to snackbar
        snackbar.setAction("Register", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Displaying another snackbar when user click the action for first snackbar
//                Snackbar s = Snackbar.make(v, "Register", Snackbar.LENGTH_LONG);
//                s.show();
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });

        //Customizing colors
        snackbar.setActionTextColor(Color.WHITE);
        View view = snackbar.getView();
        view.setBackground(getActivity().getDrawable(R.drawable.back_gradiant));
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);


        //Displaying snackbar
        snackbar.show();
    }

    private class GetAllVideo extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
//            recyclerView_users.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(mainurl + page_count + "&psize=20");
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {


                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String VideoName = jsonObject1.getString("VideoName");
                        String CategoryID = jsonObject1.getString("CategoryID");
                        String Video = jsonObject1.getString("Video");
                        String Photo = AllOurCommonUrl.videopath+"videoimage/"+jsonObject1.getString("Photo");

                        Log.e("image","---------------------"+Photo);


                        String Duration = jsonObject1.getString("Duration");
                        String Date = jsonObject1.getString("Date");
                        String View = jsonObject1.getString("View");
                        String Description = jsonObject1.getString("Description");
                        String video_link = jsonObject1.getString("video_link");
                        String viewed_user = jsonObject1.getString("viewed_user");
                        String Name = jsonObject1.getString("Name");

                        String is_viewed;

                        if (sharedpreferance.getId().equalsIgnoreCase("")) {
                            String flag = "true";
                            is_viewed = flag;
                        } else {
                            is_viewed = jsonObject1.getString("is_viewed");
                        }

                        String[] string = Date.split(" ");
                        Log.e("str1", "--------" + string[0]);
                        Log.e("str2", "--------" + string[1]);

                        Date dt = CommonMethod.convert_date(Date);
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
                        videoItemArrayList.add(new VideoItem(id, VideoName, CategoryID, Video, Photo, Duration, fulldate, View, Description,video_link,viewed_user,Name,is_viewed));

                    }

                    recyclerVideoAllAdapter.notifyDataSetChanged();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progress_load.setVisibility(View.GONE);
            if (videoItemArrayList.isEmpty()) {
                txt_nodata.setVisibility(View.VISIBLE);
                Log.e("nodata", "-------------");
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

    }
}
