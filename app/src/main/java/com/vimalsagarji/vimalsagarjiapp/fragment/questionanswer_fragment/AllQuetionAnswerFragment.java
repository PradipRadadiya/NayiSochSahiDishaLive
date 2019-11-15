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
import com.vimalsagarji.vimalsagarjiapp.JSONParser;
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
import java.util.Date;
import java.util.List;


@SuppressWarnings("ALL")
public class AllQuetionAnswerFragment extends Fragment {

    public AllQuetionAnswerFragment() {

    }

    private Sharedpreferance sharedpreferance;
    private static final String TAG = AllQuetionAnswerFragment.class.getSimpleName();
    private ArrayList<String> listQuestion = new ArrayList<String>();
    private final ArrayList<String> listAnswer = new ArrayList<String>();
    private final ArrayList<String> listUserID = new ArrayList<String>();
    private final ArrayList<String> listID = new ArrayList<String>();
    private final ArrayList<String> listDate = new ArrayList<String>();
    private final ArrayList<String> listIsApproved = new ArrayList<String>();
    private final ArrayList<String> listName = new ArrayList<String>();
    private final ArrayList<String> listview = new ArrayList<String>();
    private final ArrayList<String> listflag = new ArrayList<String>();
    private Button btnAskQuestion;
    private final String res = "";


    private static final String URL = CommonUrl.Main_url+"questionanswer/viewallappques/?page=1&psize=1000";
//    private static final String URL = "http://www.aacharyavimalsagarsuriji.com/vimalsagarji/questionanswer/viewallappquesyear/?page=1&psize=1000";
    static final String PostURL = CommonUrl.Main_url+"questionanswer/askques/";
    private ListView listView;
    private Dialog dialog;

    private CustomAdpter adpter;
        private KProgressHUD loadingProgressDialog;
    private TextView txt_nodata_today;
    private EditText InputBox;
    List<ThoughtToday> listfilterdata = new ArrayList<>();
    private final String AllSearchQuestion = CommonUrl.Main_url+"questionanswer/searchallappques/?page=1&psize=1000";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
//    String approve = "";
    private ProgressBar progressbar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_question_answer, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedpreferance = new Sharedpreferance(getActivity());
        listView = (ListView) getActivity().findViewById(R.id.list);
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

        if (CommonMethod.isInternetConnected(getActivity())) {
            if (sharedpreferance.getId().equalsIgnoreCase("")){
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(URL);
            }else{
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
        btnAskQuestion = (Button) getActivity().findViewById(R.id.btnAskQuestion);

        btnAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(v);
//                    Snackbar.make(v, "Please register after ask question. ", Snackbar.LENGTH_SHORT).show();
                } else {

                    Log.e("else call","-------------");

                        Log.e("approve call","-------------");
                        dialog = new Dialog(getActivity());
//                Window window = dialog.getWindow();
//                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_askquestion_dialog);
                        dialog.setCancelable(false);
                        final EditText etAskQuestion = (EditText) dialog.findViewById(R.id.etAskQuestion);
                        Button btnPost = (Button) dialog.findViewById(R.id.btnPost);
                        final String strUid = sharedpreferance.getId();
                        final String strAskQuestion = etAskQuestion.getText().toString();
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
                                        new postData().execute(CommonMethod.encodeEmoji(etAskQuestion.getText().toString()));
                                        if (res != null) {
                                            etAskQuestion.getText().clear();
//                                    Toast.makeText(getActivity(), "Post Your Data Succesfully", Toast.LENGTH_SHORT).show();
                                        } else {
//                                    Toast.makeText(getActivity(), "Question some Occured", Toast.LENGTH_SHORT).show();
                                        }
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
            new LoadJsonTask().execute(URL);
        }else {
            new LoadJsonTask().execute(URL+"&uid="+sharedpreferance.getId());
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

    private class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressbar.setVisibility(View.VISIBLE);
            /*loadingProgressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
*/
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Log.d(TAG, "URL Path :" + url);
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(url);
                Log.e("response","-------------------"+jsonObject);
                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    listQuestion = new ArrayList<>();
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = object.getString("ID");
                        listID.add(id);
                        String strQuestion = object.getString("Question");
                        String strAnswer = object.getString("Answer");
                        String date = object.getString("Date");
                        String view = object.getString("View");
                        listview.add(view);

                        String strIsApproved = object.getString("Is_Approved");
                        String strUserID = object.getString("UserID");
                        String name = object.getString("Name");
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

                        listQuestion.add(strQuestion);
                        Log.d(TAG, "list Question data:" + listQuestion);
                        listAnswer.add(strAnswer);
                        Log.d(TAG, "list Answer data:" + listAnswer);
                        listUserID.add(strUserID);
                        Log.d(TAG, "list UserID data:" + listUserID);
                        listIsApproved.add(strIsApproved);
                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        Log.d(TAG, "list IsApproved data:" + listIsApproved);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* if (loadingProgressDialog != null) {
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

    private class LoadJsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Log.d(TAG, "URL Path :" + url);
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(url);
                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    listQuestion = new ArrayList<>();

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        String id = object.getString("ID");
                        listID.add(id);
                        String strQuestion = object.getString("Question");
                        String strAnswer = object.getString("Answer");
                        String date = object.getString("Date");
                        String strIsApproved = object.getString("Is_Approved");
                        String strUserID = object.getString("UserID");
                        String name = object.getString("Name");
                        String view = object.getString("View");
                        listview.add(view);


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

                        listQuestion.add(strQuestion);
                        Log.d(TAG, "list Question data:" + listQuestion);
                        listAnswer.add(strAnswer);
                        Log.d(TAG, "list Answer data:" + listAnswer);
                        listUserID.add(strUserID);
                        Log.d(TAG, "list UserID data:" + listUserID);
                        listIsApproved.add(strIsApproved);
                        listDate.add(dayOfTheWeek + ", " + fulldate);
                        Log.d(TAG, "list IsApproved data:" + listIsApproved);
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



            holder.txt_views.setText(listview.get(position));
            holder.txtQuestion.setText("Q: "+CommonMethod.decodeEmoji(items.get(position)));
            holder.txtAnswer.setText("A: "+CommonMethod.decodeEmoji(listAnswer.get(position)));
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
            progressbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Question", params[0]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"questionanswer/askques/", nameValuePairs, getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            loadingProgressDialog.dismiss();
            progressbar.setVisibility(View.GONE);
            Log.e("respone question ask", "---------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Thank you! You will be responded shortly.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                loadingProgressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
