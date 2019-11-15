package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

/**
 * Created by Grapes-Pradip on 3/16/2017.
 */

@SuppressWarnings("ALL")
public class CompetitionListQuestion extends AppCompatActivity {
    private String categoryID;
    private ListView comp_que_list;
    private TextView comp_name;
    private TextView partipants;
    private Sharedpreferance sharedpreferance;
    private String approve = "";
    private KProgressHUD loadingProgressDialog;
    private TextView txt_nodata_today;
    private ArrayList<CompetitionQuestion> competitionQuestions = new ArrayList<>();
    @SuppressWarnings("unused")
    private String status = "";
    private LinearLayout lin_main;
    private TextView txt_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comp_que_list);
        sharedpreferance = new Sharedpreferance(CompetitionListQuestion.this);
        findID();
        Intent getIntent = getIntent();
        String strCometitionIntentTitle = getIntent.getStringExtra("listTitle");
        categoryID = getIntent.getStringExtra("categoryID");
        txt_title.setText(strCometitionIntentTitle);
        comp_name.setText(strCometitionIntentTitle);

        if (CommonMethod.isInternetConnected(CompetitionListQuestion.this)) {
            new CheckUserApprove().execute();
            new CheckParticipants().execute("56", categoryID);
            Log.e("user id", "------------------------------" + sharedpreferance.getId());
            new CatrgoryQuestion().execute(categoryID, sharedpreferance.getId());
        } else {
            final Snackbar snackbar = Snackbar
                    .make(lin_main, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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

    private void findID() {
        lin_main = (LinearLayout) findViewById(R.id.lin_main);
        comp_que_list = (ListView) findViewById(R.id.comp_que_list);
        ImageView imgBack = (ImageView) findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) findViewById(R.id.imgHome);
        txt_title = (TextView) findViewById(R.id.txt_title);
        comp_name = (TextView) findViewById(R.id.comp_name);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        partipants = (TextView) findViewById(R.id.partipants);
        imgHome.setVisibility(View.GONE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
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
//                    Toast.makeText(CompetitionListQuestion.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class PostData extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(CompetitionListQuestion.this)
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
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/answer/", nameValuePairs, CompetitionListQuestion.this);
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
                Toast.makeText(CompetitionListQuestion.this, "" + msg, Toast.LENGTH_SHORT).show();
                loadingProgressDialog.dismiss();
                new CheckParticipants().execute("56", categoryID);
                Log.e("user id", "------------------------------" + sharedpreferance.getId());
                new CatrgoryQuestion().execute(categoryID, sharedpreferance.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            loadingProgressDialog.dismiss();
        }
    }

    private class CatrgoryQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(CompetitionListQuestion.this)
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
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/getallquestionsbycid/?page=1&psize=1000", nameValuePairs, CompetitionListQuestion.this);
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
                int c_index = 0;
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    competitionQuestions = new ArrayList<>();
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
                        String status = object.getString("status");


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
                        competitionQuestion.setStatus(status);
                        competitionQuestions.add(competitionQuestion);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
            if (comp_que_list != null) {
                CompetitionAdapter customAdpter = new CompetitionAdapter(getApplicationContext(), competitionQuestions);
                if (customAdpter.getCount() != 0) {
                    comp_que_list.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    comp_que_list.setAdapter(customAdpter);
                } else {
                    txt_nodata_today.setVisibility(View.VISIBLE);
                    comp_que_list.setVisibility(View.GONE);
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
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/checkparticipants/", nameValuePairs, CompetitionListQuestion.this);
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
                partipants.setText("Participants(" + jsonObject.getString("Count") + ")");
//                Toast.makeText(CompetitionGridListActivity.this, "" + message, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            loadingProgressDialog.dismiss();
        }
    }

    @SuppressWarnings("NullableProblems")
    private class CompetitionAdapter extends ArrayAdapter<CompetitionQuestion> {
        final ArrayList<CompetitionQuestion> items;
        final Context context;
        final int resource;

        public CompetitionAdapter(Context context, ArrayList<CompetitionQuestion> items) {
            super(context, R.layout.comp_questionlist, items);
            this.context = context;
            this.resource = R.layout.comp_questionlist;
            this.items = items;
        }

        @SuppressLint("ViewHolder")
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null, false);
            holder.txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
            holder.btn_Reply = (Button) convertView.findViewById(R.id.btnReply);
            holder.edit_answer = (EditText) convertView.findViewById(R.id.edit_answer);
            holder.radiogroup = (RadioGroup) convertView.findViewById(R.id.radiogroup);
//            holder.edit_answer.setEnabled(true);
//            holder.edit_answer.requestFocus();
            Log.e("question", "------------" + items.get(position).getQuestion());
            holder.txtQuestion.setText(items.get(position).getQuestion());

            String qtype = items.get(position).getQType();
            String flag=items.get(position).getStatus();

            if (qtype.equalsIgnoreCase("Radio")) {
                holder.edit_answer.setVisibility(View.GONE);
                holder.btn_Reply.setVisibility(View.GONE);

                String option = items.get(position).getOptions();

                if (option.equalsIgnoreCase("null")) {

                } else {
                    String id = items.get(position).getID();
                    Log.e("option", "---------------------" + option);
                    final String[] opt = option.split(",");
                    int i;
                    for (i = 0; i < opt.length; i++) {
                        Log.e("opt", "------------" + opt[i]);
                        holder.radioButton = new RadioButton(CompetitionListQuestion.this);
                        Log.e("id", "----------------" + holder.radioButton.getId());
                        holder.radioButton.setText(opt[i]);
                        holder.radiogroup.addView(holder.radioButton);
                        holder.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (CommonMethod.isInternetConnected(CompetitionListQuestion.this)) {
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
                                            new PostData().execute(items.get(position).getID(), sharedpreferance.getId(), rbtext, categoryID);
                                            status = "";
                                        } else {
                                            Toast.makeText(CompetitionListQuestion.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        holder.radiogroup.setEnabled(false);
                                        Toast.makeText(CompetitionListQuestion.this, "User has already replied.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    final Snackbar snackbar = Snackbar
                                            .make(lin_main, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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

            } else {

                if (flag.equalsIgnoreCase("1")) {
                    holder.btn_Reply.setVisibility(View.GONE);
                    final String qid = items.get(position).getID();
                    holder.edit_answer.setVisibility(View.VISIBLE);
                    holder.edit_answer.setEnabled(false);

                }else {
                    holder.btn_Reply.setVisibility(View.VISIBLE);
                    final String qid = items.get(position).getID();
                    holder.edit_answer.setVisibility(View.VISIBLE);
                    holder.edit_answer.setEnabled(true);
//                    holder.edit_answer.requestFocus();
                    holder.btn_Reply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (CommonMethod.isInternetConnected(CompetitionListQuestion.this)) {
                                status = "";
//                            new checkReplay().execute(qid,sharedpreferance.getId());
                                if (TextUtils.isEmpty(holder.edit_answer.getText().toString())) {
                                    holder.edit_answer.setError("Give answer after reply");
                                    holder.edit_answer.requestFocus();

                                } else {

                                    if (items.get(position).getAnswer().equalsIgnoreCase("null")) {
                                        if (approve.equalsIgnoreCase("1")) {
                                            new PostData().execute(qid, sharedpreferance.getId(), holder.edit_answer.getText().toString(), categoryID);
                                        } else {
                                            Toast.makeText(CompetitionListQuestion.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(CompetitionListQuestion.this, "User has already replied.", Toast.LENGTH_SHORT).show();
                                        holder.btn_Reply.setVisibility(View.GONE);
                                    }

                                }
                            } else {
                                final Snackbar snackbar = Snackbar
                                        .make(lin_main, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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


            convertView.setTag(holder);
            return convertView;
        }
    }

    private class ViewHolder {
        TextView txtQuestion;
        EditText edit_answer;
        Button btn_Reply;
        RadioGroup radiogroup;
        RadioButton radioButton;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
