package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerCompetitionQuestionCorrectWrongAnswerAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestionCorrectWrongItem;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class ParticularUserResultDetail extends AppCompatActivity {


    private ProgressBar progress_load;
    RecyclerView recycleview_comp_result;
    String cid, uid;
    LinearLayoutManager linearLayoutManager;
    RecyclerCompetitionQuestionCorrectWrongAnswerAdapter recyclerCompetitionQuestionCorrectWrongAnswerAdapter;
    ArrayList<CompetitionQuestionCorrectWrongItem> competitionQuestionCorrectWrongItems = new ArrayList<>();
    private TextView txt_get_mark;
    private TextView txt_total_marks;
    private LinearLayout lin_mark;
    private String description;
    private TextView txt_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_user_result_detail);
        linearLayoutManager = new LinearLayoutManager(ParticularUserResultDetail.this);

        toolbarClick();

        cid = getIntent().getStringExtra("competition_id");
        uid = getIntent().getStringExtra("user_id");
        description = getIntent().getStringExtra("description");

        progress_load =  findViewById(R.id.progress_load);
        recycleview_comp_result =  findViewById(R.id.recycleview_comp_result);
        recycleview_comp_result.setLayoutManager(linearLayoutManager);

        Log.e("cid", "-------------------" + cid);
        Log.e("uid", "-------------------" + uid);

        new GetAllCompetitionQuestion().execute();

    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_description =  findViewById(R.id.txt_description);
        txt_description.setEnabled(true);
        txt_get_mark =  findViewById(R.id.txt_get_mark);
        txt_total_marks =  findViewById(R.id.txt_total_marks);
        txt_title =  findViewById(R.id.txt_title);
        imgarrorback =  findViewById(R.id.imgarrorback);
        img_search =  findViewById(R.id.img_search);
        lin_mark =  findViewById(R.id.lin_mark);
        txt_title.setText("Results");
        img_search.setVisibility(View.VISIBLE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticularUserResultDetail.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        txt_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ParticularUserResultDetail.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.question_description);
                ImageView img_close =  dialog.findViewById(R.id.img_close_popup);

                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                TextView txt_description =  dialog.findViewById(R.id.txt_description);
                txt_description.setText(CommonMethod.decodeEmoji(description));
                dialog.show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(dialog.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class GetAllCompetitionQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(AllCompetionQuestionActivity.this);
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recycleview_comp_result.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", cid));
            nameValuePairs.add(new BasicNameValuePair("uid", uid));
            responseJSON = CommonMethod.postStringResponse(CommonURL.Main_url + "competition/getcompetitionresultbyuser", nameValuePairs, ParticularUserResultDetail.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    txt_description.setEnabled(false);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);


                    if (jsonObject.getString("total_mark").equalsIgnoreCase("null")) {

                        lin_mark.setVisibility(View.GONE);

                    }

                    txt_total_marks.setText(String.valueOf(jsonArray.length()));
                    txt_get_mark.setText(jsonObject.getString("total_mark"));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String question = jsonObject1.getString("question");
                        String qid = jsonObject1.getString("qid");
                        Log.e("question", "---------------" + question);

                        String true_answer = jsonObject1.getString("true_answer");
                        String competition_id = jsonObject1.getString("competition_id");
                        String Name = jsonObject1.getString("Name");

                        String user_id = jsonObject1.getString("user_id");
                        Log.e("userid", "-------------" + user_id);

                        String answer;
                        if (jsonObject1.getString("answer").isEmpty()) {
                            answer = "";
                            Log.e("answer", "-------------" + answer);
                        } else {
                            answer = jsonObject1.getString("answer");
                            Log.e("answer", "-------------" + answer);
                        }

                        JSONArray jsonArray1 = jsonObject1.getJSONArray("options");

                        ArrayList<String> optionArrayList = new ArrayList<>();

                        for (int j = 0; j < jsonArray1.length(); j++) {

                            JSONObject object = jsonArray1.getJSONObject(j);

                            optionArrayList.add(object.getString("optionvalue"));
                            Log.e("option", "---------------------" + optionArrayList);
                        }

                        String listString = "";

                        for (String str : optionArrayList) {
                            listString += str + "|";
                        }

                        competitionQuestionCorrectWrongItems.add(new CompetitionQuestionCorrectWrongItem(question, qid, true_answer, competition_id, Name, user_id, answer, listString));

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progress_load.setVisibility(View.GONE);
            recycleview_comp_result.setVisibility(View.VISIBLE);
            if (recycleview_comp_result != null) {
                recyclerCompetitionQuestionCorrectWrongAnswerAdapter = new RecyclerCompetitionQuestionCorrectWrongAnswerAdapter(ParticularUserResultDetail.this, competitionQuestionCorrectWrongItems);
                if (recyclerCompetitionQuestionCorrectWrongAnswerAdapter.getItemCount() != 0) {
                    recycleview_comp_result.setVisibility(View.VISIBLE);
//                    txt_nodata.setVisibility(View.GONE);
                    recycleview_comp_result.setAdapter(recyclerCompetitionQuestionCorrectWrongAnswerAdapter);
                } else {
//                    txt_nodata.setVisibility(View.VISIBLE);
                    recycleview_comp_result.setVisibility(View.GONE);


                    AlertDialog.Builder builder = new AlertDialog.Builder(ParticularUserResultDetail.this);
                    builder.setMessage("Sorry! No results found.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    dialog.dismiss();
                                    finish();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            }


        }
    }


}
