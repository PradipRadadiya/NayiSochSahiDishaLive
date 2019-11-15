package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.ActivityHomeMain;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.RecyclerCompetitionQuestionAllAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestion;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestionItem;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;


public class CompetitionQuestionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerCompetitionQuestionAllAdapter recyclerCompetitionCategoryAdapter;
    private ProgressBar progress_load;
    private ArrayList<CompetitionQuestionItem> competitionItemArrayList = new ArrayList<>();
    private TextView txt_nodata;
    private TextView txt_timer;
    private String cid, minute;

    //Timer
    private CountDownTimer countDownTimer;          // built in android class CountDownTimer

    private long totalTimeCountInMilliseconds;      // total count down time in milliseconds

    private long timeBlinkInMilliseconds;

    private Button btn_submit;
    Sharedpreferance sharedpreferance;


    ArrayList<String> qidlist = new ArrayList<>();
    ArrayList<String> answerlist = new ArrayList<>();


    private String status = "error";
    private String starttime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_questionall);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sharedpreferance = new Sharedpreferance(CompetitionQuestionActivity.this);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        minute = intent.getStringExtra("minute");
        starttime = intent.getStringExtra("starttime");
        String endtime = intent.getStringExtra("endtime");
        bindID();

        Calendar calender = Calendar.getInstance();
        calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

        @SuppressLint("SimpleDateFormat")
        String formattedDate = new SimpleDateFormat("HH:mm").format(calender.getTime());
