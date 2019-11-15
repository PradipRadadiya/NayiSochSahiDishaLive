package com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.JSONParser;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.EventDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.EventAdpter;
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
 * Created by BHARAT on 20/01/2016.
 */
@SuppressWarnings("ALL")
public class ThisMonthEventFragment extends Fragment {

    public ThisMonthEventFragment() {

    }

    private static final String TAG = ThisMonthEventFragment.class.getSimpleName();
    private static final String constantURL = Constant.GET_ALLYEAR_EVENT_DATA;
    private static final String strMonth = "geteventsbycategorymonth";
    private static String URL = "";
    static String img = Constant.ImgURL;
    private final List<EventAdpter> listAllEvent = new ArrayList<>();
    String[] daysArray = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    String day = "";
    int dayOfWeek = 0;
    Date date;
    private final String Photo = CommonUrl.Main_url+"static/eventimage/";
    private final String Audio = CommonUrl.Main_url+"static/eventaudio/";
    private final String Video = CommonUrl.Main_url+"static/eventvideo/";
    final int drawableImage = R.drawable.event;

    private TextView txt_nodata_today;
    private EditText InputBox;
    private CustomAdpter adpter;
//    private KProgressHUD loadingProgressDialog;
    private List<EventAdpter> listfilterdata = new ArrayList<>();
    private final String MonthSearch = CommonUrl.Main_url+"event/searcheventsbycategorymonth/?page=1&psize=1000";

