package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

@SuppressWarnings("ALL")
public class CompetitionGridListActivity extends AppCompatActivity {

    private String categoryID;
    private KProgressHUD loadingProgressDialog;
    private ArrayList<CompetitionQuestion> questionArrayList = new ArrayList<>();
    private Sharedpreferance sharedpreferance;
    private static int c_index = 0;
    private String status = "";
    private TextView etParticipants;
    private RelativeLayout rel_comp_detail;
    private String approve = "";
    private SwipeRefreshLayout activity_main_swipe_refresh_layout;
    private String str = null;
    private TextView txt_nodata_today;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_competition_grid_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_competition_grid_list_iten);
        setSupportActionBar(toolbar);
        sharedpreferance = new Sharedpreferance(CompetitionGridListActivity.this);
        etParticipants = (TextView) findViewById(R.id.etParticipants);
        ImageView imgBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        rel_comp_detail = (RelativeLayout) findViewById(R.id.rel_comp_detail);
        TextView etCompetitionGriditem = (TextView) findViewById(R.id.etCompetitionGriditem);
        imgHome.setVisibility(View.GONE);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
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

        Intent getIntent = getIntent();
        String strCometitionIntentTitle = getIntent.getStringExtra("listTitle");
        categoryID = getIntent.getStringExtra("categoryID");
        etCompetitionGriditem.setText(strCometitionIntentTitle);
        etCompetitionGriditem.setTextColor(Color.BLUE);

        if (CommonMethod.isInternetConnected(CompetitionGridListActivity.this)) {
            new CheckUserApprove().execute();
            new CheckParticipants().execute("56", categoryID);
            Log.e("user id", "------------------------------" + sharedpreferance.getId());
            new CatrgoryQuestion().execute(categoryID, sharedpreferance.getId());
        } else {
            final Snackbar snackbar = Snackbar
                    .make(rel_comp_detail, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
        new LoadCatrgoryQuestion().execute(categoryID, sharedpreferance.getId());
    }

    private class ViewHolder {
        TextView txtQuestion;
        TextView edit_answer;
        Button btnReply;
        RadioGroup radiogroup;
        RadioButton radioButton;

    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<CompetitionQuestion> {

        final ArrayList<CompetitionQuestion> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, ArrayList<CompetitionQuestion> items) {
            super(context, R.layout.comp_questionlist, items);
            this.context = context;
            this.resource = R.layout.comp_questionlist;
            this.items = items;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null, false);

            holder.txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
            holder.edit_answer = (TextView) convertView.findViewById(R.id.edit_answer);
            holder.btnReply = (Button) convertView.findViewById(R.id.btnReply);
            holder.radiogroup = (RadioGroup) convertView.findViewById(R.id.radiogroup);
            holder.txtQuestion.setText(items.get(position).getQuestion());
            String qtype = items.get(position).getQType();


            if (qtype.equalsIgnoreCase("Radio")) {
                holder.edit_answer.setVisibility(View.GONE);
                holder.btnReply.setVisibility(View.GONE);

                String option = items.get(position).getOptions();
                String id = items.get(position).getID();
                Log.e("option", "---------------------" + option);
                final String[] opt = option.split(",");
                int i;
                for (i = 0; i < opt.length; i++) {
                    Log.e("opt", "------------" + opt[i]);
                    holder.radioButton = new RadioButton(CompetitionGridListActivity.this);
                    Log.e("id", "----------------" + holder.radioButton.getId());
                    holder.radioButton.setText(opt[i]);
                    holder.radiogroup.addView(holder.radioButton);
                    holder.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (CommonMethod.isInternetConnected(CompetitionGridListActivity.this)) {
                                status = "";
//                                new checkReplay().execute(items.get(position).getID(), sharedpreferance.getId());
                                if (items.get(position).getAnswer().equalsIgnoreCase("null")) {
                                    if (approve.equalsIgnoreCase("1")) {
                                        RadioButton rb = (RadioButton) findViewById(checkedId);
                                        String rbtext = (String) rb.getText();
                                        Log.e("checked id", "----------------" + checkedId);
                                        Log.e("Question id", "-------------------" + items.get(position).getID());
                                        Log.e("User Id", "------------------------" + sharedpreferance.getId());
                                        Log.e("Answer", "----------------" + rbtext);
                                        Log.e("Category Id", "--------------------" + categoryID);
                                        new postData().execute(items.get(position).getID(), sharedpreferance.getId(), rbtext, categoryID);
                                        status = "";
                                    } else {
                                        Toast.makeText(CompetitionGridListActivity.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    holder.radiogroup.setEnabled(false);
                                    Toast.makeText(CompetitionGridListActivity.this, "User has already replied.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                final Snackbar snackbar = Snackbar
                                        .make(rel_comp_detail, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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

            } else {
                final String qid = items.get(position).getID();
                holder.edit_answer.setVisibility(View.VISIBLE);
                holder.btnReply.setVisibility(View.VISIBLE);
/*


                holder.edit_answer.setOnClickListener(new View.OnClickListener() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(CompetitionGridListActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_replay);
                        dialog.show();
                        final EditText edit_replay = (EditText) dialog.findViewById(R.id.edit_replay);
                        final Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View v) {
                                Log.e("Reply", "--------------" + edit_replay.getText().toString());
                                Log.e("Reply", "--------------" + edit_replay.getText().toString());
                                str = edit_replay.getText().toString();
                                Log.e("str", "--------------" + str);
                                dialog.dismiss();
                                if (str == null) {
                                    Log.e("str", "--------------" + str);
                                } else {
                                    holder.edit_answer.setText("Hello");
                                    holder.edit_answer.setText(str);
                                    Log.e("else str", "--------------" + str);
                                }
                            }
                        });

                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    }
                });

*/

//                final String answer = holder.edit_answer.getText().toString();
                holder.btnReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (CommonMethod.isInternetConnected(CompetitionGridListActivity.this)) {
                            status = "";
//                            new checkReplay().execute(qid,sharedpreferance.getId());
                            if (TextUtils.isEmpty(holder.edit_answer.getText().toString())) {
//                                holder.edit_answer.setError("Give answer after Replay");
//                                holder.edit_answer.requestFocus();

                            } else {

                                if (items.get(position).getAnswer().equalsIgnoreCase("null")) {
                                    if (approve.equalsIgnoreCase("1")) {
                                        new postData().execute(qid, sharedpreferance.getId(), holder.edit_answer.getText().toString(), categoryID);
                                    } else {
                                        Toast.makeText(CompetitionGridListActivity.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(CompetitionGridListActivity.this, "User has already replied.", Toast.LENGTH_SHORT).show();
                                    holder.btnReply.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            final Snackbar snackbar = Snackbar
                                    .make(rel_comp_detail, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
            convertView.setTag(holder);
            return convertView;

        }

    }

    private class postData extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(CompetitionGridListActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("qid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("uid", params[1]));
                nameValuePairs.add(new BasicNameValuePair("Answer", params[2]));
                nameValuePairs.add(new BasicNameValuePair("cid", params[3]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/answer/", nameValuePairs, CompetitionGridListActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("respone", "--------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                jsonObject.getString("status");
                String msg = jsonObject.getString("message");
                jsonObject.getString("Answer");
                Toast.makeText(CompetitionGridListActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            loadingProgressDialog.dismiss();
        }
    }

    private class checkReplay extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loadingProgressDialog = KProgressHUD.create(CompetitionGridListActivity.this)
//                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                    .setLabel("Please Wait")
//                    .setCancellable(true);
//            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("qid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("uid", params[1]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/checkuserreply/", nameValuePairs, CompetitionGridListActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("respone", "--------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                Toast.makeText(CompetitionGridListActivity.this, "" + message, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            loadingProgressDialog.dismiss();
        }
    }

    private class CatrgoryQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(CompetitionGridListActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("cid", params[0]));
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", params[1]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/getallquestionsbycid/?page=1&psize=1000", nameValuePairs, CompetitionGridListActivity.this);
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
                c_index = 0;
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    questionArrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CompetitionQuestion competitionQuestion = new CompetitionQuestion();
                        String id = object.getString("ID");
                        String question = object.getString("Question");
                        String qtype = object.getString("QType");
                        String name = object.getString("Name");
                        String options = object.getString("Options");
                        String answer = object.getString("Answer");
                        Log.e("anwer", "------------" + answer);
                        if (answer.equalsIgnoreCase("null")) {
                            Log.e("anwer is", "------------" + answer);
                            answer = "null";
                        }

                        c_index++;
                        String index = String.valueOf(c_index);
                        competitionQuestion.setID(id);
                        competitionQuestion.setQuestion(index + "). " + question);
                        competitionQuestion.setQType(qtype);
                        competitionQuestion.setName(name);
                        competitionQuestion.setOptions(options);
                        competitionQuestion.setAnswer(answer);
                        questionArrayList.add(competitionQuestion);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
            ListView listView = (ListView) findViewById(R.id.listGridItemCompetition);
            if (listView != null) {
                CustomAdpter customAdpter = new CustomAdpter(getApplicationContext(), questionArrayList);
                if (customAdpter.getCount() != 0) {
                    listView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    listView.setAdapter(customAdpter);
                } else {
                    txt_nodata_today.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }

            }
        }
    }

    private class LoadCatrgoryQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("cid", params[0]));
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", params[1]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/getallquestionsbycid/?page=1&psize=1000", nameValuePairs, CompetitionGridListActivity.this);
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
                c_index = 0;
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    questionArrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CompetitionQuestion competitionQuestion = new CompetitionQuestion();
                        String id = object.getString("ID");
                        String question = object.getString("Question");
                        String qtype = object.getString("QType");
                        String name = object.getString("Name");
                        String options = object.getString("Options");
                        String answer = object.getString("Answer");
                        Log.e("anwer", "------------" + answer);
                        if (answer.equalsIgnoreCase("null")) {
                            Log.e("anwer is", "------------" + answer);
                            answer = "null";
                        }

                        c_index++;
                        String index = String.valueOf(c_index);
                        competitionQuestion.setID(id);
                        competitionQuestion.setQuestion(index + "). " + question);
                        competitionQuestion.setQType(qtype);
                        competitionQuestion.setName(name);
                        competitionQuestion.setOptions(options);
                        competitionQuestion.setAnswer(answer);
                        questionArrayList.add(competitionQuestion);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListView listView = (ListView) findViewById(R.id.listGridItemCompetition);
            if (listView != null) {
                CustomAdpter customAdpter = new CustomAdpter(getApplicationContext(), questionArrayList);
                if (customAdpter.getCount() != 0) {
                    txt_nodata_today.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(customAdpter);
                    activity_main_swipe_refresh_layout.setRefreshing(false);
                } else {
                    txt_nodata_today.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }


            }
        }
    }

    private class CheckParticipants extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loadingProgressDialog = KProgressHUD.create(CompetitionGridListActivity.this)
//                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                    .setLabel("Please Wait")
//                    .setCancellable(true);
//            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("qid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("cid", params[1]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/checkparticipants/", nameValuePairs, CompetitionGridListActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("respone", "--------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                status = jsonObject.getString("status");
                etParticipants.setText("Participants(" + jsonObject.getString("Count") + ")");
//                Toast.makeText(CompetitionGridListActivity.this, "" + message, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            loadingProgressDialog.dismiss();
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
//                    Toast.makeText(CompetitionGridListActivity.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
