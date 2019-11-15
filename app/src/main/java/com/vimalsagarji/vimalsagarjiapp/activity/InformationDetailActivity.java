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
import com.vimalsagarji.vimalsagarjiapp.model.ThisMonthComments;
import com.vimalsagarji.vimalsagarjiapp.util.CommonAPI_Name;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;
import com.vimalsagarji.vimalsagarjiapp.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by BHARAT on 04/02/2016.
 */
@SuppressWarnings("ALL")
public class InformationDetailActivity extends AppCompatActivity {

    private KProgressHUD loadingProgressDialog;
    private Sharedpreferance sharedpreferance;
    private TextView txtDate;
    private TextView txtDescri;
    private TextView txt_time;
    private static final String TAG = InformationDetailActivity.class.getSimpleName();
    private List<ThisMonthComments> listComments = new ArrayList<>();
    private Dialog dialog;
    private ListView listView;
    private SessionManager session;
    private String strUserID = "";
    private String strID = "";
    private String strIS_approved = "";
    private LinearLayout lin_like;
    private LinearLayout lin_comment;
    private ImageView img_like;
    private ImageView img_comment;
    private TextView txt_like;
    private TextView txt_comment;
    private TextView txt_nodata_today;
    private LinearLayout rl_layout;
    //    private String approve = "";
    private final ArrayList<String> listname = new ArrayList<>();
    TextView etTitle;
    String title;
    String description;
    CustomAdpter adpter;
    //check like
    private String status;
    private String message;
    private int count_comment;
    String view;
    private TextView txt_title;
    String click_action;
    int commentsize;
    private ImageView img_info;
    LinearLayout img_share;
    ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private String photo;
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
        setContentView(R.layout.content_this_month_information__sub);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sharedpreferance = new Sharedpreferance(InformationDetailActivity.this);
        rl_layout = (LinearLayout) findViewById(R.id.rl_layout);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();


        if (appLinkData != null) {
            Log.e("appLinkData", "--------------" + appLinkData.getQueryParameter("key"));

            strID = appLinkData.getQueryParameter("key");
            click_action = "information_click";
        } else {
            Intent intent1 = getIntent();
            strID = intent1.getStringExtra("listID");
            click_action = intent1.getStringExtra("click_action");
        }


        if (!sharedpreferance.getId().equalsIgnoreCase("")) {
            new checkViewed().execute();
        }


//        view = intent1.getStringExtra("view");
        Log.e("strid", "--------------------------" + strID);


        txtlocation = (TextView) findViewById(R.id.txtlocation);
        ImageView imgHomeBack = (ImageView) findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) findViewById(R.id.imgHome);
        imgHome.setVisibility(View.GONE);
        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        imgHomeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(InformationDetailActivity.this, InformationCategory.class);
