package com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.JSONParser;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.InformationDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.utils.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
public class TodayInformationFragment extends Fragment {

    public TodayInformationFragment() {

    }

    //    private KProgressHUD loadingProgressDialog;
    private boolean isLoading = true;
    private int pageCount = 5;
    private static final String TAG = TodayInformationFragment.class.getSimpleName();
    private List<InformationCategory> listTodayInformationlist = new ArrayList<>();
    private final String URL = Constant.GET_TODAYINFORMATION_CATEGORY + "&psize=1000";
    private ListView listView;
    private TextView txt_nodata_today;
    private ImageView imsearch;
    private EditText InputBox;
    private CustomAdpter adpter;
    private final String TodaySearch = CommonUrl.Main_url+"info/searchallinfobycidtoday/?page=1&psize=1000";
    private List<InformationCategory> listfilterdata = new ArrayList<>();
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private Intent intent;
    private ProgressBar progressbar;
    Sharedpreferance sharedpreferance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_toaday_information, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedpreferance = new Sharedpreferance(getActivity());

        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        listView = (ListView) getActivity().findViewById(R.id.list_today);
        txt_nodata_today = (TextView) getActivity().findViewById(R.id.txt_nodata_today);
        InputBox = (EditText) getActivity().findViewById(R.id.etText);
        imsearch = (ImageView) getActivity().findViewById(R.id.imgSerch);
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final InformationCategory informationCategory1 = adpter.items.get(position);
                Log.e("position list view","---------------------"+position);
                informationCategory1.setFlag("true");
                adpter.items.set(position,informationCategory1);
                adpter.notifyDataSetChanged();


                InformationCategory informationCategory = (InformationCategory) parent.getItemAtPosition(position);
                intent = new Intent(getActivity(), InformationDetailActivity.class);
                intent.putExtra("listTitle", informationCategory.getTitle());
                Log.e("listTitle", "-----------------------------" + informationCategory.getTitle());
                intent.putExtra("listDescription", informationCategory.getDescription());
                Log.e("listDescription", "-----------------------------" + informationCategory.getDescription());
                intent.putExtra("listDate", informationCategory.getDay() + "," + informationCategory.getDate());
                Log.e("listDate", "-----------------------------" + informationCategory.getDate());
                intent.putExtra("listID", informationCategory.getID());
                intent.putExtra("click_action", "");
                intent.putExtra("time", informationCategory.getTime());
                intent.putExtra("view", informationCategory.getView());

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (adpter == null) {
                    return;
                }
                if (adpter.getCount() == 0) {
                    return;
                }

                int i = firstVisibleItem + visibleItemCount;
                System.out.println("firstVisibleItem : " + firstVisibleItem + "  visibleItemCount " + visibleItemCount + "  totalItemCount " + totalItemCount);
                if (i >= totalItemCount && !isLoading) {
                    onLoadData();
                    isLoading = true;
                }
            }
        });


    }

    private void loadData() {
//        adpter.clear();
        if (sharedpreferance.getId().equalsIgnoreCase("")) {

        } else {
            new LoadJsonTask().execute(URL + "&uid=" + sharedpreferance.getId());
        }
    }

    private void onLoadData() {
        pageCount += 5;
        new JsonTaskMore().execute(String.valueOf(pageCount));
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);

//            loadingProgressDialog = KProgressHUD.create(getActivity())
//                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                    .setLabel("Please Wait")
//                    .setCancellable(true);
//            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String status = "";
            String url = params[0];
            Log.d(TAG, "URL Path :" + url);
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(url);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    System.out.println("Today Data" + jsonArray);
                    if (jsonArray != null) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            String strID = object.getString("ID");
                            String strTitle = object.getString("Title");
                            String strDescription = object.getString("Description");
                            String strAddress = object.getString("Address");
                            String strDate = object.getString("Date");
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

                            System.out.println("Title is" + strTitle);
                            System.out.println("Jsonobject is" + object);

                            InformationCategory category = new InformationCategory();
                            category.setID(strID);
                            category.setTitle(strTitle);
                            category.setDescription(strDescription);
                            category.setAddress(strAddress);
                            category.setDate(fulldate);
                            category.setDay(dayOfTheWeek);
                            category.setView(view);


                            if (sharedpreferance.getId().equalsIgnoreCase("")){
                                String flag = "true";
                                category.setFlag(flag);
                            }else {
                                String flag = object.getString("is_viewed");
                                category.setFlag(flag);
                            }

                            listTodayInformationlist.add(category);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);

