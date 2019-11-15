package com.vimalsagarji.vimalsagarjiapp.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.vimalsagarji.vimalsagarjiapp.fcm.MyFirebaseMessagingService.questionid;


@SuppressWarnings("ALL")
public class AllQuestionDetail extends AppCompatActivity {
    ImageView imgEvent;
    private ImageView imgBack;
    private ImageView imgHome;
    private TextView txt_question;
    private TextView txt_answer;
    private String view;
    private String qid;
    private String que;
    private String ans;

    private KProgressHUD loadingProgressDialog;
    Sharedpreferance sharedpreferance;
    private LinearLayout img_share;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail);
        sharedpreferance = new Sharedpreferance(AllQuestionDetail.this);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        Intent intent = getIntent();
        if (appLinkData != null) {
            Log.e("appLinkData", "--------------" + appLinkData.getQueryParameter("key"));
            questionid = appLinkData.getQueryParameter("key");

        } else {
            qid = intent.getStringExtra("qid");
        }

        txt_question = (TextView) findViewById(R.id.txt_question);
        txt_answer = (TextView) findViewById(R.id.txt_answer);
        imgBack = (ImageView) findViewById(R.id.imgarrorback);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        img_share = (LinearLayout) findViewById(R.id.img_share);
        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        imgHome.setVisibility(View.GONE);

        TextView txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Question Detail");

        Log.e("Question", "-----------------------" + intent.getStringExtra("Question"));
        Log.e("Answer", "-------------------------" + intent.getStringExtra("Answer"));

//        String que = intent.getStringExtra("Question");
//        String ans = intent.getStringExtra("Answer");
//        view = intent.getStringExtra("view");


        Log.e("uid", "--------------" + sharedpreferance.getId());
        if (!sharedpreferance.getId().equalsIgnoreCase("")) {
            new checkViewed().execute();
        }


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intentBack=new Intent(getApplicationContext(),QuestionAnswerActivity.class);
//                startActivity(intentBack);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intentHome);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n Q & A \n" + CommonMethod.decodeEmoji(que) + "\n\n" +CommonUrl.Main_url+"questiondetail?key="+questionid+ "\n\n" + getResources().getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private class countView extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"countviews/question/?qid=" + questionid + "&view=" + view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------" + s);
        }

    }

    private class viewQuestionAnswer extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(AllQuestionDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Downloading data")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
//            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"questionanswer/viewquesansbyid/?qid=" + questionid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------" + s);
            loadingProgressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        jsonObject1.getString("ID");
                        que = jsonObject1.getString("Question");
                        ans = jsonObject1.getString("Answer");
                        jsonObject1.getString("Date");
                        jsonObject1.getString("Is_Approved");
                        jsonObject1.getString("UserID");
                        view = jsonObject1.getString("View");

                        String answer="Answer : "+ CommonMethod.decodeEmoji(ans);
                        SpannableStringBuilder spannableStringBuilder=new SpannableStringBuilder(answer);
                        spannableStringBuilder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        txt_answer.setText(spannableStringBuilder );
                        txt_question.setText("Question : " + CommonMethod.decodeEmoji(que));

                        if (CommonMethod.isInternetConnected(AllQuestionDetail.this)) {
                            new countView().execute();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    private class checkViewed extends AsyncTask<String, Void, String> {

        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new BasicNameValuePair("qid", questionid));
            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "questionanswer/setqaviewed", nameValuePairs, AllQuestionDetail.this);
            return responseJson;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        new viewQuestionAnswer().execute();

    }

}
