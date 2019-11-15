package com.vimalsagarji.vimalsagarjiapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.ImageViewActivity;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.ThisMonthVideo;
import com.vimalsagarji.vimalsagarjiapp.util.CommonAPI_Name;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by BHARAT on 04/02/2016.
 */
@SuppressWarnings("ALL")
public class ThoughtsDetailActivity extends AppCompatActivity {


    private String photo;

    public ThoughtsDetailActivity() {

    }

    private TextView txtDate;
    private TextView txtDescri;
    private static final String TAG = ThoughtsDetailActivity.class.getSimpleName();
    private Button btnLike;
    private Button btnComment;
    private Button btnLikeClick;
    private final ArrayList<String> listID = new ArrayList<String>();
    private final ArrayList<String> listComment = new ArrayList<String>();
    private final ArrayList<String> listThoughtID = new ArrayList<String>();
    private final ArrayList<String> listIs_Approved = new ArrayList<String>();
    private ArrayList<String> listUserID = new ArrayList<String>();
    private final ArrayList<String> listUserName = new ArrayList<String>();
    private static final String URL = CommonUrl.Main_url+"thought/getallappcomments/?page=1&psize=1000";
    private static final String PostCommentURL = CommonUrl.Main_url+"thought/comment/";
    private static final String strLikeURL = CommonUrl.Main_url+"thought/likethought/";
    ProgressDialog pd;
    String strResult;
    int countLike = 1;
    String strLikeResult = "";
    private String tid;
    private String like_status = "";
    private String approve = "";


    private KProgressHUD loadingProgressDialog;
    private EditText et_event;
    private TextView txt_like, txt_comment, txt_nodata_today;
    private String status, message;
    private Sharedpreferance sharedpreferance;
    private List<ThisMonthVideo> listComments = new ArrayList<>();
    private Dialog dialog;
    private ListView listView;
    private RelativeLayout rl_layout;
    private String view;
    private TextView txt_title;
    EditText etTitle;
    String click_action;
    int commentsize;
    private LinearLayout img_share;
    String title;
    String description;

    private ProgressBar progressBar;
    private ImageView img_thought;
    private TextView txtlocation;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_allthought_subactivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sharedpreferance = new Sharedpreferance(ThoughtsDetailActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_allthoughtsubactivity);
        setSupportActionBar(toolbar);

        img_thought = (ImageView) findViewById(R.id.img_thought);
        txtlocation = (TextView) findViewById(R.id.txtlocation);
        ImageView imgHomeBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
        imgHome.setVisibility(View.GONE);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        imgHomeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        img_thought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThoughtsDetailActivity.this, ImageViewActivity.class);
                intent.putExtra("imagePath", CommonURL.ImagePath + "thoughtimage/" + photo.replaceAll(" ", "%20"));
                startActivity(intent);
            }
        });


        btnLike = (Button) findViewById(R.id.btnLike);
        btnComment = (Button) findViewById(R.id.btnComment);
        btnLikeClick = (Button) findViewById(R.id.btnLikeClick);


        etTitle = (EditText) findViewById(R.id.etTitle);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDescri = (TextView) findViewById(R.id.txtDescri);

        img_share = (LinearLayout) findViewById(R.id.img_share);
        LinearLayout lin_like = (LinearLayout) findViewById(R.id.lin_like);
        LinearLayout lin_comment = (LinearLayout) findViewById(R.id.lin_comment);
        ImageView img_like = (ImageView) findViewById(R.id.img_like);
        ImageView img_comment = (ImageView) findViewById(R.id.img_comment);
        txt_like = (TextView) findViewById(R.id.txt_like_event);
        txt_comment = (TextView) findViewById(R.id.txt_comment_event);
        txt_title = (TextView) findViewById(R.id.txt_title);



        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();


        Intent intent1 = getIntent();


        if (appLinkData != null) {
            Log.e("appLinkData", "--------------" + appLinkData.getQueryParameter("key"));

            tid = appLinkData.getQueryParameter("key");
            click_action="information_click";
        } else {
            tid = intent1.getStringExtra("thoughtid");
            click_action = intent1.getStringExtra("click_action");
            Log.e("tid", "-------------" + tid);
            Log.e("uid", "--------------" + sharedpreferance.getId());

        }










        txt_title.setText("Thought Detail");

