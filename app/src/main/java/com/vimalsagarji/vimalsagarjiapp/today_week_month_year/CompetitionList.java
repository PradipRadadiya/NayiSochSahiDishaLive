package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
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
 * Created by pc on 5/26/17.
 */

public class CompetitionList extends AppCompatActivity {

    private TextView comp_name;
    private TextView partipants;
    private TextView txt_nodata_today;
    RecyclerView comp_que_list;
    String c_cid, c_cname;
    ArrayList<CompetitionQuestion> competitionQuestions = new ArrayList<>();
    KProgressHUD loadingProgressDialog;
    Sharedpreferance sharedpreferance;
    private LinearLayout lin_main;
    private String approve = "";
    private String status = "";
    LinearLayoutManager linearLayoutManager;
    TextView txt_title;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competition_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        viewID();
        sharedpreferance = new Sharedpreferance(CompetitionList.this);
        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            progressbar.setVisibility(View.GONE);

            Log.e("registed not","--------------");
//            Snackbar.make(lin_main, "Please first register after competition.", Snackbar.LENGTH_INDEFINITE).show();
            showSnackbar();
        }
        linearLayoutManager = new LinearLayoutManager(CompetitionList.this);
        comp_que_list.setLayoutManager(linearLayoutManager);
        Intent getIntent = getIntent();
        c_cname = getIntent.getStringExtra("listTitle");
        c_cid = getIntent.getStringExtra("categoryID");
        Log.e("cname", "_---------------------" + c_cname);
        Log.e("id", "_---------------------" + c_cid);
        sharedpreferance = new Sharedpreferance(CompetitionList.this);
        comp_name.setText(c_cname);
        if (CommonMethod.isInternetConnected(CompetitionList.this)) {
            new CheckUserApprove().execute();
            new CheckParticipants().execute("56", c_cid);
            Log.e("user id", "------------------------------" + sharedpreferance.getId());
//            new CatrgoryQuestion().execute(c_cid, sharedpreferance.getId());

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

    private void viewID() {
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Competition List");
        img_search.setVisibility(View.VISIBLE);
        ImageView imgBack = (ImageView) findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) findViewById(R.id.imgHome);
        lin_main = (LinearLayout) findViewById(R.id.lin_main);
        comp_name = (TextView) findViewById(R.id.comp_name);
        partipants = (TextView) findViewById(R.id.partipants);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        comp_que_list = (RecyclerView) findViewById(R.id.comp_que_list);
        imgHome.setVisibility(View.GONE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompetitionList.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private class PostData extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(CompetitionList.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
            progressbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("qid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("uid", params[1]));
                nameValuePairs.add(new BasicNameValuePair("Answer", params[2]));
                nameValuePairs.add(new BasicNameValuePair("cid", params[3]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/answer/", nameValuePairs, CompetitionList.this);
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
                Toast.makeText(CompetitionList.this, "Thank you! Your answer submitted successfully!", Toast.LENGTH_SHORT).show();
                loadingProgressDialog.dismiss();
//                progressbar.setVisibility(View.GONE);
                new CheckParticipants().execute("56", c_cid);
                Log.e("user id", "------------------------------" + sharedpreferance.getId());
                new CatrgoryQuestion().execute(c_cid, sharedpreferance.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressbar.setVisibility(View.GONE);
            loadingProgressDialog.dismiss();
        }
    }

    private class CatrgoryQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*loadingProgressDialog = KProgressHUD.create(CompetitionList.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("cid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("uid", params[1]));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/getallquestionsbycid/?page=1&psize=1000", nameValuePairs, CompetitionList.this);
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

           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            if (comp_que_list != null) {
                RecyclerCompetitionAdapter recyclerCompetitionAdapter = new RecyclerCompetitionAdapter(CompetitionList.this, competitionQuestions);
                if (recyclerCompetitionAdapter.getItemCount() != 0) {
                    Log.e("if call", "-------");
                    comp_que_list.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    comp_que_list.setAdapter(recyclerCompetitionAdapter);
                } else {
                    Log.e("if call", "-------");
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
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"competition/checkparticipants/", nameValuePairs, CompetitionList.this);
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
//                    Toast.makeText(CompetitionList.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public class RecyclerCompetitionAdapter extends RecyclerView.Adapter<RecyclerCompetitionAdapter.ViewHolder> {


        private final Activity activity;
        private final ArrayList<CompetitionQuestion> itemArrayList;
        private String id;
        Sharedpreferance sharedpreferance;


        public RecyclerCompetitionAdapter(Activity activity, ArrayList<CompetitionQuestion> itemArrayList) {
            super();
            this.activity = activity;
            this.itemArrayList = itemArrayList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.competition_item, viewGroup, false);
            sharedpreferance = new Sharedpreferance(activity);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int i) {

            holder.txtQuestion.setText(itemArrayList.get(i).getQuestion());
            String qtype = itemArrayList.get(i).getQType();
            String flag = itemArrayList.get(i).getStatus();
            if (qtype.equalsIgnoreCase("Radio")) {
                holder.edit_answer.setVisibility(View.GONE);
                holder.btnReply.setVisibility(View.GONE);

                String option = itemArrayList.get(i).getOptions();

                if (option.equalsIgnoreCase("null")) {

                } else {
                    String id = itemArrayList.get(i).getID();
                    Log.e("option", "---------------------" + option);
                    final String[] opt = option.split(",");
                    int j;
                    for (j = 0; j < opt.length; j++) {
                        Log.e("opt", "------------" + opt[j]);
                        holder.radioButton = new RadioButton(activity);
                        Log.e("id", "----------------" + holder.radioButton.getId());
                        holder.radioButton.setText(opt[j]);
                        holder.radiogroup.addView(holder.radioButton);


                        holder.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                                    progressbar.setVisibility(View.GONE);
                                    showSnackbar();
//                                    Snackbar.make(lin_main, "Please first register after competition.", Snackbar.LENGTH_LONG).show();
                                } else {
                                    if (CommonMethod.isInternetConnected(activity)) {
                                        status = "";
//                                new checkReplay().execute(items.get(position).getID(), sharedpreferance.getId());
                                        if (itemArrayList.get(i).getAnswer().equalsIgnoreCase("null")) {
                                            if (approve.equalsIgnoreCase("1")) {
                                                RadioButton rb = (RadioButton) findViewById(checkedId);
                                                String rbtext = (String) rb.getText();
                                                Log.e("checked id", "----------------" + checkedId);
                                                Log.e("Question id", "-------------------" + itemArrayList.get(i).getID());
                                                Log.e("User Id", "------------------------" + sharedpreferance.getId());
                                                Log.e("Answer", "----------------" + rbtext);
                                                Log.e("Category Id", "--------------------" + c_cid);
                                                new PostData().execute(itemArrayList.get(i).getID(), sharedpreferance.getId(), rbtext, c_cid);
                                                status = "";
                                            } else {
//                                            Toast.makeText(activity, "You are not approved user.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            holder.radiogroup.setEnabled(false);
                                            Toast.makeText(activity, "User has already replied.", Toast.LENGTH_SHORT).show();
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
                            }
                        });

                    }
                }

            } else {

                if (flag.equalsIgnoreCase("1")) {
                    holder.btnReply.setVisibility(View.GONE);
                    final String qid = itemArrayList.get(i).getID();
                    holder.edit_answer.setVisibility(View.VISIBLE);
                    holder.edit_answer.setEnabled(false);
                    holder.edit_answer.setText("Thank you! You have already reply it!");

                } else {
                    holder.btnReply.setVisibility(View.VISIBLE);
                    final String qid = itemArrayList.get(i).getID();
                    holder.edit_answer.setVisibility(View.VISIBLE);
                    holder.edit_answer.setEnabled(true);
//                    holder.edit_answer.requestFocus();
                    holder.btnReply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                                showSnackbar();
//                                Snackbar.make(lin_main, "Please first register after competition.", Snackbar.LENGTH_LONG).show();
                            } else {

                                if (CommonMethod.isInternetConnected(CompetitionList.this)) {
                                    status = "";
//                            new checkReplay().execute(qid,sharedpreferance.getId());
                                    if (TextUtils.isEmpty(holder.edit_answer.getText().toString())) {
                                        holder.edit_answer.setError("Please enter your answer!");
                                        holder.edit_answer.requestFocus();

                                    } else {

                                        if (itemArrayList.get(i).getAnswer().equalsIgnoreCase("null")) {
                                            if (approve.equalsIgnoreCase("1")) {
                                                new PostData().execute(qid, sharedpreferance.getId(), holder.edit_answer.getText().toString(), c_cid);
                                            } else {
                                                Toast.makeText(CompetitionList.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(CompetitionList.this, "User has already replied.", Toast.LENGTH_SHORT).show();
                                            holder.btnReply.setVisibility(View.GONE);
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
                        }
                    });

                }

            }


        }

        @Override
        public int getItemCount() {

            return itemArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txtQuestion;
            EditText edit_answer;
            Button btnReply;
            RadioGroup radiogroup;
            RadioButton radioButton;

            public ViewHolder(View itemView) {
                super(itemView);

                txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
                edit_answer = (EditText) itemView.findViewById(R.id.edit_answer);
                btnReply = (Button) itemView.findViewById(R.id.btnReply);
                radiogroup = (RadioGroup) itemView.findViewById(R.id.radiogroup);
            }

            @Override
            public void onClick(View v) {

            }

        }


    }


    //Method to show the snackbar
    @SuppressLint("NewApi")
    private void showSnackbar() {
        //Creating snackbar
        Snackbar snackbar = Snackbar.make(lin_main, "Please register to proceed!", Snackbar.LENGTH_INDEFINITE);
        Toast.makeText(this, R.string.notregister, Toast.LENGTH_LONG).show();

        //Adding action to snackbar
        snackbar.setAction("Register", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Displaying another snackbar when user click the action for first snackbar
//                Snackbar s = Snackbar.make(v, "Register", Snackbar.LENGTH_LONG);
//                s.show();
                Intent intent = new Intent(CompetitionList.this, RegisterActivity.class);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...


        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            progressbar.setVisibility(View.GONE);

            Log.e("registed not","--------------");
//            Snackbar.make(lin_main, "Please first register after competition.", Snackbar.LENGTH_INDEFINITE).show();
            showSnackbar();
        }
        else {
            Log.e("registed","--------------");
            if (CommonMethod.isInternetConnected(CompetitionList.this)) {
                new CatrgoryQuestion().execute(c_cid, sharedpreferance.getId());
            } else {
                Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