//                String formattedDate = calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE);
        Log.e("start time", "----------------" + starttime);
        Log.e("current time", "----------------" + formattedDate);

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
            Date startDate = format.parse(starttime);
            Date endDate = format.parse(formattedDate);
            long mills = startDate.getTime() - endDate.getTime();
            Log.e("Data1", "" + startDate.getTime());
            Log.e("Data2", "" + endDate.getTime());
            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;

            String diff = hours + ":" + mins; // updated value every1 second

            Log.e("difference", "-------------" + diff);
            Log.e("difference", "-------------" + Math.abs(Integer.parseInt(minute) - mins));

            int totalMinute = Integer.parseInt(minute) - Math.abs(mins);

            minute = String.valueOf(totalMinute);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        new GetAllCompetitionQuestion().execute();
        new UserBlockExam().execute();
        int time = Integer.parseInt(minute) * 60;
        totalTimeCountInMilliseconds = time * 1000;      // time count for 3 minutes = 180 seconds
        timeBlinkInMilliseconds = 30 * 1000;

        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                txt_timer.setText("Time Left: " + String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60) + " minutes");
                // format the textview to show the easily readable format
            }

            @Override
            public void onFinish() {
                // this function will be called when the timecount is finished

                if (status.equalsIgnoreCase("success")) {

                } else {
                    txt_timer.setText("Time up!");
                    txt_timer.setVisibility(View.GONE);
                    qidlist = new ArrayList<>();
                    answerlist = new ArrayList<>();

                    for (int i = 0; i < recyclerCompetitionCategoryAdapter.questionarr.length; i++) {
                        Log.e("qid", "-------------" + recyclerCompetitionCategoryAdapter.questionarr[i]);
                        Log.e("anser", "-------------" + recyclerCompetitionCategoryAdapter.answerarr[i]);
                    }

                    for (String s : recyclerCompetitionCategoryAdapter.questionarr)
                        if (s != null && s.length() > 0)
                            qidlist.add(s);


                    for (String s : recyclerCompetitionCategoryAdapter.answerarr)
                        if (s != null && s.length() > 0)
                            answerlist.add(s);

                    Log.e("qid list", "----------------------" + qidlist);
                    Log.e("answer list ", "----------------------" + answerlist);

                    new PostQuestionData().execute();

                }

            }

        }.start();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qidlist = new ArrayList<>();
                answerlist = new ArrayList<>();

                for (int i = 0; i < recyclerCompetitionCategoryAdapter.questionarr.length; i++) {
                    Log.e("qid", "-------------" + recyclerCompetitionCategoryAdapter.questionarr[i]);
                    Log.e("anser", "-------------" + recyclerCompetitionCategoryAdapter.answerarr[i]);
                }

                for (String s : recyclerCompetitionCategoryAdapter.questionarr)
                    if (s != null && s.length() > 0)
                        qidlist.add(s);


                for (String s : recyclerCompetitionCategoryAdapter.answerarr)
                    if (s != null && s.length() > 0)
                        answerlist.add(s);

                Log.e("qid list", "----------------------" + qidlist);
                Log.e("answer list ", "----------------------" + answerlist);


               /* AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(CompetitionQuestionActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(CompetitionQuestionActivity.this);
                }
                builder.setTitle("Alert")
                        .setMessage("Are you sure you submit this competition?")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                                new PostQuestionData().execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/


                final Dialog dialog = new Dialog(CompetitionQuestionActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_confirm);

                Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new PostQuestionData().execute();
                    }
                });


                ImageView img_close_popup = (ImageView) dialog.findViewById(R.id.img_close_popup);
                img_close_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
                TextView txt_description = (TextView) dialog.findViewById(R.id.txt_description);
                TextView txt_date = (TextView) dialog.findViewById(R.id.txt_date);
                TextView txt_time = (TextView) dialog.findViewById(R.id.txt_time);

//                    txt_title.setText(CommonMethod.decodeEmoji(title));
//                    txt_description.setText(CommonMethod.decodeEmoji(description));
//
//                    String[] datesarray = CommonMethod.decodeEmoji(date).split("-");
//
//                    txt_date.setText(CommonMethod.decodeEmoji(datesarray[2] + "-" + datesarray[1] + "-" + datesarray[0]));
//
//                    txt_time.setText(CommonMethod.decodeEmoji(time));

                dialog.show();
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



              /*  Log.e("qidlist","---------"+recyclerCompetitionCategoryAdapter.qidlist);
                if (competitionItemArrayList.size() == recyclerCompetitionCategoryAdapter.qidlist.size()) {
                    Log.e("qid", "----------------------" + recyclerCompetitionCategoryAdapter.qidlist);
                    Log.e("answerlist", "----------------------" + recyclerCompetitionCategoryAdapter.answerlist);

                    new PostQuestionData().execute();

                } else {
                    Toast.makeText(CompetitionQuestionActivity.this, "Please fill all the question", Toast.LENGTH_SHORT).show();
                }*/
              
            }
        });

    }

    private void bindID() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CompetitionQuestionActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_competition);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_nodata = (TextView) findViewById(R.id.txt_nodata);
        txt_timer = (TextView) findViewById(R.id.txt_timer);
        recyclerView.setLayoutManager(linearLayoutManager);
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
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", cid));

            responseJSON = CommonMethod.postStringResponse(CommonURL.Main_url + "competition/getquestionbycid", nameValuePairs, CompetitionQuestionActivity.this);

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);

                    /*countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
                        // 500 means, onTick function will be called at every 500 milliseconds

                        @Override
                        public void onTick(long leftTimeInMilliseconds) {
                            long seconds = leftTimeInMilliseconds / 1000;
                            txt_timer.setText("Time Left: " + String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                            // format the textview to show the easily readable format
                        }

                        @Override
                        public void onFinish() {
                            // this function will be called when the timecount is finished

                            if (status.equalsIgnoreCase("success")) {

                            } else {

                                txt_timer.setText("Time up!");
                                txt_timer.setVisibility(View.GONE);
                                new PostQuestionData().execute();
                            }


                        }

                    }.start();
*/

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e("id", "---------------" + id);
                        String question = jsonObject1.getString("question");
                        String answer = jsonObject1.getString("answer");

                        String questiontype = jsonObject1.getString("qtype");
                        String competition_id = jsonObject1.getString("competition_id");