//        if (CommonMethod.isInternetConnected(ThoughtsDetailActivity.this)) {
//            new GetThoughtDetail().execute();
//            new CheckUserApprove().execute();
//            CheckLikedUser checkLikedUser = new CheckLikedUser();
//            checkLikedUser.execute();
////            new CheckCountComment().execute();
////            new CheckCountLikes().execute();
//        } else {
//            final Snackbar snackbar = Snackbar
//                    .make(rl_layout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
//            snackbar.setActionTextColor(Color.RED);
//            snackbar.show();
//            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    snackbar.dismiss();
//                }
//            });
//        }

        if (!sharedpreferance.getId().equalsIgnoreCase("")) {
            new checkViewed().execute();
        }
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n Thought \n" + CommonMethod.decodeEmoji(title) + "\n\n"  +CommonUrl.Main_url+"thoughtdetail?key="+tid+"\n\n" +getResources().getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        lin_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (CommonMethod.isInternetConnected(ThoughtsDetailActivity.this)) {
                        try {
                            if (status.equalsIgnoreCase("0")) {
                                new LikeComment().execute(tid);
                                new CheckCountLikes().execute();
                                new CheckCountComment().execute();
                                new CheckLikedUser().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.likealready, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(rl_layout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
        lin_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {

                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {

                    dialog = new Dialog(ThoughtsDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_bypeople_comment);
                    progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar);

                    if (CommonMethod.isInternetConnected(ThoughtsDetailActivity.this)) {
                        new CommentList().execute(tid);
                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(rl_layout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                        snackbar.setAction("Dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
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
                    txt_nodata_today = (TextView) dialog.findViewById(R.id.txt_nodata_today);
                    RelativeLayout rl_layout = (RelativeLayout) dialog.findViewById(R.id.layout_byPeople);
                    dialog.show();
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    btnPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                                Toast.makeText(ThoughtsDetailActivity.this, R.string.notregister, Toast.LENGTH_SHORT).show();

                            } else {
                                String strComment = etComment.getText().toString().replaceAll("%","percent");
                                if (TextUtils.isEmpty(strComment)) {
                                    etComment.setError("Please enter your comments!");
                                    etComment.requestFocus();
                                } else {
                                    if (approve.equalsIgnoreCase("1")) {
                                        new CommentPost().execute(CommonMethod.encodeEmoji(strComment));
                                        etComment.setText("");
                                    } else {
//                                    Toast.makeText(ThoughtsDetailActivity.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
                                    }
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
            }
        });

    }


    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<String> {

        final List<String> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<String> items) {
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
            // holder.txt_ID.setText(items.get(position));
            if (listUserName.get(position).equalsIgnoreCase("null")) {
                holder.txtCommentUserName.setText("Admin");
                holder.txtCommentDescription.setText(CommonMethod.decodeEmoji(listComment.get(position)));
            } else {
                holder.txtCommentUserName.setText(CommonMethod.decodeEmoji(listUserName.get(position)));
                holder.txtCommentDescription.setText(CommonMethod.decodeEmoji(listComment.get(position)));
            }
            return convertView;
        }

        private class ViewHolder {
            TextView txtCommentUserName, txtCommentDescription;

        }


    }

    private class CommentPost extends AsyncTask<String, Void, String> {
        String resposejson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ThoughtsDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("tid", tid));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Comment", params[0]));
            resposejson = CommonMethod.postStringResponse(PostCommentURL, nameValuePairs, ThoughtsDetailActivity.this);

            return resposejson;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(ThoughtsDetailActivity.this, R.string.commentsuccess, Toast.LENGTH_SHORT).show();
                    new CommentList().execute(tid);
//                    dialog.dismiss();
                } else {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(ThoughtsDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class CheckLikedUser extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("tid", tid));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"thought/checklike/", nameValuePairs, ThoughtsDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response check like", "--------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                like_status = jsonObject.getString("status");
                Log.e("like_status", "------------------" + like_status);
                status = like_status;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class CheckCountLikes extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("tid", tid));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"thought/countlikes/", nameValuePairs, ThoughtsDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response check like", "--------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("count");
                Log.e("count like", "------------------" + jsonArray.getString(0));
                String count = jsonArray.getString(0);
                if (count.equalsIgnoreCase("")) {
                    txt_like.setText("0");
                } else {
                    txt_like.setText(count);
                }
//                String count=jsonObject.getString("count");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class CheckCountComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("tid", tid));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"thought/countcomments/", nameValuePairs, ThoughtsDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response check like", "--------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("count");
                Log.e("count comment", "------------------" + jsonArray.getString(0));
                String count = jsonArray.getString(0);
                if (count.equalsIgnoreCase("")) {
                    txt_comment.setText("0");
                } else {
                    txt_comment.setText(count);
                }
//                String count=jsonObject.getString("count");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class LikeComment extends AsyncTask<String, Void, String> {
        String responeJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ThoughtsDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("tid", params[0]));
            responeJSON = CommonMethod.postStringResponse(strLikeURL, nameValuePairs, ThoughtsDetailActivity.this);
            return responeJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------------------" + s);
            status = "1";
            loadingProgressDialog.dismiss();
        }
    }

    //Comment List
    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   loadingProgressDialog = KProgressHUD.create(ThoughtsDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("tid", params[0]));
            responseJSON = CommonMethod.postStringResponse(URL, nameValuePairs, ThoughtsDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    listUserID = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String strID = object.getString("ID");
                        String strThoughtID = object.getString("ThoughtID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        String strUserID = object.getString("UserID");
                        String name = object.getString("Name");
                        listID.add(strID);
                        Log.d(TAG, "list Id data:" + listID);
                        listThoughtID.add(strThoughtID);
                        Log.d(TAG, "list Title data:" + listThoughtID);
                        listComment.add(strComment);
                        Log.d(TAG, "list Description data:" + listComment);
                        listIs_Approved.add(strIs_Approved);
                        Log.d(TAG, "list Address data:" + listIs_Approved);
                        listUserID.add(strUserID);
                        Log.d(TAG, "list Date data:" + listUserID);
                        listUserName.add(name);

                    }
                    commentsize = listUserID.size();
                    txt_comment.setText(String.valueOf(commentsize));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressBar.setVisibility(View.GONE);
            listView = (ListView) dialog.findViewById(R.id.listbyPeopleComment);
            txt_nodata_today = (TextView) dialog.findViewById(R.id.txt_nocomment);
            if (listView != null) {
                CustomAdpter adpter = new CustomAdpter(getApplicationContext(), listUserID);
                listView.setAdapter(adpter);
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

    private class CommentList2 extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  loadingProgressDialog = KProgressHUD.create(ThoughtsDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("tid", params[0]));
            responseJSON = CommonMethod.postStringResponse(URL, nameValuePairs, ThoughtsDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    listUserID = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String strID = object.getString("ID");
                        String strThoughtID = object.getString("ThoughtID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        String strUserID = object.getString("UserID");
                        String name = object.getString("Name");
                        listID.add(strID);
                        Log.d(TAG, "list Id data:" + listID);
                        listThoughtID.add(strThoughtID);
                        Log.d(TAG, "list Title data:" + listThoughtID);
                        listComment.add(strComment);
                        Log.d(TAG, "list Description data:" + listComment);
                        listIs_Approved.add(strIs_Approved);
                        Log.d(TAG, "list Address data:" + listIs_Approved);
                        listUserID.add(strUserID);
                        Log.d(TAG, "list Date data:" + listUserID);
                        listUserName.add(name);
                        commentsize = listComment.size();
                        txt_comment.setText(String.valueOf(commentsize));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

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
//                    Toast.makeText(ThoughtsDetailActivity.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"countviews/thought/?tid=" + tid + "&view=" + view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------" + s);

//            new CommentList2().execute(tid);
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
                Intent intent = new Intent(ThoughtsDetailActivity.this, RegisterActivity.class);
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

    private class GetThoughtDetail extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ThoughtsDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getthoughtbyidforadmin + "?tid=" + tid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();

                    int commentcount = jsonObject.getInt("commentcount");
                    Log.e("comment detail", "-----------------" + commentcount);
//                    txt_comment.setText(String.valueOf(commentcount));

                    int likecount = jsonObject.getInt("likecount");
                    Log.e("like detail", "-----------------" + likecount);
                    txt_like.setText(String.valueOf(likecount));

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        title = object.getString("Title");
                        description = object.getString("Description");
                        String dates = object.getString("Date");
                        view = object.getString("View");
                        photo = object.getString("Photo");
                        String location=object.getString("location");
                        String[] string = dates.split(" ");
                        Log.e("str1", "--------" + string[0]);
                        Log.e("str2", "--------" + string[1]);

                        Date dt = CommonMethod.convert_date(dates);
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
                        String date = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year;


                        etTitle.setText(CommonMethod.decodeEmoji(title));
                        txtDate.setText(CommonMethod.decodeEmoji(date));
                        txtDescri.setText(CommonMethod.decodeEmoji(description));
                        Log.e("location","------------"+location);
                        txtlocation.setText("Location: "+CommonMethod.decodeEmoji(location));

                        if (!photo.equalsIgnoreCase("")) {
                            Glide.with(ThoughtsDetailActivity.this).load(CommonURL.ImagePath + "thoughtimage/" + photo
                                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(img_thought);
                        }else{
                            img_thought.setVisibility(View.GONE);
                        }

                        new countView().execute();

                    }

                }
                if (loadingProgressDialog != null) {
                    loadingProgressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                loadingProgressDialog.dismiss();
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
            nameValuePairs.add(new BasicNameValuePair("tid", tid));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "thought/setthoughtviewed", nameValuePairs, ThoughtsDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response","-----------------"+s);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        if (CommonMethod.isInternetConnected(ThoughtsDetailActivity.this)) {
            new GetThoughtDetail().execute();
            new CommentList2().execute(tid);
            if (sharedpreferance.getId().equalsIgnoreCase("")) {
            } else {
                new CheckUserApprove().execute();
                CheckLikedUser checkLikedUser = new CheckLikedUser();
                checkLikedUser.execute();
            }
//            new CheckCountComment().execute();
//            new CheckCountLikes().execute();
        } else {
            final Snackbar snackbar = Snackbar
                    .make(rl_layout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
