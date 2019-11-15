package com.vimalsagarji.vimalsagarjiapp.fragment.audio_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.AudioDetail;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.ThisMonthAudio;
import com.vimalsagarji.vimalsagarjiapp.retrofit.APIClient;
import com.vimalsagarji.vimalsagarjiapp.retrofit.ApiInterface;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.AudioCategoryitem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vimalsagarji.vimalsagarjiapp.R.id.imgAudio;

/**
 * Created by BHARAT on 23/01/2016.
 */
@SuppressWarnings("ALL")
public class TodayAudioFragment extends Fragment {

    public TodayAudioFragment() {

    }

    private final static String URL = CommonUrl.Main_url+"audio/getaudiobycategorytoday/?page=1&psize=1000";
    private final String ImgURL = CommonUrl.Main_url+"static/audioimage/";
    private static final String AudioPath = CommonUrl.Main_url+"static/audios/";
    private ArrayList<ThisMonthAudio> arrayList = new ArrayList<>();
    private static String strCid = "";
    private View view;
    private TextView audioImagname;
    //    private KProgressHUD loadingProgressDialog;
    private TextView txt_nodata_today;
    private EditText InputBox;
    private final String MonthSearchAudio = CommonUrl.Main_url+"audio/searchallaudiotoday/?page=1&psize=1000";
    private ListView listView;
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private ProgressBar progressbar;
    Sharedpreferance sharedpreferance;
    CustomAdpter customAdpter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_thismonth_audio, container, false);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedpreferance = new Sharedpreferance(getActivity());
        AudioCategoryitem audioCategoryitem = (AudioCategoryitem) getActivity();
        strCid = audioCategoryitem.getMydata();
        listView = (ListView) getActivity().findViewById(R.id.thismonth_audio);
        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);
        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
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


        if (strCid.equalsIgnoreCase("e_alliamgeid")) {
            if (CommonMethod.isInternetConnected(getActivity())) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    new GetTodayEventAudio().execute(CommonUrl.Main_url+"event/geteventsbycategorytoday/?page=1&psize=1000");
                } else {
                    new GetTodayEventAudio().execute(CommonUrl.Main_url+"event/geteventsbycategorytoday/?page=1&psize=1000" + "&uid=" + sharedpreferance.getId());
                }
            } else {
                Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
            }
        } else if (strCid.equalsIgnoreCase("bypeopleidid")) {
            if (CommonMethod.isInternetConnected(getActivity())) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    new GetTodayByPeople().execute(CommonUrl.Main_url+"bypeople/getallapppoststoday/?page=1&psize=1000");
                } else {
                    new GetTodayByPeople().execute(CommonUrl.Main_url+"bypeople/getallapppoststoday/?page=1&psize=1000"+"&uid="+sharedpreferance.getId());
                }
            } else {
                Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
            }
        } else {

            if (CommonMethod.isInternetConnected(getActivity())) {
//                GetMonthAudio monthAudio = new GetMonthAudio();
//                monthAudio.execute();
            } else {
                Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
            }

        }


        audioImagname = (TextView) getActivity().findViewById(R.id.audioImagname);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final ThisMonthAudio informationCategory1 = customAdpter.items.get(position);
                Log.e("position list view","---------------------"+position);
                informationCategory1.setFlag("true");
                customAdpter.items.set(position,informationCategory1);
                customAdpter.notifyDataSetChanged();


                Intent i = new Intent(getActivity(), AudioDetail.class);
                i.putExtra("click_action", "");
                i.putExtra("id", customAdpter.items.get(position).getID());
                i.putExtra("AudioName", customAdpter.items.get(position).getAudioName());
                i.putExtra("CategoryID", customAdpter.items.get(position).getCategoryID());
                i.putExtra("Audio", customAdpter.items.get(position).getAudio());
                i.putExtra("Photo", customAdpter.items.get(position).getPhoto());
                i.putExtra("Duration", customAdpter.items.get(position).getDuration());
                i.putExtra("Date", customAdpter.items.get(position).getDate());
                i.putExtra("view", customAdpter.items.get(position).getView());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

    }

    private void loadData() {
        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            new LoadGetMonthAudio().execute(URL + "&cid=" + strCid);
        } else {
            new LoadGetMonthAudio().execute(URL + "&cid=" + strCid + "&uid=" + sharedpreferance.getId());
        }
    }

    private class GetMonthAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

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

            try {
                responseJSON = CommonMethod.getStringResponse(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) ;
                {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        String AudioName = object.getString("AudioName");
                        String CategoryID = object.getString("CategoryID");
                        String Audio = AudioPath + object.getString("Audio");
                        String Photo = ImgURL + object.getString("Photo");
                        String Duration = object.getString("Duration");
                        String Date = object.getString("Date");
                        String view = object.getString("View");
                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        String flag=null;
                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            flag = "true";
                        }else {
                            flag = object.getString("is_viewed");
                        }

                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view, flag));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    customAdpter = new CustomAdpter(getActivity(), arrayList);

                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Data");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }


    }

    private class LoadGetMonthAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) ;
                {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        String AudioName = object.getString("AudioName");
                        String CategoryID = object.getString("CategoryID");
                        String Audio = AudioPath + object.getString("Audio");
                        String Photo = ImgURL + object.getString("Photo");
                        String Duration = object.getString("Duration");
                        String Date = object.getString("Date");
                        String view = object.getString("View");
                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);

                        String flag=null;
                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            flag = "true";
                        }else {
                            flag = object.getString("is_viewed");
                        }

                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view, flag));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (getActivity() != null) {

                if (listView != null) {
                    customAdpter = new CustomAdpter(getActivity(), arrayList);

                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Data");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }


    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<ThisMonthAudio> {

        final List<ThisMonthAudio> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<ThisMonthAudio> items) {
            super(context, R.layout.audiocategorylistviewsubelement, items);
            this.context = context;
            this.resource = R.layout.audiocategorylistviewsubelement;
            this.items = items;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.btnComment = (Button) convertView.findViewById(R.id.btnComment);
                holder.btnLike = (Button) convertView.findViewById(R.id.btnLike);
                holder.btnLikeClick = (Button) convertView.findViewById(R.id.btnLikeClick);
                holder.txtAudioName = (TextView) convertView.findViewById(R.id.txtAudioName);
                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txtAudioName = (TextView) convertView.findViewById(R.id.txtAudioName);
                holder.txtAudioDate = (TextView) convertView.findViewById(R.id.txtAudioDate);
                holder.imgAudio = (ImageView) convertView.findViewById(imgAudio);
                holder.imgPlayAudio = (ImageView) convertView.findViewById(R.id.imgPlayAudio);
                holder.imgPlayPush = (ImageView) convertView.findViewById(R.id.imgPlayPush);
                holder.imgPlayAudio1 = (ImageView) convertView.findViewById(R.id.imgPlayAudio1);
                holder.img_new = (ImageView) convertView.findViewById(R.id.img_new);
              /*  holder.imgPlayAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("play audio", "------------------");
                       *//* final ThisMonthAudio informationCategory1 = customAdpter.items.get(position);
                        Log.e("position list view","---------------------"+position);
                        informationCategory1.setFlag("true");
                        customAdpter.items.set(position,informationCategory1);
                        customAdpter.notifyDataSetChanged();


                        Intent i = new Intent(getActivity(), AudioDetail.class);
                        i.putExtra("click_action", "");
                        i.putExtra("id", items.get(position).getID());
                        i.putExtra("AudioName", items.get(position).getAudioName());
                        i.putExtra("CategoryID", items.get(position).getCategoryID());
                        i.putExtra("Audio", items.get(position).getAudio());
                        i.putExtra("Photo", items.get(position).getPhoto());
                        i.putExtra("Duration", items.get(position).getDuration());
                        i.putExtra("Date", items.get(position).getDate());
                        i.putExtra("view", items.get(position).getView());
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*//*
                    }

                });
*/

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_views.setText(items.get(position).getView());
            holder.txtAudioName.setText(items.get(position).getAudioName());
            holder.txtAudioDate.setText(items.get(position).getDate());
//            Picasso.with(getActivity()).load(items.get(position).getPhoto().replaceAll(" ", "%20")).placeholder(R.drawable.loader).resize(0, 200).error(R.drawable.no_image).into(holder.imgAudio);

            Glide.with(getActivity()).load(items.get(position).getPhoto()
                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(holder.imgAudio);

//            audioImagname.setText(items.get(position).getAudioName());


//            holder.imgAudio.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

            if (items.get(position).getFlag().equalsIgnoreCase("true")){
                holder.img_new.setVisibility(View.GONE);
            }
            else{
                holder.img_new.setVisibility(View.VISIBLE);
            }


            return convertView;
        }


        private class ViewHolder {
            TextView txtAudioName, txtAudioDate, txtDuration, txt_views;
            Button btnLike, btnComment, btnLikeClick;
            ImageView imgAudio, imgPlayAudio, imgPlayPush, imgPlayAudio1, img_new;

        }
    }

    private class GetTodayEventAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) ;
                {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = "eid";
                        String AudioName = object.getString("Title");
                        String CategoryID = "cid";
                        String Audio = CommonUrl.Main_url+"static/eventaudio/" + object.getString("Audio");
                        String Photo = CommonUrl.Main_url+"static/eventimage/" + object.getString("Photo");
                        String Duration = "5";
                        String Date = object.getString("Date");
                        String view = object.getString("View");
                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);

                        String flag=null;
                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            flag = "true";
                        }else {
                            flag = object.getString("is_viewed");
                        }

                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view, flag));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    customAdpter = new CustomAdpter(getActivity(), arrayList);

                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Data");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }


    }

    private class GetTodayByPeople extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) ;
                {
                    arrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = "bid";
                        String AudioName = object.getString("Title");
                        String CategoryID = "cid";
                        String Audio = CommonUrl.Main_url+"static/bypeopleaudio/" + object.getString("Audio");
                        String Photo = CommonUrl.Main_url+"static/bypeopleimage/" + object.getString("Photo");
                        String Duration = "5";
                        String Date = object.getString("Date");
                        String view = object.getString("View");
                        java.util.Date dt = CommonMethod.convert_date(Date);
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

                        String[] time = Date.split("\\s+");
                        Log.e("time", "-----------------------" + time[1]);


                        String flag=null;
                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            flag = "true";
                        }else {
                            flag = object.getString("is_viewed");
                        }

                        arrayList.add(new ThisMonthAudio(id, AudioName, CategoryID, Audio, Photo, Duration, dayOfTheWeek + ", " + fulldate, view, flag));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressbar.setVisibility(View.GONE);
          /*  if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            if (getActivity() != null) {
                if (listView != null) {
                    customAdpter = new CustomAdpter(getActivity(), arrayList);

                    if (customAdpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(customAdpter);
                    } else {
                        listView.setVisibility(View.GONE);
                        txt_nodata_today.setText("No Data");
                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_SHORT).show();
                    }
                }


            }

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

        listView.setVisibility(View.GONE);
        if (strCid.equalsIgnoreCase("e_alliamgeid")) {
            Log.e("event", "--------");
            if (CommonMethod.isInternetConnected(getActivity())) {
//                new GetTodayEventAudio().execute();
            } else {
                Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
            }
        } else if (strCid.equalsIgnoreCase("bypeopleidid")) {
            Log.e("by people", "--------");
            if (CommonMethod.isInternetConnected(getActivity())) {
//                new GetTodayByPeople().execute();
            } else {
                Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Log.e("audio", "--------");


            if (CommonMethod.isInternetConnected(getActivity())) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    GetMonthAudio monthAudio = new GetMonthAudio();
                    monthAudio.execute(URL + "&cid=" + strCid);
                } else {
                    GetMonthAudio monthAudio = new GetMonthAudio();
                    monthAudio.execute(URL + "&cid=" + strCid + "&uid=" + sharedpreferance.getId());
                }


            }
        }
    }

    private void addFavoriteAudio(String id) {
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.favAudio(id, sharedpreferance.getId());
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