//                        String qtype = null;
//                        if (questiontype.equalsIgnoreCase("Radio")){
//                            qtype="Option";
//                        }else {
//                            qtype = jsonObject1.getString("QType");
//                        }

                        String categoryid = jsonObject1.getString("answer");
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("options");
                        ArrayList<String> optionArrayList = new ArrayList<>();
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            optionArrayList.add(jsonArray1.getString(j));
                            Log.e("option", "---------------------" + optionArrayList);
                        }

                        String listString = "";

                        for (String str : optionArrayList) {
                            listString += str + "|";
                        }

                        competitionItemArrayList.add(new CompetitionQuestionItem(id, question, questiontype, competition_id, answer, listString, false, -1));

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }


            progress_load.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (recyclerView != null) {
                recyclerCompetitionCategoryAdapter = new RecyclerCompetitionQuestionAllAdapter(CompetitionQuestionActivity.this, competitionItemArrayList);
                if (recyclerCompetitionCategoryAdapter.getItemCount() != 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    txt_nodata.setVisibility(View.GONE);
                    recyclerView.setAdapter(recyclerCompetitionCategoryAdapter);
                } else {
                    txt_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PostQuestionData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CompetitionQuestionActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
                nameValuePairs.add(new BasicNameValuePair("cid", cid));

                for (int i = 0; i < qidlist.size(); i++) {

                    nameValuePairs.add(new BasicNameValuePair("qid[" + i + "]", qidlist.get(i)));
                    nameValuePairs.add(new BasicNameValuePair("Answer[" + i + "]", answerlist.get(i)));

                }

                nameValuePairs.add(new BasicNameValuePair("time", "10:15 am"));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "competition/answer/", nameValuePairs, CompetitionQuestionActivity.this);




            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("respone", "--------------------" + s);

//            Toast.makeText(CompetitionQuestionActivity.this, "Thank you! Your answer submitted successfully!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                jsonObject.getString("status");
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

/*
                    AlertDialog.Builder builder = new AlertDialog.Builder(CompetitionQuestionActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("Thank you!\nYour answer submitted successfully!\nResult will declare after one day.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    status = "success";
                                    dialog.dismiss();
                                    countDownTimer.onFinish();
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();*/


                    final Dialog dialog = new Dialog(CompetitionQuestionActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alert_dialog);

                    Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            status = "success";
                            dialog.dismiss();
                            countDownTimer.cancel();
                            Intent intent = new Intent(CompetitionQuestionActivity.this, ActivityHomeMain.class);
                            startActivity(intent);
                            finishAffinity();

//                            button_start.setEnabled(false);
                        }
                    });


                    TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
                    TextView txt_description = (TextView) dialog.findViewById(R.id.txt_description);
                    TextView txt_date = (TextView) dialog.findViewById(R.id.txt_date);
                    TextView txt_time = (TextView) dialog.findViewById(R.id.txt_time);

//                    txt_title.setText(CommonMethod.decodeEmoji(title));
                    txt_description.setText("Your answer submitted successfully.");
//
//                    String[] datesarray = CommonMethod.decodeEmoji(date).split("-");
//
//                    txt_date.setText(CommonMethod.decodeEmoji(datesarray[2] + "-" + datesarray[1] + "-" + datesarray[0]));
//
//                    txt_time.setText(CommonMethod.decodeEmoji(time));

                    dialog.show();
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


//                    Toast.makeText(CompetitionQuestionActivity.this, "Thank you! Your answer submitted successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CompetitionQuestionActivity.this, "Sorry! Your answer not submitted.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CompetitionQuestionActivity.this, ActivityHomeMain.class);
                    startActivity(intent);
                    finishAffinity();
                }

//                String msg = jsonObject.getString("message");
//                jsonObject.getString("Answer");

//                progressbar.setVisibility(View.GONE);


            } catch (JSONException e) {
                e.printStackTrace();
            }

//            progressbar.setVisibility(View.GONE);

        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(CompetitionQuestionActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(CompetitionQuestionActivity.this);
        }
        builder.setTitle("Alert")
                .setMessage("Are you sure you want to exit this competition?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @SuppressLint("StaticFieldLeak")
    private class UserBlockExam extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", cid));
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "competition/setcompetitionviewed", nameValuePairs, CompetitionQuestionActivity.this);
            return responseJson;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