//            if (loadingProgressDialog != null) {
//                loadingProgressDialog.dismiss();
//            }
            if (getActivity() != null) {
                if (listView != null) {
                    adpter = new CustomAdpter(getActivity(), listTodayInformationlist);
                    if (adpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adpter);
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
            String status = "";
            String url = params[0];
            Log.d(TAG, "URL Path :" + url);
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(url);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    System.out.println("Today Data" + jsonArray);
                    listTodayInformationlist = new ArrayList<>();
                    if (jsonArray != null) {
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object = jsonArray.getJSONObject(j);
                            String strID = object.getString("ID");
                            String strTitle = object.getString("Title");
                            String strDescription = object.getString("Description");
                            String strAddress = object.getString("Address");
                            String strDate = object.getString("Date");
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

                            System.out.println("Title is" + strTitle);
                            System.out.println("Jsonobject is" + object);

                            InformationCategory category = new InformationCategory();
                            category.setID(strID);
                            category.setTitle(strTitle);
                            category.setDescription(strDescription);
                            category.setAddress(strAddress);
                            category.setDate(fulldate);
                            category.setDay(dayOfTheWeek);
                            category.setView(view);


                            if (sharedpreferance.getId().equalsIgnoreCase("")){
                                String flag = "true";
                                category.setFlag(flag);
                            }else {
                                String flag = object.getString("is_viewed");
                                category.setFlag(flag);
                            }

                            listTodayInformationlist.add(category);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (getActivity() != null) {
                if (listView != null) {
                    adpter = new CustomAdpter(getActivity(), listTodayInformationlist);
                    if (adpter.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adpter);
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
    public class CustomAdpter extends ArrayAdapter<InformationCategory> {
        final List<InformationCategory> items;
        final List<InformationCategory> filterdata;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<InformationCategory> items) {
            super(context, R.layout.thismonth_list_item, items);
            this.context = context;
            this.resource = R.layout.thismonth_list_item;
            this.items = items;
            this.filterdata = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txt_Title = (TextView) convertView.findViewById(R.id.txtTitle);
                holder.txt_Description = (TextView) convertView.findViewById(R.id.txtDescription);
                holder.txt_Date = (TextView) convertView.findViewById(R.id.txtDate);
                holder.txt_Address = (TextView) convertView.findViewById(R.id.txt_address);
                holder.img_new = (ImageView) convertView.findViewById(R.id.img_new);
                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            InformationCategory inCategory = items.get(position);
//            holder.txt_Title.setMaxLines((int) 1.5);
            holder.txt_Title.setText(CommonMethod.decodeEmoji(inCategory.getTitle()));
            holder.txt_Description.setText(CommonMethod.decodeEmoji(inCategory.getDescription()));
            holder.txt_Date.setText(CommonMethod.decodeEmoji(inCategory.getDay() + ", " + inCategory.getDate()));
            holder.txt_Address.setText(CommonMethod.decodeEmoji(inCategory.getAddress()));
            holder.txt_views.setText(CommonMethod.decodeEmoji(inCategory.getView()));

            if (inCategory.getFlag().equalsIgnoreCase("true")){
                holder.img_new.setVisibility(View.GONE);
            }
            else{
                holder.img_new.setVisibility(View.VISIBLE);
            }


//            InformationCategory nCategory = filterdata.get(position);
//            holder.txt_Title.setMaxLines((int) 1.5);
//            holder.txt_Title.setText(CommonMethod.decodeEmoji(nCategory.getTitle()));
//            holder.txt_Description.setText(CommonMethod.decodeEmoji(nCategory.getDescription()));
//            holder.txt_Date.setText(CommonMethod.decodeEmoji(inCategory.getDay() + ", " + inCategory.getDate()));
//            holder.txt_Address.setText(CommonMethod.decodeEmoji(inCategory.getAddress()));
//            holder.txt_views.setText(CommonMethod.decodeEmoji(inCategory.getView()));
            return convertView;
        }

        private class ViewHolder {
            TextView txt_ID, txt_Title, txt_Description, txt_Address, txt_Date, txt_views;
            private ImageView img_new;
        }

        public void addNewData(List<InformationCategory> list) {
            items.clear();
            items.addAll(list);
            this.notifyDataSetChanged();
            isLoading = false;
        }
    }

    private class JsonTaskMore extends AsyncTask<String, String, String> {
        final List<InformationCategory> list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String count = params[0];
            String url = URL + count;
            Log.d(TAG, "JsonTAskLoadMore:" + url);
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(url);
                //  for (int i = 0; i < jsonObject.length(); i++)
                //{
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject object = jsonArray.getJSONObject(j);
                    String strID = object.getString("ID");
                    String strTitle = object.getString("Title");
                    String strDescription = object.getString("Description");
                    String strAddress = object.getString("Address");
                    String strDate = object.getString("Date");
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
                    InformationCategory informationCategory = new InformationCategory();
                    informationCategory.setID(strID);
                    informationCategory.setTitle(strTitle);
                    informationCategory.setDescription(strDescription);
                    informationCategory.setAddress(strAddress);
                    informationCategory.setDate(fulldate);
                    informationCategory.setDay(dayOfTheWeek);
                    informationCategory.setTime(time[1]);
                    informationCategory.setView(view);
                    list.add(informationCategory);
                    //  }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (listView != null) {
                if (adpter != null) {
                    adpter.addNewData(list);
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        listView.setVisibility(View.GONE);
        if (CommonMethod.isInternetConnected(getActivity())) {
            listTodayInformationlist.clear();

            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(URL);
            } else {
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(URL + "&uid=" + sharedpreferance.getId());
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
}