    private GridView gridView;
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private ProgressBar progressbar;
    ArrayList<String> timelist=new ArrayList<>();
    Sharedpreferance sharedpreferance;
    private String cid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cid = getArguments().getString("cid");
        Log.e("cid","------------------"+cid);
        return inflater.inflate(R.layout.fragment_thismonth, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedpreferance=new Sharedpreferance(getActivity());
        URL = CommonUrl.Main_url+"event/geteventsbycategorymonth/?cid=" + cid + "&page=1&psize=1000";
        progressbar= (ProgressBar) getActivity().findViewById(R.id.progressbar);
        gridView = (GridView) getActivity().findViewById(R.id.grid_thisMonth);
//        gridView = (GridView) getActivity().findViewById(R.id.grid_thisMonth);
        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);
        InputBox = (EditText) getActivity().findViewById(R.id.etText);
        ImageView imsearch = (ImageView) getActivity().findViewById(R.id.imgSerch);
        activity_main_swipe_refresh_layout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_main_swipe_refresh_layout);

        activity_main_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        InputBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });


        if (CommonMethod.isInternetConnected(getActivity())) {
            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(URL);
            } else {
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(URL+"&uid="+sharedpreferance.getId());
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


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final EventAdpter eventitem = adpter.items.get(position);
                Log.e("position list view","---------------------"+position);
                eventitem.setFlag("true");
                adpter.items.set(position,eventitem);
                adpter.notifyDataSetChanged();


                EventAdpter eventAdpter = (EventAdpter) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("click_action", "");
                intent.putExtra("listtitle", eventAdpter.getTitle());
                intent.putExtra("listDate", eventAdpter.getDate());
                intent.putExtra("listAddress", eventAdpter.getAddress());
                intent.putExtra("listID", eventAdpter.getID());
                intent.putExtra("discription", eventAdpter.getDescription());
                Log.e("discription", "-----------------------" + eventAdpter.getDescription());
                intent.putExtra("Audio", Audio + eventAdpter.getAudio());
                String audio = Audio + eventAdpter.getAudio();
                audio = audio.replace(" ", "_");
                String video = Video + eventAdpter.getVideo();
                video = video.replace(" ", "_");
                String photo = Photo + eventAdpter.getPhoto();
                photo = photo.replace(" ", "-");
                Log.e("Audio", "-----------------------" + audio);
                intent.putExtra("Video", video);
                Log.e("Video", "-----------------------" + video);
                intent.putExtra("Photo", photo);
                Log.e("Photo", "-----------------------" + photo);
                intent.putExtra("view", eventAdpter.getView());
                //intent.putExtra("listImage",eventAdpter.getPhoto());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }

    private void loadData() {
        adpter.clear();
        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            LoadJsonTask loadJsonTask = new LoadJsonTask();
            loadJsonTask.execute(URL);
        }else {
            LoadJsonTask loadJsonTask = new LoadJsonTask();
            loadJsonTask.execute(URL+"&uid="+sharedpreferance.getId());
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
           /* loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            String strUrl = params[0];
            //     String ImageURL = params[1];
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(strUrl);
                // for (int i=0; i<jsonObject.length()  ;i++){
                JSONArray array = jsonObject.getJSONArray("data");
                timelist=new ArrayList<>();
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        String strID = object.getString("ID");
                        String strTitle = object.getString("Title");
                        String strDescription = object.getString("Description");
                        String strAddress = object.getString("Address");
                        String strDate = object.getString("Date");
                        String strdate1 = strDate.substring(0, 10);
                        String strAudio = object.getString("Audio");
                        String strAudioImage = object.getString("AudioImage");
                        String strVideoLink = object.getString("VideoLink");
                        String strVideo = object.getString("Video");
                        String strVideoImage = object.getString("VideoImage");
                        String strPhoto = object.getString("Photo");
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


                        EventAdpter eventAdpter = new EventAdpter();
                        eventAdpter.setID(strID);
                        eventAdpter.setTitle(strTitle);
                        eventAdpter.setDescription(strDescription);
                        eventAdpter.setAddress(strAddress);
                        eventAdpter.setDate(fulldate);
                        eventAdpter.setAudio(strAudio);
                        eventAdpter.setAudioImage(strAudioImage);
                        eventAdpter.setVideoLink(strVideoLink);
                        eventAdpter.setVideo(strVideo);
                        eventAdpter.setVideoImage(strVideoImage);
                        eventAdpter.setPhoto(strPhoto);
                        eventAdpter.setView(view);

                        if (sharedpreferance.getId().equalsIgnoreCase("")) {
                            String flag = "true";
                            eventAdpter.setFlag(flag);
                        } else {
                            String flag = object.getString("is_viewed");
                            eventAdpter.setFlag(flag);
                        }


                        listAllEvent.add(eventAdpter);
                        timelist.add(dayOfTheWeek);
                    }
                } else {
//                    Toast.makeText(getActivity(), "No Event Found in ThisMonth", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressbar.setVisibility(View.GONE);
            /*if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            if (getActivity() != null) {

                if (gridView != null) {
                    adpter = new CustomAdpter(getActivity(), listAllEvent);
                    if (adpter.getCount() != 0) {
                        gridView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        gridView.setAdapter(adpter);
                    } else {
                        gridView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No ThisMonth Event Found", Toast.LENGTH_SHORT).show();
                    }



                } else {
                    assert gridView != null;
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(), "Gridview is null", Toast.LENGTH_SHORT).show();
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
            //     String ImageURL = params[1];
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(strUrl);
                // for (int i=0; i<jsonObject.length()  ;i++){
                JSONArray array = jsonObject.getJSONArray("data");
                timelist=new ArrayList<>();
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        String strID = object.getString("ID");
                        String strTitle = object.getString("Title");
                        String strDescription = object.getString("Description");
                        String strAddress = object.getString("Address");
                        String strDate = object.getString("Date");
                        String strdate1 = strDate.substring(0, 10);
                        String strAudio = object.getString("Audio");
                        String strAudioImage = object.getString("AudioImage");
                        String strVideoLink = object.getString("VideoLink");
                        String strVideo = object.getString("Video");
                        String strVideoImage = object.getString("VideoImage");
                        String strPhoto = object.getString("Photo");
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


                        EventAdpter eventAdpter = new EventAdpter();
                        eventAdpter.setID(strID);
                        eventAdpter.setTitle(strTitle);
                        eventAdpter.setDescription(strDescription);
                        eventAdpter.setAddress(strAddress);
                        eventAdpter.setDate(fulldate);
                        eventAdpter.setAudio(strAudio);
                        eventAdpter.setAudioImage(strAudioImage);
                        eventAdpter.setVideoLink(strVideoLink);
                        eventAdpter.setVideo(strVideo);
                        eventAdpter.setVideoImage(strVideoImage);
                        eventAdpter.setPhoto(strPhoto);
                        eventAdpter.setView(view);

                        if (sharedpreferance.getId().equalsIgnoreCase("")) {
                            String flag = "true";
                            eventAdpter.setFlag(flag);
                        } else {
                            String flag = object.getString("is_viewed");
                            eventAdpter.setFlag(flag);
                        }



                        listAllEvent.add(eventAdpter);
                        timelist.add(dayOfTheWeek);
                    }
                } else {
//                    Toast.makeText(getActivity(), "No Event Found in ThisMonth.", Toast.LENGTH_SHORT).show();
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
                if (gridView != null) {
                    adpter = new CustomAdpter(getActivity(), listAllEvent);
                    if (adpter.getCount() != 0) {
                        gridView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        gridView.setAdapter(adpter);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        gridView.setVisibility(View.GONE);
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No ThisMonth Event Found", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    assert gridView != null;
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(), "Gridview is null", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<EventAdpter> {

        final List<EventAdpter> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<EventAdpter> items) {
            super(context, R.layout.custom_event_grid_layout, items);
            this.context = context;
            this.resource = R.layout.custom_event_grid_layout;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                //holder.txt_ID = (TextView) convertView.findViewById(R.id.txtID);
                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.grid_txtTitle = (TextView) convertView.findViewById(R.id.grid_txtTitle);
                holder.grid_txtDate = (TextView) convertView.findViewById(R.id.grid_txtDate);
                holder.grid_txtday = (TextView) convertView.findViewById(R.id.grid_txtday);
                holder.img_new = (ImageView) convertView.findViewById(R.id.img_new);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            EventAdpter eventAdpterHolder = items.get(position);
            holder.txt_views.setText(CommonMethod.decodeEmoji(eventAdpterHolder.getView()));
            holder.grid_txtTitle.setText(CommonMethod.decodeEmoji(eventAdpterHolder.getTitle()));
            holder.grid_txtDate.setText(CommonMethod.decodeEmoji(eventAdpterHolder.getDate()));
            holder.grid_txtday.setText(CommonMethod.decodeEmoji(String.valueOf(timelist.get(position))));
            String strPhoto = eventAdpterHolder.getPhoto();

            if (eventAdpterHolder.getFlag().equalsIgnoreCase("true")){
                holder.img_new.setVisibility(View.GONE);
            }
            else{
                holder.img_new.setVisibility(View.VISIBLE);
            }

            return convertView;

        }

        private class ViewHolder {
            TextView grid_txtTitle, grid_txtDate, txt_views,grid_txtday;
            ImageView img_new;


        }
    }

    private void addFavoriteEvent(String id) {
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.favEvent(id, sharedpreferance.getId());
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