//                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        img_share = (LinearLayout) findViewById(R.id.img_share);
        img_info = (ImageView) findViewById(R.id.img_info);
        etTitle = (TextView) findViewById(R.id.etTitle);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txtDescri = (TextView) findViewById(R.id.txtDescri);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Information Detail");


        //Like Comment Id
        lin_like = (LinearLayout) findViewById(R.id.lin_like);
        lin_comment = (LinearLayout) findViewById(R.id.lin_comment);
        img_like = (ImageView) findViewById(R.id.img_like);
        img_comment = (ImageView) findViewById(R.id.img_comment);
        txt_like = (TextView) findViewById(R.id.txt_like);
        txt_comment = (TextView) findViewById(R.id.txt_comment);


        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationDetailActivity.this, ImageViewActivity.class);
                intent.putExtra("imagePath", CommonURL.ImagePath + "infoimage/" + photo.replaceAll(" ", "%20"));
                startActivity(intent);
            }
        });

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n Information \n" + CommonMethod.decodeEmoji(title) + "\n\n" + CommonUrl.Main_url + "infodetail?key=" + strID + "\n\n" + getResources().getString(R.string.app_name) + "\n\n";
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
                    session = new SessionManager(getApplicationContext());
                    HashMap<String, String> user = session.getUserDetails();
                    strUserID = user.get(SessionManager.KEY_ID);
                    Log.e("user id", "------------------" + strUserID);
                    strIS_approved = user.get(SessionManager.KEY_STATUS);
                    Log.e("status", "-----------------" + strIS_approved);
                    Log.e(TAG, "IS Approved Data is" + strIS_approved);
                    Log.e("StrUserID", "--------------------------------------" + strUserID);
                    if (CommonMethod.isInternetConnected(InformationDetailActivity.this)) {
                        if (status == null) {

                        } else {
                            if (status.equalsIgnoreCase("0")) {
                                new LikeComment().execute(strID);
                                new getLikeCount().execute(strID);
                                new CheckLike().execute(sharedpreferance.getId(), strID);

                            } else {
                                Toast.makeText(getApplicationContext(), R.string.likealready, Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        Snackbar.make(v, R.string.internet, Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
        lin_comment.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View v) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {

                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {

                    dialog = new Dialog(InformationDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_bypeople_comment);
                    progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar);
                    if (CommonMethod.isInternetConnected(InformationDetailActivity.this)) {
                        new CommentList().execute(strID);
                    } else {
                        Snackbar.make(v, R.string.internet, Snackbar.LENGTH_SHORT).show();
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
                                Toast.makeText(InformationDetailActivity.this, R.string.notregister, Toast.LENGTH_SHORT).show();
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
            }
        });

    }

    //Comment List
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
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("infoid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "info/getallappcomments/?page=1&psize=1000", nameValuePairs, InformationDetailActivity.this);
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
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strInformationID = object.getString("InformationID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        Log.e("is apprived", "---------------------------------" + strIs_Approved);
                        String strUserID = object.getString("UserID");
                        Log.e("UserID", "---------------------------------" + strUserID);
                        String strDate = object.getString("Date");
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

                    commentsize = listComments.size();
                    Log.e("Comment size", "---------------------" + listComments.size());
                    count_comment = listComments.size();
                    String count = String.valueOf(count_comment);
                    txt_comment.setText(String.valueOf(commentsize));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

/*
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

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

    private class CommentList2 extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* loadingProgressDialog = KProgressHUD.create(InformationDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("infoid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "info/getallappcomments/?page=1&psize=1000", nameValuePairs, InformationDetailActivity.this);
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
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strInformationID = object.getString("InformationID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        Log.e("is apprived", "---------------------------------" + strIs_Approved);
                        String strUserID = object.getString("UserID");
                        Log.e("UserID", "---------------------------------" + strUserID);
                        String strDate = object.getString("Date");
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
                    commentsize = listComments.size();
                    Log.e("Comment size", "---------------------" + listComments.size());
                    count_comment = listComments.size();
                    String count = String.valueOf(count_comment);
                    txt_comment.setText(String.valueOf(commentsize));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            /*if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

        }
    }

    //Custom Adapter
    @SuppressWarnings("NullableProblems")
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

    //Comment Post
    private class CommentPost extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(InformationDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();


            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("infoid", strID));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Comment", params[0]));
            response = CommonMethod.postStringResponse(CommonUrl.Main_url + "info/comment/", nameValuePairs, InformationDetailActivity.this);


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
                    Toast.makeText(InformationDetailActivity.this, R.string.commentsuccess, Toast.LENGTH_SHORT).show();
                    new CommentList().execute(strID);
//                    dialog.dismiss();
                } else {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(InformationDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Like
    private class LikeComment extends AsyncTask<String, Void, String> {
        String responeJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(InformationDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("infoid", params[0]));
            responeJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "info/likeinfo/", nameValuePairs, InformationDetailActivity.this);
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

    //Count Like
    private class getLikeCount extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("infoid", params[0]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "info/countlikes/", nameValuePairs, InformationDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Check Like
    private class CheckLike extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", params[0]));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("infoid", params[1]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "info/checklike/", nameValuePairs, InformationDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                status = jsonObject.getString("status");
                message = jsonObject.getString("message");

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
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url + "countviews/information/?infoid=" + strID + "&view=" + view);
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
                Intent intent = new Intent(InformationDetailActivity.this, RegisterActivity.class);
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

    private class GetAllInfo extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(InformationDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getinfobyidforadmin + "?infoid=" + strID);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                int commentcount = jsonObject.getInt("commentcount");
                Log.e("commentcount", "-----------------" + commentcount);
//                txt_comment.setText(String.valueOf(commentcount));

                int likecount = jsonObject.getInt("likecount");
                Log.e("like", "-----------------" + likecount);
                txt_like.setText(String.valueOf(likecount));

                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        title = object.getString("Title");
                        description = object.getString("Description");
                        String address = object.getString("Address");
                        String dates = object.getString("Date");
                        view = object.getString("View");
                        photo = object.getString("Photo");

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
                        txtlocation.setText("Location: " + CommonMethod.decodeEmoji(address));

                        try {
                            if (!photo.equalsIgnoreCase("")) {
                                Glide.with(InformationDetailActivity.this).load(CommonURL.ImagePath + "infoimage/" + photo
                                        .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(img_info);

                                Log.e("info image", "---------------------" + CommonURL.ImagePath + "infoimage/" + photo.replaceAll(" ", "%20"));

                            } else {
                                img_info.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        new countView().execute();


                            /*rl_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
                                }
                            });*/

                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        if (CommonMethod.isInternetConnected(InformationDetailActivity.this)) {
            new GetAllInfo().execute();
            new CommentList2().execute(strID);
            if (sharedpreferance.getId().equalsIgnoreCase("")) {
            } else {
//                new CheckUserApprove().execute();
//            new getLikeCount().execute(strID);
                new CheckLike().execute(sharedpreferance.getId(), strID);
            }

        } else {
            Snackbar.make(rl_layout, R.string.internet, Snackbar.LENGTH_SHORT).show();
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
            nameValuePairs.add(new BasicNameValuePair("infoid", strID));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "info/setinfoviewed", nameValuePairs, InformationDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
        }


    }


}


