package com.vimalsagarji.vimalsagarjiapp.fragment.bypeople_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.JSONParser;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.ByPeopleDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.AllByPeople;
import com.vimalsagarji.vimalsagarjiapp.retrofit.APIClient;
import com.vimalsagarji.vimalsagarjiapp.retrofit.ApiInterface;
import com.vimalsagarji.vimalsagarjiapp.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BHARAT on 09/02/2016.
 */
@SuppressWarnings("ALL")
public class ThisMonthbyPeopleFragment extends Fragment {

    public ThisMonthbyPeopleFragment() {

    }

    private static final String TAG = AllbyPeopleFragment.class.getSimpleName();

    private static final String AudioPath = CommonUrl.Main_url+"static/bypeopleaudio/";
    private static final String VideoPath = CommonUrl.Main_url+"static/bypeoplevideo/";
    private static final String strImageUrlName = "bypeopleimage";
    private static final String ImgURL = Constant.ImgURL;
    private static final String IMAGEURL = ImgURL.replace("audioimage", strImageUrlName);
    private String strID = "";
    private String strTitle = "";
    private String strPost = "";
    private String strAudio = "";
    private String strAudioImage = "";
    private String strVideo = "";
    private String strVideoImage = "";
    private String strVideoLink = "";
    private String strUserID = "";
    private String strIs_Approved = "";
    private String strDate = "";
    private String strPhoto = "";


    private List<AllByPeople> listAllByPeople = new ArrayList<>();
    private ListView listView;
    private String strImageUrl = "";

    private final String strURL = CommonUrl.Main_url+"bypeople/getallapppostsmonth/?page=1&psize=1000";
    //    private KProgressHUD loadingProgressDialog;
    private TextView txt_nodata_today;
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private ProgressBar progressbar;
    Sharedpreferance sharedpreferance;

    CustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_all_bypeople, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedpreferance = new Sharedpreferance(getActivity());
        Log.d(TAG, "Image Url Of By people" + IMAGEURL);
        listView = (ListView) getActivity().findViewById(R.id.byPeople_list);
        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);
        activity_main_swipe_refresh_layout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_main_swipe_refresh_layout);
        activity_main_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final AllByPeople allByPeoples = adapter.items.get(position);
                Log.e("position list view","---------------------"+position);
                allByPeoples.setFlag("true");
                adapter.items.set(position,allByPeoples);
                adapter.notifyDataSetChanged();


                AllByPeople allByPeople = (AllByPeople) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ByPeopleDetailActivity.class);
                intent.putExtra("click_action", "");
                intent.putExtra("postId", allByPeople.getId());
                intent.putExtra("listTitle", allByPeople.getTitle());
                intent.putExtra("listPost", allByPeople.getPost());
                intent.putExtra("listDate", allByPeople.getDate());
                intent.putExtra("video", VideoPath + allByPeople.getVideo().replaceAll(" ", "%20"));
                intent.putExtra("image", IMAGEURL + allByPeople.getPhoto().replaceAll(" ", "%20"));
                intent.putExtra("audio", AudioPath + allByPeople.getAudio().replaceAll(" ", "%20"));
                intent.putExtra("view", allByPeople.getView());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        if (CommonMethod.isInternetConnected(getActivity())) {

            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(strURL, IMAGEURL);
            } else {
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(strURL + "&uid=" + sharedpreferance.getId(), IMAGEURL);
            }

        } else {
            final Snackbar snackbar = Snackbar
                    .make(getView(), "No internet connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        }


    }

    private void loadData() {
        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            LoadJsonTask jsonTask = new LoadJsonTask();
            jsonTask.execute(strURL + "&uid=" + sharedpreferance.getId(), IMAGEURL);
        } else {
            LoadJsonTask jsonTask = new LoadJsonTask();
            jsonTask.execute(strURL, IMAGEURL);
        }

    }

    private class JsonTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String strUrl = params[0];
            String ImageURL = params[1];
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(strUrl);
                listAllByPeople = new ArrayList<>();
                JSONArray array = jsonObject.getJSONArray("data");
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        strID = object.getString("ID");
                        strPost = object.getString("Post");
                        strTitle = object.getString("Title");
                        strAudio = object.getString("Audio");

                        strAudioImage = object.getString("AudioImage");
                        strVideo = object.getString("Video");
                        strVideoImage = object.getString("VideoImage");
                        strVideoLink = object.getString("VideoLink");
                        strUserID = object.getString("UserID");
                        Log.d(TAG, "strUserID" + strUserID);
                        strIs_Approved = object.getString("Is_Approved");
                        strDate = object.getString("Date");
                        strPhoto = object.getString("Photo");

                        strImageUrl = strPhoto;

                        String name = object.getString("Name");
                        String view = object.getString("View");


                        Date dt = CommonMethod.convert_date(strDate);

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

                        String[] time = strDate.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        AllByPeople abp = new AllByPeople();

                        abp.setId(strID);
                        abp.setPost(strPost);
                        abp.setDate(dayOfTheWeek + ", " + fulldate);
                        abp.setTitle(strTitle);
                        abp.setAudio(strAudio);
                        abp.setAudioimage(strAudioImage);
                        abp.setPhoto(strPhoto);
                        abp.setIsapproved(strIs_Approved);
                        abp.setUserid(strUserID);
                        abp.setVideoimage(strVideoImage);
                        abp.setVideo(strVideo);
                        abp.setVideolink(strVideoLink);
                        abp.setName(name);
                        abp.setView(view);

                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            String flag = "true";
                            abp.setFlag(flag);
                        }else {
                            String flag = object.getString("is_viewed");
                            abp.setFlag(flag);
                        }

                        listAllByPeople.add(abp);


                    }
                } else {
                    Toast.makeText(getActivity(), "Data not found.", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /*if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    adapter = new CustomAdapter(getActivity(), listAllByPeople);
                    if (adapter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adapter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
                    }


                }
            }
        }

    }

    private class LoadJsonTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String strUrl = params[0];
            String ImageURL = params[1];
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(strUrl);
                listAllByPeople = new ArrayList<>();
                JSONArray array = jsonObject.getJSONArray("data");
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        strID = object.getString("ID");
                        strPost = object.getString("Post");
                        strTitle = object.getString("Title");
                        strAudio = object.getString("Audio");

                        strAudioImage = object.getString("AudioImage");
                        strVideo = object.getString("Video");
                        strVideoImage = object.getString("VideoImage");
                        strVideoLink = object.getString("VideoLink");
                        strUserID = object.getString("UserID");
                        Log.d(TAG, "strUserID" + strUserID);
                        strIs_Approved = object.getString("Is_Approved");
                        strDate = object.getString("Date");
                        strPhoto = object.getString("Photo");
                        strImageUrl = ImageURL + strPhoto;
                        String name = object.getString("Name");
                        String view = object.getString("View");


                        Date dt = CommonMethod.convert_date(strDate);
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

                        String[] time = strDate.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        AllByPeople abp = new AllByPeople();
                        abp.setId(strID);
                        abp.setPost(strPost);
                        abp.setDate(dayOfTheWeek + ", " + fulldate);
                        abp.setTitle(strTitle);
                        abp.setAudio(strAudio);
                        abp.setAudioimage(strAudioImage);
                        abp.setPhoto(strPhoto);
                        abp.setIsapproved(strIs_Approved);
                        abp.setUserid(strUserID);
                        abp.setVideoimage(strVideoImage);
                        abp.setVideo(strVideo);
                        abp.setVideolink(strVideoLink);
                        abp.setName(name);
                        abp.setView(view);


                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            String flag = "true";
                            abp.setFlag(flag);
                        }else {
                            String flag = object.getString("is_viewed");
                            abp.setFlag(flag);
                        }

                        listAllByPeople.add(abp);


                    }
                } else {
                    Toast.makeText(getActivity(), "Data not found.", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (getActivity() != null) {
                if (listView != null) {
                    adapter = new CustomAdapter(getActivity(), listAllByPeople);
                    if (adapter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adapter);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
                    }

                }

            }
        }

    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdapter extends ArrayAdapter<AllByPeople> {

        final List<AllByPeople> items;
        final Context context;
        final int resource;

        public CustomAdapter(Context context, List<AllByPeople> items) {
            super(context, R.layout.allpeoplelistitem, items);
            this.context = context;
            this.resource = R.layout.allpeoplelistitem;
            this.items = items;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                holder.imgAudio = (ImageView) convertView.findViewById(R.id.imgAudio);
                holder.txtPost = (TextView) convertView.findViewById(R.id.txtPost);
                holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
                holder.txt_postby = (TextView) convertView.findViewById(R.id.txt_postby);
                holder.img_new = (ImageView) convertView.findViewById(R.id.img_new);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AllByPeople allByPeople = items.get(position);
//            Picasso.with(getActivity()).load(IMAGEURL + allByPeople.getPhoto().replaceAll(" ", "%20")).placeholder(R.drawable.loader).resize(0,200).error(R.drawable.bypeople_error).into(holder.imgAudio);


            holder.txt_views.setText(CommonMethod.decodeEmoji(allByPeople.getView()));
            holder.txtTitle.setText(CommonMethod.decodeEmoji(allByPeople.getTitle()));
            holder.txtPost.setText(CommonMethod.decodeEmoji(allByPeople.getPost()));
            holder.txtDate.setText(CommonMethod.decodeEmoji(allByPeople.getDate()));
            holder.txt_postby.setText("Post By : " + CommonMethod.decodeEmoji(allByPeople.getName()));

            if (allByPeople.getFlag().equalsIgnoreCase("true")) {
                holder.img_new.setVisibility(View.GONE);
            } else {
                holder.img_new.setVisibility(View.VISIBLE);
            }

            return convertView;

        }

        private class ViewHolder {
            TextView txtTitle, txtPost, txtDate, txt_postby, txt_views;
            ImageView imgAudio, img_new;

        }

    }
    private void addFavoriteByPeople(String id) {
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.favByPeople(id, sharedpreferance.getId());
        callApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                Log.e("reponse", "-----------------" + response.body());
                if (response.isSuccessful()) {

                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        } else {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                Toast.makeText(getActivity(), R.string.reopen, Toast.LENGTH_SHORT).show();

            }
        });
    }

}