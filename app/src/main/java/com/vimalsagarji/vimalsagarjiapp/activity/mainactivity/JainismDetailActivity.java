package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.vimalsagarji.vimalsagarjiapp.activity.InformationDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.jcplayer.JcPlayerView;
import com.vimalsagarji.vimalsagarjiapp.model.JainismItem;
import com.vimalsagarji.vimalsagarjiapp.model.ThisMonthComments;
import com.vimalsagarji.vimalsagarjiapp.model.ThisMonthVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class JainismDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_content, txt_date, txt_titles, txt_views, txt_like, txt_comment;
    private LinearLayout lin_like, lin_comment, lin_share;
    JainismItem jainismItem;
    Sharedpreferance sharedpreferance;
    private String likeStatus = "0";
    JcPlayerView jcplayerview_audio;
    private Dialog dialog;
    private ProgressBar progressBar;
    private KProgressHUD loadingProgressDialog;
    private List<ThisMonthComments> listComments = new ArrayList<>();
    private ListView listView;
    private TextView txt_nodata_today;
    CustomAdpter adpter;
    private ArrayList<String> listname = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jainism_detail);
        sharedpreferance = new Sharedpreferance(JainismDetailActivity.this);
        bindToolbar();
        bindId();
        bindClick();

        jainismItem = (JainismItem) getIntent().getSerializableExtra("itemList");
        txt_titles.setText(CommonMethod.decodeEmoji(jainismItem.getTitle()));
        txt_content.setText(CommonMethod.decodeEmoji(jainismItem.getDescription()));
        txt_like.setText(CommonMethod.decodeEmoji(jainismItem.getCountLike()));
        txt_comment.setText(CommonMethod.decodeEmoji(jainismItem.getCountComment()));
        txt_date.setText(CommonMethod.decodeEmoji(jainismItem.getDate()));
        txt_views.setText(CommonMethod.decodeEmoji(jainismItem.getCountViews() + " Views"));

        if (jainismItem.getAudio().equalsIgnoreCase("")) {
            jcplayerview_audio.setVisibility(View.GONE);
        } else {
            Log.e("audio", "-----------------" + CommonUrl.Main_url + "static/jainaismaudio" + jainismItem.getAudio().replaceAll(" ", "%20"));
            try {
                jcplayerview_audio.playAudio(CommonUrl.Main_url + "static/jainaismaudio/" + jainismItem.getAudio().replaceAll(" ", "%20"), "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (sharedpreferance.getId().equalsIgnoreCase("")) {

        } else {
            new userSetViewsCount().execute();
            new userCheckLike().execute();
        }

    }

    private void bindClick() {
        lin_like.setOnClickListener(this);
        lin_comment.setOnClickListener(this);
        lin_share.setOnClickListener(this);
    }

    private void bindId() {
        txt_titles = findViewById(R.id.txt_title_jainism);
        txt_content = findViewById(R.id.txt_content);
        txt_date = findViewById(R.id.txt_date);
        txt_views = findViewById(R.id.txt_views);
        txt_like = findViewById(R.id.txt_like);
        txt_comment = findViewById(R.id.txt_comment);
        lin_like = findViewById(R.id.lin_like);
        lin_comment = findViewById(R.id.lin_comment);
        lin_share = findViewById(R.id.lin_share);
        jcplayerview_audio = findViewById(R.id.jcplayerview_audio);
    }

    private void bindToolbar() {
        TextView txt_title = findViewById(R.id.txt_title);
        ImageView img_search = findViewById(R.id.img_search);
        ImageView imgarrorback = findViewById(R.id.imgarrorback);
        txt_title.setText("Jainism Detail");
        img_search.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    //Method to show the snackbar
    @SuppressLint("NewApi")
    private void showSnackbar(View v,int str) {
        //Creating snackbar
        Snackbar snackbar = Snackbar.make(v, str, Snackbar.LENGTH_LONG);

        //Adding action to snackbar
        snackbar.setAction("Register", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Displaying another snackbar when user click the action for first snackbar
//                Snackbar s = Snackbar.make(v, "Register", Snackbar.LENGTH_LONG);
//                s.show();
                Intent intent = new Intent(JainismDetailActivity.this, RegisterActivity.class);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_like:
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(view,R.string.notregister);
                } else {
                    if (likeStatus.equalsIgnoreCase("0")) {
                        likeStatus = "1";
                        new userLikePost().execute();
                    } else {
                        Toast.makeText(this, R.string.likealready, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.lin_comment:

                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(view,R.string.notregister);
                } else {
                    dialog = new Dialog(JainismDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_bypeople_comment);
                    progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar);
                    if (CommonMethod.isInternetConnected(JainismDetailActivity.this)) {
                        new CommentList().execute(jainismItem.getId());
                    } else {
                        Snackbar.make(view, R.string.internet, Snackbar.LENGTH_SHORT).show();
                    }

                    ImageView imgback1 = (ImageView) dialog.findViewById(R.id.imgback);
                    imgback1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });



                    Button btnPost = (Button) dialog.findViewById(R.id.btnPost);
                    final EditText etComment = (EditText) dialog.findViewById(R.id.etComment);
                    RelativeLayout rl_layout = (RelativeLayout) dialog.findViewById(R.id.layout_byPeople);

                    dialog.show();
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    btnPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                                Toast.makeText(JainismDetailActivity.this, R.string.notregister, Toast.LENGTH_SHORT).show();
                            } else {
                                String strComment = etComment.getText().toString();
                                if (TextUtils.isEmpty(strComment)) {
                                    etComment.setError("Please enter your comments!");
                                    etComment.requestFocus();
                                } else {
                                    new CommentPost().execute(CommonMethod.encodeEmoji(etComment.getText().toString().replaceAll("%", "percent")));
                                    etComment.setText("");

                                }
                            }
                        }
                    });

                    rl_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
                        }
                    });
                }

                break;
            case R.id.lin_share:
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n Jainism \n" + CommonMethod.decodeEmoji(jainismItem.getTitle()) + "\n\n" + CommonUrl.Main_url + "jainismdetail?key=1" + "\n\n" + getResources().getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class userCheckLike extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("jid", jainismItem.getId()));
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "jainaism/checklike", nameValuePairs, JainismDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("0")) {
                    likeStatus = "0";
                } else {
                    likeStatus = "1";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class userLikePost extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(JainismDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("jid", jainismItem.getId()));
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "jainaism/jainismlike", nameValuePairs, JainismDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            loadingProgressDialog.dismiss();
            likeStatus = "1";
            int count = 0;
            count = Integer.parseInt(jainismItem.getCountLike());
            count += 1;
            txt_like.setText(String.valueOf(count));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class userSetViewsCount extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("jid", jainismItem.getId()));
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "jainaism/setjainaismviewed", nameValuePairs, JainismDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
        }


    }

    //Comment Post
    @SuppressLint("StaticFieldLeak")
    private class CommentPost extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(JainismDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();


            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("jid", jainismItem.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Comment", params[0]));
            response = CommonMethod.postStringResponse(CommonUrl.Main_url + "jainaism/comment/", nameValuePairs, JainismDetailActivity.this);


            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(JainismDetailActivity.this, R.string.commentsuccess, Toast.LENGTH_SHORT).show();
                    new CommentList().execute(jainismItem.getId());
//                    dialog.dismiss();
                } else {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(JainismDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Comment List
    @SuppressLint("StaticFieldLeak")
    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* loadingProgressDialog = KProgressHUD.create(InformationDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("jid", params[0]));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("page", "1"));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("psize", "1000"));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "jainaism/getallappcomments/", nameValuePairs, JainismDetailActivity.this);

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    listComments = new ArrayList<>();
                    listname = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strInformationID = object.getString("JainismID");
                        String strComment = object.getString("Comment");
                        String strDate = object.getString("Date");
                        String strIs_Approved = object.getString("Is_Approved");
                        Log.e("is apprived", "---------------------------------" + strIs_Approved);
                        String strUserID = object.getString("UserID");
                        Log.e("UserID", "---------------------------------" + strUserID);
                        String name = object.getString("Name");

                        ThisMonthComments thisMonthComments = new ThisMonthComments();
                        thisMonthComments.setID(strID);
                        thisMonthComments.setInformationID(strInformationID);
                        thisMonthComments.setComment(strComment);
                        thisMonthComments.setIs_Approved(strIs_Approved);
                        thisMonthComments.setUserID(strUserID);
                        thisMonthComments.setDate(strDate);
                        listComments.add(thisMonthComments);
                        listname.add(name);
                    }

                    txt_comment.setText(String.valueOf(listComments.size()));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.GONE);
            listView = (ListView) dialog.findViewById(R.id.listbyPeopleComment);
            txt_nodata_today = (TextView) dialog.findViewById(R.id.txt_nocomment);
            if (listView != null) {
                adpter = new CustomAdpter(getApplicationContext(), listComments);
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

    public class CustomAdpter extends ArrayAdapter<ThisMonthComments> {

        final List<ThisMonthComments> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<ThisMonthComments> items) {
            super(context, R.layout.custom_allthought_dialog_listitem, items);
            this.context = context;
            this.resource = R.layout.custom_allthought_dialog_listitem;
            this.items = items;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.txtCommentUserName = (TextView) convertView.findViewById(R.id.txtCommentUserName);
                holder.txtCommentDescription = (TextView) convertView.findViewById(R.id.txtCommentDescription);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ThisMonthComments monthComments = items.get(position);

            if (listname.get(position).equalsIgnoreCase("null")) {
                holder.txtCommentUserName.setText("Admin");
                holder.txtCommentDescription.setText(CommonMethod.decodeEmoji(monthComments.getComment()));
            } else {
                holder.txtCommentUserName.setText(CommonMethod.decodeEmoji(listname.get(position)));
                holder.txtCommentDescription.setText(CommonMethod.decodeEmoji(monthComments.getComment()));
            }
            return convertView;
        }

        private class ViewHolder {
            TextView txtCommentUserName, txtCommentDescription;

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class countLikes extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("jid", jainismItem.getId()));
            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "jainaism/countlikes", nameValuePairs, JainismDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("total like", "-----------" + jsonArray.get(0));
                    int totalLike = (int) jsonArray.get(0);
                    txt_like.setText(String.valueOf(totalLike));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class countView extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("jid", jainismItem.getId()));
                nameValuePairs.add(new BasicNameValuePair("view", jainismItem.getCountViews()));
                responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "countviews/jainaism/", nameValuePairs, JainismDetailActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            jcplayerview_audio.kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new countLikes().execute();
    }

}
