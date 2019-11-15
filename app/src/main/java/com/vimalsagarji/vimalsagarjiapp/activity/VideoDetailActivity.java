package com.vimalsagarji.vimalsagarjiapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.VideoFullActivity;
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

import static com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.TodayEventFragment.video_play_url;

/**
 * Created by BHARAT on 04/02/2016.
 */
@SuppressWarnings("ALL")
public class VideoDetailActivity extends AppCompatActivity {

    private KProgressHUD loadingProgressDialog;
    private TextView txt_like;
    private TextView txt_comment;
    private String status;
    private Sharedpreferance sharedpreferance;
    private List<ThisMonthVideo> listComments = new ArrayList<>();
    private final ArrayList<String> listname = new ArrayList<>();
    private Dialog dialog;
    private ListView listView;
    private LinearLayout layout_allEventSubItem;
    private String approve = "";
    private String view;
    private String vid;
    private TextView txt_title;
    String click_action;
    EditText et_event;
    TextView txtDate;
    private final String ImgURL = CommonUrl.Main_url + "static/videoimage/";
    private final String VideoPath = CommonUrl.Main_url + "static/videos/";
    String videoid;
    RelativeLayout rel_video;
    int flag = 0;
    int commentsize;
    private ProgressBar progressBar;
    private TextView txtvideolink, txt_description;
    private ImageView img_thumb;

    LinearLayout img_share;

    private String id;

//    VideoView video_view;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sharedpreferance = new Sharedpreferance(VideoDetailActivity.this);
        img_share = (LinearLayout) findViewById(R.id.img_share);

//        custom_videoplayer_standard = (JCVideoPlayerStandard) findViewById(R.id.custom_videoplayer_standard);
        et_event = (EditText) findViewById(R.id.et_event);

        txtDate = (TextView) findViewById(R.id.txtDate);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txtvideolink = (TextView) findViewById(R.id.txtvideolink);
        txt_description = (TextView) findViewById(R.id.txt_description);
        img_thumb = (ImageView) findViewById(R.id.img_thumb);


        layout_allEventSubItem = (LinearLayout) findViewById(R.id.layout_allEventSubItem);

        ImageView imgHomeBack = (ImageView) findViewById(R.id.imgarrorback);
        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        imgHomeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView imgHome = (ImageView) findViewById(R.id.imgHome);
        imgHome.setVisibility(View.GONE);
        img_search.setVisibility(View.GONE);
        imgHomeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //Like Comment Id
        LinearLayout lin_like = (LinearLayout) findViewById(R.id.lin_like);
        LinearLayout lin_comment = (LinearLayout) findViewById(R.id.lin_comment);
        ImageView img_like = (ImageView) findViewById(R.id.img_like);
        ImageView img_comment = (ImageView) findViewById(R.id.img_comment);
        txt_like = (TextView) findViewById(R.id.txt_like_event);
        txt_comment = (TextView) findViewById(R.id.txt_comment_event);
        rel_video = (RelativeLayout) findViewById(R.id.rel_video);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        if (appLinkData != null) {
            Log.e("appLinkData", "--------------" + appLinkData.getQueryParameter("key"));

            id = appLinkData.getQueryParameter("key");
            vid = appLinkData.getQueryParameter("key");
            click_action = "video_click";
        } else {

            vid = id;
            click_action = intent.getStringExtra("click_action");
        }


        Log.e("id", "------------------" + id);

        if (id.equalsIgnoreCase("eid")) {
            lin_like.setVisibility(View.GONE);
            lin_comment.setVisibility(View.GONE);
            String strVideo = intent.getExtras().getString("video");
            Log.e("videofile", "------------------" + strVideo);
            String videoname = intent.getExtras().getString("videoname");
            et_event.setText(CommonMethod.decodeEmoji(videoname));
            Log.e("videoname", "------------------" + videoname);

            String catid = intent.getExtras().getString("catid");
            Log.e("catid", "------------------" + catid);
            String photo = intent.getExtras().getString("photo");
            Log.e("photo", "------------------" + photo);
            String date = intent.getExtras().getString("date");
            Log.e("date", "------------------" + date);
            txt_title.setText("Video Detail");
            view = intent.getStringExtra("view");
            txtDate.setText(CommonMethod.decodeEmoji(date));
            video_play_url = strVideo;
//            custom_videoplayer_standard.setUp(strVideo, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, videoname);
            rel_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(VideoDetailActivity.this, VideoFullActivity.class);
                    startActivity(intent1);
                }
            });
        } else if (id.equalsIgnoreCase("bid")) {
            lin_like.setVisibility(View.GONE);
            lin_comment.setVisibility(View.GONE);
            String strVideo = intent.getExtras().getString("video");
            Log.e("videofile", "------------------" + strVideo);
            String videoname = intent.getExtras().getString("videoname");
            et_event.setText(CommonMethod.decodeEmoji(videoname));
            Log.e("videoname", "------------------" + videoname);

            String catid = intent.getExtras().getString("catid");
            Log.e("catid", "------------------" + catid);
            String photo = intent.getExtras().getString("photo");
            Log.e("photo", "------------------" + photo);
            String date = intent.getExtras().getString("date");
            Log.e("date", "------------------" + date);
            txt_title.setText("Video Detail");
            view = intent.getStringExtra("view");
            txtDate.setText(CommonMethod.decodeEmoji(date));
            video_play_url = strVideo;
            rel_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(VideoDetailActivity.this, VideoFullActivity.class);
                    startActivity(intent1);
                }
            });
