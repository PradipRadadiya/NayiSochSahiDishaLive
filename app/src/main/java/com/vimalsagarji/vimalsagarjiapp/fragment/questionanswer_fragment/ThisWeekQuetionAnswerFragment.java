package com.vimalsagarji.vimalsagarjiapp.fragment.questionanswer_fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.ThoughtToday;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;


@SuppressWarnings("ALL")
public class ThisWeekQuetionAnswerFragment extends Fragment {

    public ThisWeekQuetionAnswerFragment() {

    }

    private Sharedpreferance sharedpreferance;
    private ArrayList<String> listQuestion = new ArrayList<String>();
    private final ArrayList<String> listAnswer = new ArrayList<String>();
    private final ArrayList<String> listUserID = new ArrayList<String>();
    private final ArrayList<String> listID = new ArrayList<String>();
    private final ArrayList<String> listDate = new ArrayList<String>();
    private final ArrayList<String> listName = new ArrayList<String>();
    private final ArrayList<String> listview = new ArrayList<String>();
    private final ArrayList<String> listflag = new ArrayList<String>();
    private ListView listView;
    private Button btn_askQuestion;
    private String strAskQuestion;

    private CustomAdpter adpter;
        private KProgressHUD loadingProgressDialog;
    private TextView txt_nodata_today;
    private EditText InputBox;
    List<ThoughtToday> listfilterdata = new ArrayList<>();
    private final String WeekSearchQuestion = CommonUrl.Main_url+"questionanswer/searchallappquesweek/?page=1&psize=1000";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private Dialog dialog;
//    String approve = "";
    private ProgressBar progressbar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thisweek_question_answer, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.listview_week);


        progressbar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
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

        btn_askQuestion = (Button) getActivity().findViewById(R.id.btn_askQuestion1);
        sharedpreferance = new Sharedpreferance(getActivity());
        if (CommonMethod.isInternetConnected(getActivity())) {
            if (sharedpreferance.getId().equalsIgnoreCase("")){
                GetWeekQuestion getWeekQuestion = new GetWeekQuestion();
                getWeekQuestion.execute(CommonUrl.Main_url+"questionanswer/viewallappquesweek/?page=1&psize=1000");
            }else {
                GetWeekQuestion getWeekQuestion = new GetWeekQuestion();
                getWeekQuestion.execute(CommonUrl.Main_url+"questionanswer/viewallappquesweek/?page=1&psize=1000"+"&uid="+sharedpreferance.getId());
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


                listflag.set(position,"true");
                adpter.notifyDataSetChanged();

                Intent intent = new Intent(getActivity(), AllQuestionDetail.class);
                Log.e("Question", listQuestion.get(position));
                Log.e("Answer", listAnswer.get(position));
                intent.putExtra("Question", listQuestion.get(position));
                intent.putExtra("Answer", listAnswer.get(position));
                intent.putExtra("view", listview.get(position));
                intent.putExtra("qid", listID.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        btn_askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(v);
//                    Snackbar.make(v, "Please register after ask question. ", Snackbar.LENGTH_SHORT).show();
                } else {

                        dialog = new Dialog(getActivity());
//                Window window = dialog.getWindow();
//                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_askquestion_dialog);
                        dialog.setCancelable(false);
                        final EditText etAskQuestion = (EditText) dialog.findViewById(R.id.etAskQuestion);
                        Button btnPost = (Button) dialog.findViewById(R.id.btnPost);
                        final String strUid = sharedpreferance.getId();
                        strAskQuestion = etAskQuestion.getText().toString();
                        dialog.show();
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        final ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
                        img_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnPost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (CommonMethod.isInternetConnected(getActivity())) {
                                    String txtpost = etAskQuestion.getText().toString();
                                    if (TextUtils.isEmpty(txtpost)) {
                                        etAskQuestion.setError("Please enter your question!");
                                        etAskQuestion.requestFocus();
                                    } else {
                                        new postData().execute(CommonMethod.encodeEmoji(txtpost));
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
                        });

                }
            }
        });

    }

    private void loadData() {
        adpter.clear();
        if (sharedpreferance.getId().equalsIgnoreCase("")){
            new LoadGetWeekQuestion().execute(CommonUrl.Main_url+"questionanswer/viewallappquesweek/?page=1&psize=1000");
        }else {
            new LoadGetWeekQuestion().execute(CommonUrl.Main_url+"questionanswer/viewallappquesweek/?page=1&psize=1000"+"&uid="+sharedpreferance.getId());
        }
    }

    private class GetWeekQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
            /*loadingProgressDialog = KProgressHUD.create(getActivity())
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
                        listID.add(id);
                        String Question = object.getString("Question");
                        String Answer = object.getString("Answer");
                        String Date = object.getString("Date");
                        String uid = object.getString("UserID");
                        String name = object.getString("Name");
                        String view = object.getString("View");
                        listview.add(view);
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
                        listQuestion.add(Question);
                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        listAnswer.add(Answer);
                        listUserID.add(uid);
                        listName.add(name);
                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            String flag = "true";
                            listflag.add(flag);
                        }else {
                            String flag = object.getString("is_viewed");
                            listflag.add(flag);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
/*
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            if (getActivity() != null) {
            if (listView != null) {
                adpter = new CustomAdpter(getActivity(), listQuestion);
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

    private class LoadGetWeekQuestion extends AsyncTask<String, Void, String> {
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
                        listID.add(id);
                        String Question = object.getString("Question");
                        String Answer = object.getString("Answer");
                        String Date = object.getString("Date");
                        String uid = object.getString("UserID");
                        String name = object.getString("Name");
                        String view = object.getString("View");
                        listview.add(view);
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
                        listQuestion.add(Question);
                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        listAnswer.add(Answer);
                        listUserID.add(uid);
                        listName.add(name);
                        if (sharedpreferance.getId().equalsIgnoreCase("")){
                            String flag = "true";
                            listflag.add(flag);
                        }else {
                            String flag = object.getString("is_viewed");
                            listflag.add(flag);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (getActivity() != null) {
            if (listView != null) {
                adpter = new CustomAdpter(getActivity(), listQuestion);
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

    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<String> {

        final List<String> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<String> items) {
            super(context, R.layout.question_answer_list_item, items);
            this.context = context;
            this.resource = R.layout.question_answer_list_item;
            this.items = items;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                //holder.txt_ID = (TextView) convertView.findViewById(R.id.txtID);
                holder.txt_views = (TextView) convertView.findViewById(R.id.txt_views);
                holder.txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
                holder.txtAnswer = (TextView) convertView.findViewById(R.id.txtAnswer);
                holder.txt_date = (TextView) convertView.findViewById(R.id.txt_datess);
                holder.txt_postby = (TextView) convertView.findViewById(R.id.txt_postby);
                holder.img_new = (ImageView) convertView.findViewById(R.id.img_new);
                //makeTextViewResizable(holder.txtAnswer, 2,"Read More",true);
                //  holder.btnReadMore=(Button)convertView.findViewById(R.id.btnReadMore);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_views.setText(CommonMethod.decodeEmoji(listview.get(position)));
            holder.txtQuestion.setText("Q: "+CommonMethod.decodeEmoji(items.get(position)));
            holder.txtAnswer.setText("A:"+CommonMethod.decodeEmoji(listAnswer.get(position)));
            holder.txt_date.setText(CommonMethod.decodeEmoji(listDate.get(position)));
            holder.txt_postby.setText("Question By:" + CommonMethod.decodeEmoji(listName.get(position)));


            if (listflag.get(position).equalsIgnoreCase("true")){
                holder.img_new.setVisibility(View.GONE);
            }
            else{
                holder.img_new.setVisibility(View.VISIBLE);
            }

            return convertView;

        }


        private class ViewHolder {
            TextView txtQuestion, txtAnswer, txt_date, txt_postby, txt_views;
            ImageView img_new;
            //  Button btnReadMore;

        }


    }

    private class postData extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
//            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
                nameValuePairs.add(new BasicNameValuePair("Question", params[0]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"questionanswer/askques/", nameValuePairs, getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(View.GONE);
//            loadingProgressDialog.dismiss();
            Log.e("respone question ask", "---------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    dialog.dismiss();
                    loadingProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Thank you! You will be responded shortly.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    loadingProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
