package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.OpinionPollAdpter;
import com.vimalsagarji.vimalsagarjiapp.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.vimalsagarji.vimalsagarjiapp.R.id.frameLayout_progressbar_no;

@SuppressWarnings("ALL")
public class OpinionPoll extends AppCompatActivity {

    private KProgressHUD loadingProgressDialog;
    //Index of Question
    private static int index_question = 0;
    //URL of Get all Question
    private static final String strURL = Constant.GET_ALL_OPINION_POLL;
    //Shared Preference Class
    private Sharedpreferance sharedpreferance;

    private List<OpinionPollAdpter> listOpinionPollAdpter = new ArrayList<>();
    private ListView listView;
    private static String status = "";
    private String message = "";
    private static String strID;
    private RelativeLayout rel_opinion;
    private String approve = "";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private TextView txt_nodata_today;
    private ProgressBar progressbar;
    ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_opinion_poll);
        sharedpreferance = new Sharedpreferance(OpinionPoll.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_opinionPoll);
        setSupportActionBar(toolbar);
        ImageView imgBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        TextView txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        txt_title.setText("Opinion Poll");
        progressbar= (ProgressBar) findViewById(R.id.progressbar);
        listView = (ListView) findViewById(R.id.listOpinionPoll);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        rel_opinion = (RelativeLayout) findViewById(R.id.rel_opninion);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        if (CommonMethod.isInternetConnected(OpinionPoll.this)) {
            new CheckUserApprove().execute();
//            GetAllOpinionPoll task = new GetAllOpinionPoll();
//            task.execute();
        } else {
            final Snackbar snackbar = Snackbar
                    .make(rel_opinion, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        activity_main_swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        activity_main_swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();

            }
        });


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
                Intent intent = new Intent(OpinionPoll.this, RegisterActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        //Customizing colors
        snackbar.setActionTextColor(Color.WHITE);
        View view = snackbar.getView();
        view.setBackground(getDrawable(R.drawable.back_gradiant));
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        //Displaying snackbar
        snackbar.show();
    }


    private void loadData() {
        new LoadGetAllOpinionPoll().execute();
    }

    private class GetAllOpinionPoll extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* loadingProgressDialog = KProgressHUD.create(OpinionPoll.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();*/



           progressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(strURL + "&user_id=" + sharedpreferance.getId());
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    index_question = 0;
                    listOpinionPollAdpter = new ArrayList<>();

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        strID = object.getString("ID");
                        Log.e("id--------------", "" + strID);
                        String strQues = object.getString("Ques");
                        Log.e("qid", "********************" + strID);
                        Log.e("Ques--------------", "" + strQues);
                        String strtotal_polls = object.getString("total_polls");
                        Log.e("total_polls------------", "" + strtotal_polls);
                        String stryes_polls = object.getString("yes_polls");
                        Log.e("yes_polls------------", "" + stryes_polls);
                        String strno_polls = object.getString("no_polls");
                        Log.e("no_polls------------", "" + strno_polls);
                        String flag = object.getString("Status");
                        index_question++;
                        String index = String.valueOf(index_question);


                        OpinionPollAdpter opinionPollAdpter = new OpinionPollAdpter();
                        opinionPollAdpter.setID(strID);
                        opinionPollAdpter.setQues(index + "). " + strQues);
                        opinionPollAdpter.setTotal_polls(strtotal_polls);
                        opinionPollAdpter.setYes_polls(stryes_polls);
                        opinionPollAdpter.setNo_polls(strno_polls);
                        opinionPollAdpter.setFlag(flag);
                        listOpinionPollAdpter.add(opinionPollAdpter);
                    }
                } else {
//                    Toast.makeText(getApplicationContext(), "No Opinion Available", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

         /*   if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
         progressbar.setVisibility(View.GONE);
            if (listView != null) {
                CustomAdpterOpinionPoll customAdpter = new CustomAdpterOpinionPoll(OpinionPoll.this, listOpinionPollAdpter);
                if (customAdpter.getCount() != 0) {
                    listView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listView.setAdapter(customAdpter);
                    customAdpter.notifyDataSetChanged();
                } else {
                    txt_nodata_today.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "No Opinion Available", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private class LoadGetAllOpinionPoll extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(strURL + "&user_id=" + sharedpreferance.getId());
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    index_question = 0;
                    listOpinionPollAdpter = new ArrayList<>();

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        strID = object.getString("ID");
                        Log.e("id--------------", "" + strID);
                        String strQues = object.getString("Ques");
                        Log.e("qid", "********************" + strID);
                        Log.e("Ques--------------", "" + strQues);
                        String strtotal_polls = object.getString("total_polls");
                        Log.e("total_polls------------", "" + strtotal_polls);
                        String stryes_polls = object.getString("yes_polls");
                        Log.e("yes_polls------------", "" + stryes_polls);
                        String strno_polls = object.getString("no_polls");
                        Log.e("no_polls------------", "" + strno_polls);
                        String flag = object.getString("Status");
                        index_question++;
                        String index = String.valueOf(index_question);


                        OpinionPollAdpter opinionPollAdpter = new OpinionPollAdpter();
                        opinionPollAdpter.setID(strID);
                        opinionPollAdpter.setQues(index + "). " + strQues);
                        opinionPollAdpter.setTotal_polls(strtotal_polls);
                        opinionPollAdpter.setYes_polls(stryes_polls);
                        opinionPollAdpter.setNo_polls(strno_polls);
                        opinionPollAdpter.setFlag(flag);
                        listOpinionPollAdpter.add(opinionPollAdpter);
                    }
                } else {
//                    Toast.makeText(getApplicationContext(), "No Opinion Available", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (listView != null) {
                CustomAdpterOpinionPoll customAdpter = new CustomAdpterOpinionPoll(OpinionPoll.this, listOpinionPollAdpter);
                if (customAdpter.getCount() != 0) {
                    listView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listView.setAdapter(customAdpter);
                    customAdpter.notifyDataSetChanged();
                    activity_main_swipe_refresh_layout.setRefreshing(false);
                } else {
                    txt_nodata_today.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "No Opinion Available", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private class CheckUserPoll extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("qid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("uid", params[1]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"opinionpoll/checkuserpoll/", nameValuePairs, OpinionPoll.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                status = jsonObject.getString("status");
                message = jsonObject.getString("message");
                if (status.equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("ID");
                        String qid = object.getString("QID");
                        String userid = object.getString("UserID");
                        String poll = object.getString("Y");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AnswerByUser extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           loadingProgressDialog = KProgressHUD.create(OpinionPoll.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
           progressbar.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("qid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("uid", params[1]));
            nameValuePairs.add(new BasicNameValuePair("Poll", params[2]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"opinionpoll/registerpoll/", nameValuePairs, OpinionPoll.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(OpinionPoll.this, "Thank you for your valuable opinion!", Toast.LENGTH_SHORT).show();
            loadingProgressDialog.dismiss();
            progressbar.setVisibility(View.GONE);
            new GetAllOpinionPoll().execute();
        }
    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdpterOpinionPoll extends ArrayAdapter<OpinionPollAdpter> {

        final List<OpinionPollAdpter> items;
        final Context context;
        final int resource;

        public CustomAdpterOpinionPoll(Context context, List<OpinionPollAdpter> items) {
            super(context, R.layout.opinionpoll_listview_custom, items);
            this.context = context;
            this.resource = R.layout.opinionpoll_listview_custom;
            this.items = items;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);
                //holder.txt_ID = (TextView) convertView.findViewById(R.id.txtID);
                holder.txtQues = (TextView) convertView.findViewById(R.id.txtQues);
                holder.txtYes = (TextView) convertView.findViewById(R.id.txtYes);
                holder.txtNo = (TextView) convertView.findViewById(R.id.txtNo);
                holder.txt_yes = (TextView) convertView.findViewById(R.id.txt_yes);
                holder.txt_no = (TextView) convertView.findViewById(R.id.txt_no);
                holder.lin = (LinearLayout) convertView.findViewById(R.id.lin);
                holder.progress_yes = (ProgressBar) convertView.findViewById(R.id.progress_yes);
                holder.progres_no = (ProgressBar) convertView.findViewById(R.id.progres_no);
                holder.frameLayout_progressbar_yes = (FrameLayout) convertView.findViewById(R.id.frameLayout_progressbar_yes);
                holder.frameLayout_progressbar_no = (FrameLayout) convertView.findViewById(frameLayout_progressbar_no);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            Log.e("status", "-------------------" + status);

            final OpinionPollAdpter opinionPollAdpter = items.get(position);

            holder.txtQues.setText(opinionPollAdpter.getQues());
            String checkpoll = opinionPollAdpter.getFlag();
            String totalpols = opinionPollAdpter.getTotal_polls();
            String yespols = opinionPollAdpter.getYes_polls();
            String nopols = opinionPollAdpter.getNo_polls();

            int total = Integer.parseInt(totalpols);
            int yes = Integer.parseInt(yespols);
            int no = Integer.parseInt(nopols);


            holder.frameLayout_progressbar_no.setVisibility(View.VISIBLE);
            holder.frameLayout_progressbar_yes.setVisibility(View.VISIBLE);


            if (checkpoll.equalsIgnoreCase("1")) {
                holder.lin.setVisibility(View.GONE);

                try {
                    float per = (yes * 100) / total;
                    String yesper = String.valueOf(per);

                    float per1 = (no * 100) / total;
                    String noper = String.valueOf(per1);

                    holder.progress_yes.setMax(total);
                    holder.progress_yes.setProgress(yes);

                    holder.progres_no.setMax(total);
                    holder.progres_no.setProgress(no);

                    String strYes = "Yes";
                    holder.txtYes.setText(yesper + "% " + strYes);
                    String strNo = "No";
                    holder.txtNo.setText(noper + "% " + strNo);
                } catch (Exception e) {
                    Log.e("exception error", "-----------------" + e.toString());
                }
            } else {
                holder.lin.setVisibility(View.VISIBLE);
                try {
                    float per = (yes * 100) / total;
                    String yesper = String.valueOf(per);

                    float per1 = (no * 100) / total;
                    String noper = String.valueOf(per1);

                    holder.progress_yes.setMax(total);
                    holder.progress_yes.setProgress(yes);

                    holder.progres_no.setMax(total);
                    holder.progres_no.setProgress(no);

                    String strYes = "Yes";
                    holder.txtYes.setText(yesper + "% " + strYes);
                    String strNo = "No";
                    holder.txtNo.setText(noper + "% " + strNo);
                } catch (Exception e) {
                    Log.e("exception error", "-----------------" + e.toString());
                }
            }


            holder.txt_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sharedpreferance.getId().equalsIgnoreCase("")) {
//                        Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                        showSnackbar(v);
                    } else {
                        if (CommonMethod.isInternetConnected(OpinionPoll.this)) {
                            Log.e("yes", "----------------------");

//                        new CheckUserPoll().execute(opinionPollAdpter.getID(), sharedpreferance.getId());
                            if (approve.equalsIgnoreCase("1")) {
                                new AnswerByUser().execute(opinionPollAdpter.getID(), sharedpreferance.getId(), "Y");
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not approved user.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            final Snackbar snackbar = Snackbar
                                    .make(rel_opinion, "No internet connection.", Snackbar.LENGTH_INDEFINITE);
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
            });
            holder.txt_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sharedpreferance.getId().equalsIgnoreCase("")) {
                        showSnackbar(v);
//                        Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (CommonMethod.isInternetConnected(OpinionPoll.this)) {


                            Log.e("no", "----------------------");
//                        new CheckUserPoll().execute(opinionPollAdpter.getID(), sharedpreferance.getId());
                            if (approve.equalsIgnoreCase("1")) {
                                new AnswerByUser().execute(opinionPollAdpter.getID(), sharedpreferance.getId(), "N");
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not approved user.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            final Snackbar snackbar = Snackbar
                                    .make(rel_opinion, "Internet not connected.", Snackbar.LENGTH_INDEFINITE);
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
            });

            return convertView;
        }

        private class ViewHolder {
            TextView txtYes, txtNo, txtQues;
            ProgressBar progress_yes, progres_no;
            LinearLayout lin;
            TextView txt_yes, txt_no;
            FrameLayout frameLayout_progressbar_yes, frameLayout_progressbar_no;
        }


    }

    //Check user approve or not
    private class CheckUserApprove extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"userregistration/checkuserapproveornot/?uid=" + sharedpreferance.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        approve = jsonObject1.getString("Is_Active");
                    }

                } else {
//                    Toast.makeText(OpinionPoll.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onResume() {
        super.onResume();
        // put your code here...
        if (CommonMethod.isInternetConnected(OpinionPoll.this)) {
            GetAllOpinionPoll task = new GetAllOpinionPoll();
            task.execute();
        }
    }
}