//            custom_videoplayer_standard.setUp(strVideo, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, videoname);
        } else {
            if (CommonMethod.isInternetConnected(VideoDetailActivity.this)) {

//                new CheckUserApprove().execute();
//                new getLikeCount().execute(id);
//                new CheckLike().execute(sharedpreferance.getId(), id);
//                new VideoDetail().execute();
//                new CountComment().execute(id);


            } else {
                final Snackbar snackbar = Snackbar
                        .make(layout_allEventSubItem, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
        lin_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (CommonMethod.isInternetConnected(VideoDetailActivity.this)) {
                        if (status.equalsIgnoreCase("0")) {
                            new LikeComment().execute(id);
                            new getLikeCount().execute(id);
                            new CheckLike().execute(sharedpreferance.getId(), id);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.likealready, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(layout_allEventSubItem, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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

                    dialog = new Dialog(VideoDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_bypeople_comment);
                    progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar);

                    if (CommonMethod.isInternetConnected(VideoDetailActivity.this)) {
                        new CommentList().execute(vid);
                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(layout_allEventSubItem, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
                    RelativeLayout rl_layout = (RelativeLayout) dialog.findViewById(R.id.layout_byPeople);

                    dialog.show();
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    btnPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sharedpreferance.getId().equalsIgnoreCase("")) {
                                Toast.makeText(VideoDetailActivity.this, R.string.notregister, Toast.LENGTH_SHORT).show();

                            } else {
                                String strComment = etComment.getText().toString().replaceAll("%", "percent");
                                if (TextUtils.isEmpty(strComment)) {
                                    etComment.setError("Please enter your comments!");
                                    etComment.requestFocus();
                                } else {
                                    if (approve.equalsIgnoreCase("1")) {
                                        new CommentPost().execute(id, sharedpreferance.getId(), CommonMethod.encodeEmoji(strComment));
                                        etComment.setText("");
                                    } else {
//                                    Toast.makeText(VideoDetailActivity.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
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


/*
        video_view= (VideoView) findViewById(R.id.video_view);
        String testUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        video_view.setVideoPath(testUrl);*/

//
//        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
//        jzVideoPlayerStandard.setUp("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
//                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子闭眼睛");
//        jzVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");


        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n Video \n" + CommonMethod.decodeEmoji(et_event.getText().toString()) + "\n\n" + CommonUrl.Main_url + "videodetail?key=" + vid + "\n\n" + getResources().getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
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
           /* loadingProgressDialog = KProgressHUD.create(VideoDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("vid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "video/getallappcomments/?page=1&psize=1000", nameValuePairs, VideoDetailActivity.this);
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
                        String strInformationID = object.getString("VideoID");
                        String strComment = object.getString("Comment");
                        Log.e("comment", "------------------" + strComment);
                        String strIs_Approved = object.getString("Is_Approved");
                        Log.e("is apprived", "---------------------------------" + strIs_Approved);
                        String strUserID = object.getString("UserID");
                        Log.e("UserID", "---------------------------------" + strUserID);
                        String strDate = object.getString("Date");
                        String name = object.getString("Name");

                        ThisMonthVideo thisMonthVideo = new ThisMonthVideo();
                        thisMonthVideo.setID(strID);
                        thisMonthVideo.setVideoID(strInformationID);
                        thisMonthVideo.setComment(strComment);
                        thisMonthVideo.setIs_Approved(strIs_Approved);
                        thisMonthVideo.setUserID(strUserID);
                        thisMonthVideo.setDate(strDate);
                        listComments.add(thisMonthVideo);
                        listname.add(name);

                    }

                    commentsize = listComments.size();
                    txt_comment.setText(String.valueOf(commentsize));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
          /*  if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressBar.setVisibility(View.GONE);
            listView = (ListView) dialog.findViewById(R.id.listbyPeopleComment);
            TextView txt_nodata_today = (TextView) dialog.findViewById(R.id.txt_nocomment);
            if (listView != null) {
                CustomAdpter adpter = new CustomAdpter(getApplicationContext(), listComments);
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
           /* loadingProgressDialog = KProgressHUD.create(VideoDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("vid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "video/getallappcomments/?page=1&psize=1000", nameValuePairs, VideoDetailActivity.this);
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
                        String strInformationID = object.getString("VideoID");
                        String strComment = object.getString("Comment");
                        Log.e("comment", "------------------" + strComment);
                        String strIs_Approved = object.getString("Is_Approved");
                        Log.e("is apprived", "---------------------------------" + strIs_Approved);
                        String strUserID = object.getString("UserID");
                        Log.e("UserID", "---------------------------------" + strUserID);
                        String strDate = object.getString("Date");
                        String name = object.getString("Name");

                        ThisMonthVideo thisMonthVideo = new ThisMonthVideo();
                        thisMonthVideo.setID(strID);
                        thisMonthVideo.setVideoID(strInformationID);
                        thisMonthVideo.setComment(strComment);
                        thisMonthVideo.setIs_Approved(strIs_Approved);
                        thisMonthVideo.setUserID(strUserID);
                        thisMonthVideo.setDate(strDate);
                        listComments.add(thisMonthVideo);
                        listname.add(name);


                    }
                    commentsize = listComments.size();
                    txt_comment.setText(String.valueOf(commentsize));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

        }
    }

    private class CountComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(VideoDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("vid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "video/getallappcomments/?page=1&psize=1000", nameValuePairs, VideoDetailActivity.this);
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
                        String strInformationID = object.getString("VideoID");
                        String strComment = object.getString("Comment");
                        Log.e("comment", "------------------" + strComment);
                        String strIs_Approved = object.getString("Is_Approved");
                        Log.e("is apprived", "---------------------------------" + strIs_Approved);
                        String strUserID = object.getString("UserID");
                        Log.e("UserID", "---------------------------------" + strUserID);
                        String strDate = object.getString("Date");

                        ThisMonthVideo thisMonthVideo = new ThisMonthVideo();
                        thisMonthVideo.setID(strID);
                        thisMonthVideo.setVideoID(strInformationID);
                        thisMonthVideo.setComment(strComment);
                        thisMonthVideo.setIs_Approved(strIs_Approved);
                        thisMonthVideo.setUserID(strUserID);
                        thisMonthVideo.setDate(strDate);
                        listComments.add(thisMonthVideo);

                    }
                    int count = listComments.size();
                    String c = String.valueOf(count);
                    txt_comment.setText(c);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }

        }
    }

    //Custom Adapter
    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<ThisMonthVideo> {

        final List<ThisMonthVideo> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<ThisMonthVideo> items) {
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
            ThisMonthVideo monthComments = items.get(position);

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
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(VideoDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("vid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("uid", params[1]));
            nameValuePairs.add(new BasicNameValuePair("Comment", params[2]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "video/comment/", nameValuePairs, VideoDetailActivity.this);

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(VideoDetailActivity.this, R.string.commentsuccess, Toast.LENGTH_SHORT).show();
                    new CommentList().execute(vid);
//                    dialog.dismiss();
                } else {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(VideoDetailActivity.this, R.string.commentsuccess, Toast.LENGTH_SHORT).show();
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
            loadingProgressDialog = KProgressHUD.create(VideoDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("vid", params[0]));
            responeJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "video/videolike/", nameValuePairs, VideoDetailActivity.this);
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
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("vid", params[0]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "video/countlikes/", nameValuePairs, VideoDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
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
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("vid", params[1]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url + "video/checklike/", nameValuePairs, VideoDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                status = jsonObject.getString("status");
                String message = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url + "userregistration/checkuserapproveornot/?uid=" + sharedpreferance.getId());
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
//                    Toast.makeText(VideoDetailActivity.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
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
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url + "countviews/video/?vid=" + vid + "&view=" + view);
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
                Intent intent = new Intent(VideoDetailActivity.this, RegisterActivity.class);
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

    private class VideoDetail extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(VideoDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getvideobyidforadmin + "?vid=" + vid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    String commentcount = jsonObject.getString("commentcount");
                    Log.e("like", "-----------------" + commentcount);
//                    txt_comment.setText(commentcount);

                    String likecount = jsonObject.getString("likecount");
                    Log.e("like", "-----------------" + likecount);
                    txt_like.setText(likecount);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        videoid = jsonObject1.getString("ID");
                        String videonames = jsonObject1.getString("VideoName");
                        String cid = jsonObject1.getString("CategoryID");
                        final String video = jsonObject1.getString("Video").replaceAll(" ", "%20");
                        String photo = jsonObject1.getString("Photo").replaceAll(" ", "%20");
                        String duration = jsonObject1.getString("Duration");
                        String dates = jsonObject1.getString("Date");
                        String video_link = jsonObject1.getString("video_link");
                        String Description = jsonObject1.getString("Description");
                        view = jsonObject1.getString("View");


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

                        String fulldate = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year;
                        String date = fulldate;


                        String strVideo = VideoPath + video;


                        /*
                        if (video.equalsIgnoreCase("")) {
                            rel_video.setVisibility(View.GONE);
                        }else {
                            rel_video.setVisibility(View.VISIBLE);
                            Glide.with(VideoDetailActivity.this).load(CommonURL.ImagePath + "videoimage/" + photo
                                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(img_thumb);
                        }

                        */

                        Log.e("videofile", "------------------" + strVideo);
                        String videoname = videonames;

                        et_event.setText(CommonMethod.decodeEmoji(videoname));
                        Log.e("videoname", "------------------" + videoname);

                        Log.e("date", "------------------" + date);
                        txt_title.setText("Video Detail");
                        txtDate.setText(CommonMethod.decodeEmoji(date));
                        txtvideolink.setText(CommonMethod.decodeEmoji(video_link));
                        txt_description.setText(CommonMethod.decodeEmoji(Description));

                        if (!video_link.equalsIgnoreCase("")) {


                            String last11Digits = "";     //substring containing last 4 characters

                            if (video_link.length() > 11) {
                                last11Digits = video_link.substring(video_link.length() - 11);
                            } else {
                                last11Digits = video_link;
                            }
                            Log.e("11 character","-----------"+last11Digits);

                            String[] str = video_link.split("/");
//                            final String v_vode = str[3];
                            final String v_vode =last11Digits;
                            Log.e("code", "----------------" + v_vode);

                            String thumbnailMq = "https://img.youtube.com/vi/" + v_vode + "/mqdefault.jpg"; //medium quality thumbnail
                            String thumbnailHq = "https://img.youtube.com/vi/" + v_vode + "/hqdefault.jpg"; //medium quality thumbnail

                            Log.e("thumbnailMq", "----------------" + thumbnailMq);
                            Log.e("thumbnailHq", "----------------" + thumbnailHq);

                            Glide.with(VideoDetailActivity.this).load(thumbnailMq).crossFade().placeholder(R.drawable.loader).dontAnimate().into(img_thumb);

                            txtvideolink.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(VideoDetailActivity.this, YoutubePlayActivity.class);
                                    intent.putExtra("vcode", v_vode);
                                    startActivity(intent);
                                }
                            });


                            rel_video.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("video", "------------------" + video);

                                    Intent intent = new Intent(VideoDetailActivity.this, YoutubePlayActivity.class);
                                    intent.putExtra("vcode", v_vode);
                                    startActivity(intent);

                                /*
                                if (!video.equalsIgnoreCase("")) {
                                    Intent intent1 = new Intent(VideoDetailActivity.this, VideoFullActivity.class);
                                    startActivity(intent1);
                                } else {
                                    Toast.makeText(VideoDetailActivity.this, "Video not available please click on videolink.", Toast.LENGTH_SHORT).show();
                                }*/

                                }
                            });


                        }


                        video_play_url = strVideo;
                        /*rel_video.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("video", "------------------" + video);

                                Intent intent = new Intent(VideoDetailActivity.this, YoutubePlayActivity.class);
                                intent.putExtra("vcode", v_vode);
                                startActivity(intent);

                                *//*
                                if (!video.equalsIgnoreCase("")) {
                                    Intent intent1 = new Intent(VideoDetailActivity.this, VideoFullActivity.class);
                                    startActivity(intent1);
                                } else {
                                    Toast.makeText(VideoDetailActivity.this, "Video not available please click on videolink.", Toast.LENGTH_SHORT).show();
                                }*//*

                            }
                        });*/
//                        custom_videoplayer_standard.setUp(strVideo, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, videoname);
                        new countView().execute();

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
//                new CountLike().execute();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        if (vid.equalsIgnoreCase("eid")) {

        } else if (vid.equalsIgnoreCase("bid")) {

        } else {
            if (CommonMethod.isInternetConnected(VideoDetailActivity.this)) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                } else {
                    new CheckUserApprove().execute();
//                new getLikeCount().execute(id);
                    new CheckLike().execute(sharedpreferance.getId(), vid);
                    new checkViewed().execute();
                }
                new VideoDetail().execute();
                new CommentList2().execute(vid);
//                new CountComment().execute(id);


            } else {
                final Snackbar snackbar = Snackbar
                        .make(layout_allEventSubItem, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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
            nameValuePairs.add(new BasicNameValuePair("vid", vid));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "video/setvideoviewed", nameValuePairs, VideoDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
        }

    }

}


