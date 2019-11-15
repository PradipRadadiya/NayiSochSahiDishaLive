package com.vimalsagarji.vimalsagarjiapp.fragment.thought_fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.ThoughtsDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.ThoughtToday;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BHARAT on 04/02/2016.
 */
@SuppressWarnings("ALL")
public class ThisMonthThoughtsFragment extends Fragment {


    public ThisMonthThoughtsFragment() {

    }

    private List<ThoughtToday> list = new ArrayList<>();
    private ListView listView;
    private CustomAdapter adapter1;

    //    private KProgressHUD loadingProgressDialog;
    private TextView txt_nodata_today;


    private EditText InputBox;
    private List<ThoughtToday> listfilterdata = new ArrayList<>();
    private final String MonthSearchThought = CommonUrl.Main_url+"thought/searchallthoughtsbycidmonth/?page=1&psize=1000";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private ProgressBar progressbar;
    Sharedpreferance sharedpreferance;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thismonth_thoughts, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedpreferance = new Sharedpreferance(getActivity());
        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        listView = (ListView) getActivity().findViewById(R.id.listmonth);
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
                GetMonthThought getMonthThought = new GetMonthThought();
                getMonthThought.execute(CommonUrl.Main_url+"thought/getallthoughtsbycidmonth/?page=1&psize=1000");
            } else {
                GetMonthThought getMonthThought = new GetMonthThought();
                getMonthThought.execute(CommonUrl.Main_url+"thought/getallthoughtsbycidmonth/?page=1&psize=1000" + "&uid=" + sharedpreferance.getId());
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final ThoughtToday thoughtTodays = adapter1.items.get(position);
                Log.e("position list view", "---------------------" + position);
                thoughtTodays.setFlag("true");
                adapter1.items.set(position, thoughtTodays);
                adapter1.notifyDataSetChanged();


                ThoughtToday thoughtToday = (ThoughtToday) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ThoughtsDetailActivity.class);
                intent.putExtra("click_action", "");
                intent.putExtra("thoughtid", thoughtToday.getId());
                intent.putExtra("listTitle", thoughtToday.getTitle());
                intent.putExtra("listDescription", thoughtToday.getDescription());
                intent.putExtra("listDate", thoughtToday.getDate());
                intent.putExtra("view", thoughtToday.getView());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        Log.e("this month", "----------------------------");
    }

    private void loadData() {
        if (sharedpreferance.getId().equalsIgnoreCase("")) {

            new LoadGetMonthThought().execute(CommonUrl.Main_url+"thought/getallthoughtsbycidmonth/?page=1&psize=1000");
        } else {
            new LoadGetMonthThought().execute(CommonUrl.Main_url+"thought/getallthoughtsbycidmonth/?page=1&psize=1000" + "&uid=" + sharedpreferance.getId());
        }
    }

    private class GetMonthThought extends AsyncTask<String, Void, String> {
        String responseJSON1 = "";

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
                responseJSON1 = CommonMethod.getStringResponse(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        String title = object.getString("Title");
                        String description = object.getString("Description");
                        String date = object.getString("Date");
                        String view = object.getString("View");

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

                        ThoughtToday thoughtToday = new ThoughtToday();
                        thoughtToday.setId(id);
                        thoughtToday.setTitle(title);
                        thoughtToday.setDescription(description);
                        thoughtToday.setDate(dayOfTheWeek + ", " + fulldate);
                        thoughtToday.setView(view);
//

                        if (sharedpreferance.getId().equalsIgnoreCase("")) {
                            String flag = "true";
                            thoughtToday.setFlag(flag);
                        } else {
                            String flag = object.getString("is_viewed");
                            thoughtToday.setFlag(flag);
                        }

                        list.add(thoughtToday);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

          /*  if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
                if (listView != null) {
                    adapter1 = new CustomAdapter(getActivity(), list);
                    if (adapter1.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adapter1);
                    } else {
                        listView.setVisibility(View.GONE);
//                    TEmptyView.init(TViewUtil.EmptyViewBuilder.getInstance(getActivity())
//                            .setShowText(true)
//                            .setEmptyText("No Data")
//                            .setEmptyTextSize(20)
//                            .setShowButton(false)
//                            .setShowIcon(true)
//
//
//                    );
//                    TViewUtil.setEmptyView(listView);

                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }
    }

    private class LoadGetMonthThought extends AsyncTask<String, Void, String> {
        String responseJSON1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON1 = CommonMethod.getStringResponse(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        String title = object.getString("Title");
                        String description = object.getString("Description");
                        String date = object.getString("Date");
                        String view = object.getString("View");

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

                        ThoughtToday thoughtToday = new ThoughtToday();
                        thoughtToday.setId(id);
                        thoughtToday.setTitle(title);
                        thoughtToday.setDescription(description);
                        thoughtToday.setDate(dayOfTheWeek + ", " + fulldate);
                        thoughtToday.setView(view);

                        if (sharedpreferance.getId().equalsIgnoreCase("")) {
                            String flag = "true";
                            thoughtToday.setFlag(flag);
                        } else {
                            String flag = object.getString("is_viewed");
                            thoughtToday.setFlag(flag);
                        }

                        list.add(thoughtToday);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (getActivity() != null) {
                if (listView != null) {
                    adapter1 = new CustomAdapter(getActivity(), list);
                    if (adapter1.getCount() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        txt_nodata_today.setVisibility(View.GONE);
                        listView.setAdapter(adapter1);
                        activity_main_swipe_refresh_layout.setRefreshing(false);
                    } else {
                        listView.setVisibility(View.GONE);
//                    TEmptyView.init(TViewUtil.EmptyViewBuilder.getInstance(getActivity())
//                            .setShowText(true)
//                            .setEmptyText("No Data")
//                            .setEmptyTextSize(20)
//                            .setShowButton(false)
//                            .setShowIcon(true)
//
//
//                    );
//                    TViewUtil.setEmptyView(listView);

                        txt_nodata_today.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }
    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdapter extends ArrayAdapter<ThoughtToday> {

        final List<ThoughtToday> items;
        final Context context;
        final int resource;

        public CustomAdapter(Context context, List<ThoughtToday> items) {
            super(context, R.layout.thoughts_all_list_item, items);
            this.context = context;
            this.resource = R.layout.thoughts_all_list_item;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                //holder.txt_ID = (TextView) convertView.findViewById(R.id.txtID);
                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txt_Title = (TextView) convertView.findViewById(R.id.txtTitle);
                holder.txt_Description = (TextView) convertView.findViewById(R.id.txtDescription);
                holder.txt_Date = (TextView) convertView.findViewById(R.id.txtDate);
                holder.img_new = (ImageView) convertView.findViewById(R.id.img_new);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ThoughtToday ats = items.get(position);
            holder.txt_views.setText(CommonMethod.decodeEmoji(ats.getView()));
            holder.txt_Title.setText(CommonMethod.decodeEmoji(ats.getTitle()));
            holder.txt_Description.setText(CommonMethod.decodeEmoji(ats.getDescription()));
            holder.txt_Date.setText(CommonMethod.decodeEmoji(ats.getDate()));

            if (ats.getFlag().equalsIgnoreCase("true")) {
                holder.img_new.setVisibility(View.GONE);
            } else {
                holder.img_new.setVisibility(View.VISIBLE);
            }


            return convertView;

        }

        private class ViewHolder {
            TextView txt_ID, txt_Title, txt_Description, txt_Date, txt_views;
            ImageView img_new;
        }


    }


}
