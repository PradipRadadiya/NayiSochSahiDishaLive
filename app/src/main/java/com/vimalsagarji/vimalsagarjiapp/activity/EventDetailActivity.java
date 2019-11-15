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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.vimalsagarji.vimalsagarjiapp.ImageItemSplash;
import com.vimalsagarji.vimalsagarjiapp.ImageViewActivity;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.adpter.AudioListAdapter;
import com.vimalsagarji.vimalsagarjiapp.adpter.PhotoAudioVideoAdapter;
import com.vimalsagarji.vimalsagarjiapp.adpter.VideoListAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.jcplayer.JcPlayerView;
import com.vimalsagarji.vimalsagarjiapp.model.EventComment;
import com.vimalsagarji.vimalsagarjiapp.model.PhotoAudioVideoItem;
import com.vimalsagarji.vimalsagarjiapp.util.CommonAPI_Name;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;
import com.vimalsagarji.vimalsagarjiapp.utils.Constant;
import com.vimalsagarji.vimalsagarjiapp.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.vimalsagarji.vimalsagarjiapp.today_week_month_year.GalleryCategory.itemSplashArrayList;

/**
 * Created by BHARAT on 23/02/2016.
 */
@SuppressWarnings("ALL")
public class EventDetailActivity extends AppCompatActivity {

    private KProgressHUD loadingProgressDialog;
    private String strEventId = "";
    private String strImage = "";
    private String strUserID = "";
    private String strIS_approved = "";
    private String discription = "";
    private String Audio = "";
    private String Video = "";
    private String Photo = "";
    private ImageView imgEvent;
    private ImageView imgBack;
    private ImageView imgHome;
    private Dialog dialog;
    private TextView txtDate;
    private TextView txtAddress;
    private SessionManager session;
    private static final String TAG = EventDetailActivity.class.getSimpleName();
    private static final String sessiondata = "0";
    private static String strCommmentResult = "";
    private TextView txt_like_event;
    private TextView txt_comment_event;
    private ListView listView;
    private List<EventComment> listComments = new ArrayList<>();
    private final ArrayList<String> listname = new ArrayList<>();
    private TextView event_dis;
    //    private ProgressDialog progressDialog;
    private Sharedpreferance sharedpreferance;
    private LinearLayout lin_like;
    private LinearLayout lin_comment;
    private ImageView img_like;
    private ImageView img_comment;
    private TextView txt_like;
    private TextView txt_comment;
    private TextView txt_nodata_today;
    private String status;
    private String message;
    //    private JCVideoPlayerStandard videoView;
    private JcPlayerView jcplayer;
    private ImageView img_photo;
    private ImageView img_audio;
    private ImageView img_video;
    private String approve = "";
    private LinearLayout layout_allEventSubItem;
    private String view;
    private TextView txt_title;
    private EditText et_event;

    private final String Photopath = CommonUrl.Main_url+"static/eventimage/";
    private final String Audiopath = CommonUrl.Main_url+"static/eventaudio/";
    private final String Videopath = CommonUrl.Main_url+"static/eventvideo/";

    String listtitle;
    String strDate;
    String strAddress;

    int endaudio;
    int endvideo;
    int endimage;

    String audiosubstring;
    String videosubstring;
    String photosubstring;
    String click_action;
    RelativeLayout rel_video;
    TextView txtvideolink;

    LinearLayoutManager linearLayoutManager;
    ArrayList<PhotoAudioVideoItem> photoAudioVideoItems;
    String[] photos;
    String[] audios;
    String[] videos;

    ArrayList<String> photolist = new ArrayList<>();
    ArrayList<String> audiolist = new ArrayList<>();
    ArrayList<String> videolist = new ArrayList<>();

    PhotoAudioVideoAdapter photoAudioVideoAdapter;
    AudioListAdapter audioListAdapter;
    VideoListAdapter videoListAdapter;

    private RecyclerView recycleview_slide;
    TextView txt_nodata;
    int commentsize;
    private LinearLayout img_share;
    String title;
    String description;
    private ProgressBar progressBar;
    private ImageView img_plays;
    private String videoLink;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_event_subitem);
        itemSplashArrayList.clear();
        linearLayoutManager = new LinearLayoutManager(EventDetailActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        recycleview_slide = (RecyclerView) findViewById(R.id.recycleview_slide);
        txt_nodata = (TextView) findViewById(R.id.txt_nodata);
        recycleview_slide.setLayoutManager(linearLayoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_all_event_subitem);
        setSupportActionBar(toolbar);

        sharedpreferance = new Sharedpreferance(EventDetailActivity.this);
        session = new SessionManager(getApplicationContext());
        imgBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        findID();
        final Intent intent = getIntent();
//        String listtitle = intent.getStringExtra("listtitle");
//        String strDate = intent.getStringExtra("listDate");
//        String strAddress = intent.getStringExtra("listAddress");


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        if (appLinkData != null) {
            Log.e("appLinkData", "--------------" + appLinkData.getQueryParameter("key"));

            strEventId = appLinkData.getQueryParameter("key");
            click_action="event_click";
        } else {

            strEventId = intent.getStringExtra("listID");
            Log.e("eid", "--------------" + strEventId);
            click_action = intent.getStringExtra("click_action");
        }



        txt_title.setText("Event Detail");
    //First load
        if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
            if (sharedpreferance.getId().equalsIgnoreCase("")) {
            } else {
                new checkViewed().execute();
                new CheckUserApprove().execute();
    //            new EventDetail().execute();
                new CheckLike().execute(sharedpreferance.getId(), strEventId);
            }
            new GetEventDetail().execute();
            new CommentList2().execute(strEventId);

        } else {
            Toast.makeText(getApplicationContext(), "Internet not connected.", Toast.LENGTH_SHORT).show();
        }

        //end

        img_share = (LinearLayout) findViewById(R.id.img_share);

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n Event \n" + CommonMethod.decodeEmoji(title) + "\n\n"+CommonUrl.Main_url+"eventdetail?key="+strEventId+"\n\n"  + getResources().getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhotos();
            }
        });
        img_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAudios();

            }
        });

        img_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVideos();

            }
        });

        txt_comment_event = (TextView) findViewById(R.id.txt_comment_event);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgHome.setVisibility(View.GONE);

        lin_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                        if (status.equalsIgnoreCase("0")) {
//                            new CheckLike().execute(sharedpreferance.getId(), strEventId);
                            new LikeComment().execute(strEventId);
//                            new getLikeCount().execute(strEventId);

//                            new EventDetail().execute();

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
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View v) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {

                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {

                    dialog = new Dialog(EventDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_bypeople_comment);
                    progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar);
                    if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                        new CommentList().execute(strEventId);
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
                                Toast.makeText(EventDetailActivity.this, R.string.notregister, Toast.LENGTH_SHORT).show();

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
//                                    Toast.makeText(EventDetailActivity.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
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

    private void setVideos() {
        img_photo.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.back));
        img_audio.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.back));
        img_video.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.kprogresshud_grey_color));

        photoAudioVideoItems = new ArrayList<>();
        for (int i = 0; i < videolist.size(); i++) {
            Log.e("audio split", "------------------" + videolist.get(i));
            photoAudioVideoItems.add(new PhotoAudioVideoItem(videolist.get(i), "Video", "Video"));

        }

        if (recycleview_slide != null) {
            videoListAdapter = new VideoListAdapter(EventDetailActivity.this, photoAudioVideoItems);
            if (videoListAdapter.getItemCount() != 0) {
                recycleview_slide.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);
                recycleview_slide.setAdapter(videoListAdapter);

            } else {

                if (!videoLink.equalsIgnoreCase("")){
                    rel_video.setVisibility(View.VISIBLE);
                    String[] str = videoLink.split("/");
                    final String v_vode = str[3];
                    Log.e("code", "----------------" + v_vode);

                    String thumbnailMq = "https://img.youtube.com/vi/"+v_vode+"/mqdefault.jpg"; //medium quality thumbnail
                    String thumbnailHq = "https://img.youtube.com/vi/"+v_vode+"/hqdefault.jpg"; //medium quality thumbnail

                    Log.e("thumbnailMq", "----------------" + thumbnailMq);
                    Log.e("thumbnailHq", "----------------" + thumbnailHq);

                    Glide.with(EventDetailActivity.this).load(thumbnailMq).crossFade().placeholder(R.drawable.loader).dontAnimate().into(img_plays);

                    img_plays.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(EventDetailActivity.this, YoutubePlayActivity.class);
                            intent.putExtra("vcode", v_vode);
                            startActivity(intent);
                        }
                    });
                }

                recycleview_slide.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                txt_nodata.setText("No Videos\n Available.");
            }

        }

    }

    private void setAudios() {
        rel_video.setVisibility(View.GONE);
        img_photo.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.back));
        img_audio.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.kprogresshud_grey_color));
        img_video.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.back));

        photoAudioVideoItems = new ArrayList<>();
        for (int i = 0; i < audiolist.size(); i++) {
            Log.e("audio split", "------------------" + audiolist.get(i));
            photoAudioVideoItems.add(new PhotoAudioVideoItem(audiolist.get(i), "Audio", "Audio"));
        }
        if (recycleview_slide != null) {
            audioListAdapter = new AudioListAdapter(EventDetailActivity.this, photoAudioVideoItems);
            if (audioListAdapter.getItemCount() != 0) {
                recycleview_slide.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);
                recycleview_slide.setAdapter(audioListAdapter);

            } else {
                recycleview_slide.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                txt_nodata.setText("No Audio\n Available.");
            }
        }


    }

    private void setPhotos() {
        rel_video.setVisibility(View.GONE);
        img_photo.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.kprogresshud_grey_color));
        img_audio.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.back));
        img_video.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.back));

        if (recycleview_slide != null) {
            photoAudioVideoAdapter = new PhotoAudioVideoAdapter(EventDetailActivity.this, photolist);
            if (photoAudioVideoAdapter.getItemCount() != 0) {
                recycleview_slide.setVisibility(View.VISIBLE);
                txt_nodata.setVisibility(View.GONE);
                recycleview_slide.setAdapter(photoAudioVideoAdapter);

            } else {
                recycleview_slide.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                txt_nodata.setText("No Images\n Available.");
            }
        }

        photoAudioVideoItems = new ArrayList<PhotoAudioVideoItem>();
        photoAudioVideoAdapter = new PhotoAudioVideoAdapter(EventDetailActivity.this, photolist);
        recycleview_slide.setAdapter(photoAudioVideoAdapter);
    }

    private void findID() {
        rel_video = (RelativeLayout) findViewById(R.id.rel_video);
        txtvideolink = (TextView) findViewById(R.id.txtvideolink);
        txt_title = (TextView) findViewById(R.id.txt_title);
        event_dis = (TextView) findViewById(R.id.event_dis);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        imgEvent = (ImageView) findViewById(R.id.imgEvent);
        txt_like_event = (TextView) findViewById(R.id.txt_like_event);
        lin_like = (LinearLayout) findViewById(R.id.lin_like);
        lin_comment = (LinearLayout) findViewById(R.id.lin_comment);
        img_like = (ImageView) findViewById(R.id.img_like);
        img_comment = (ImageView) findViewById(R.id.img_comment);
        txt_like = (TextView) findViewById(R.id.txt_like);
        txt_comment = (TextView) findViewById(R.id.txt_comment);
//        videoView = (JCVideoPlayerStandard) findViewById(R.id.video_event);
        jcplayer = (JcPlayerView) findViewById(R.id.jcplayer);
        et_event = (EditText) findViewById(R.id.et_event);

        img_photo = (ImageView) findViewById(R.id.img_photo);
        img_audio = (ImageView) findViewById(R.id.img_audio);
        img_video = (ImageView) findViewById(R.id.img_video_icon);
        layout_allEventSubItem = (LinearLayout) findViewById(R.id.layout_allEventSubItem);
        img_plays=findViewById(R.id.img_plays);
    }

    //Comment List
    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*loadingProgressDialog = KProgressHUD.create(EventDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("eid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"event/getallcomments/?page=1&psize=1000", nameValuePairs, EventDetailActivity.this);
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
                        String strInformationID = object.getString("EventID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        String strUserID = object.getString("UserID");
                        String strDate = object.getString("Date");
                        String name = object.getString("Name");

                        EventComment eventComment = new EventComment();
                        eventComment.setID(strID);
                        eventComment.setEventID(strInformationID);
                        eventComment.setComment(strComment);
                        eventComment.setIs_Approved(strIs_Approved);
                        eventComment.setUserID(strUserID);
                        eventComment.setDate(strDate);
                        listComments.add(eventComment);
                        listname.add(name);

                    }
                    commentsize = listComments.size();
                    txt_comment_event.setText(String.valueOf(commentsize));


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
          /*  loadingProgressDialog = KProgressHUD.create(EventDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("eid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"event/getallcomments/?page=1&psize=1000", nameValuePairs, EventDetailActivity.this);
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
                        String strInformationID = object.getString("EventID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        String strUserID = object.getString("UserID");
                        String strDate = object.getString("Date");
                        String name = object.getString("Name");

                        EventComment eventComment = new EventComment();
                        eventComment.setID(strID);
                        eventComment.setEventID(strInformationID);
                        eventComment.setComment(strComment);
                        eventComment.setIs_Approved(strIs_Approved);
                        eventComment.setUserID(strUserID);
                        eventComment.setDate(strDate);
                        listComments.add(eventComment);
                        listname.add(name);


                    }
                    commentsize = listComments.size();
                    txt_comment_event.setText(String.valueOf(commentsize));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

/*
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

        }
    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<EventComment> {

        final List<EventComment> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<EventComment> items) {
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
            EventComment comment = items.get(position);
            if (listname.get(position).equalsIgnoreCase("null")) {
                holder.txtCommentUserName.setText("Admin");
                holder.txtCommentDescription.setText(CommonMethod.decodeEmoji(comment.getComment()));
            } else {
                holder.txtCommentUserName.setText(listname.get(position));
                holder.txtCommentDescription.setText(CommonMethod.decodeEmoji(comment.getComment()));
            }
            return convertView;
        }

        private class ViewHolder {
            TextView txtCommentUserName, txtCommentDescription;

        }


    }

    private class CommentPost extends AsyncTask<String, Void, String> {
        String responsejson = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(EventDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait");
            loadingProgressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("eid", strEventId));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Comment", params[0]));
            String postCommentURL = Constant.POST_COMMENT_URL;
            responsejson = CommonMethod.postStringResponse(postCommentURL, nameValuePairs, EventDetailActivity.this);

            return responsejson;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    Toast.makeText(EventDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(EventDetailActivity.this, R.string.commentsuccess, Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    loadingProgressDialog.dismiss();
                    new CommentList().execute(strEventId);
                } else {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(EventDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    /*private class EventDetail extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(EventDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("eid", strEventId));
                responseJSON = CommonMethod.postStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/event/geteventdetails/", nameValuePairs, EventDetailActivity.this);
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                txt_like_event.setText(jsonObject.getString("likecount"));
                txt_comment_event.setText(jsonObject.getString("commentcount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadingProgressDialog.dismiss();
        }
    }*/

    //Like
    private class LikeComment extends AsyncTask<String, Void, String> {
        String responeJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(EventDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("eid", params[0]));
            responeJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"event/eventlike/", nameValuePairs, EventDetailActivity.this);
            return responeJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------------------" + s);
            loadingProgressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
//                    Toast.makeText(EventDetailActivity.this, "Events like successfully.", Toast.LENGTH_SHORT).show();
                    new getLikeCount().execute(strEventId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                loadingProgressDialog.dismiss();
            }
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
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("eid", params[0]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"event/countlikes/", nameValuePairs, EventDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Log.e("count like", "------------------" + jsonArray);
                String count = jsonArray.getString(0);
                if (count.equalsIgnoreCase("")) {
                    txt_like_event.setText("0");

                } else {
                    txt_like_event.setText(count);
                    status = "1";
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
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("eid", params[1]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"event/checklike/", nameValuePairs, EventDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                status = jsonObject.getString("status");
                message = jsonObject.getString("msg");
                if (status.equalsIgnoreCase("0")) {
//                    new LikeComment().execute(strEventId);

                } else {
//                    Toast.makeText(getApplicationContext(), "Already liked this information.", Toast.LENGTH_SHORT).show();
                }

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
//                    Toast.makeText(EventDetailActivity.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
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
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"countviews/event/?eid=" + strEventId + "&view=" + view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------" + s);
            setPhotos();

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
                try {
                    jcplayer.kill();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(EventDetailActivity.this, RegisterActivity.class);
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

    private class GetEventDetail extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(EventDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getEventDetail + "?eid=" + strEventId);
            return responseJSON;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();

                    String commentcounts = jsonObject.getString("commentcount").toString();
                    Log.e("comment", "-----------------" + commentcounts);
//                    txt_comment_event.setText(commentcounts);

                    String likecount = jsonObject.getString("likecount");
                    Log.e("like", "-----------------" + likecount);
                    txt_like_event.setText(likecount);


                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        title = object.getString("Title");
                        description = object.getString("Description");
                        String address = object.getString("Address");
                        String dates = object.getString("Date");
                        view = object.getString("View");
                        String audio = object.getString("Audio").replaceAll(" ", "%20");
                        if (audio.equalsIgnoreCase("")) {

                        } else {
                            audios = audio.split(",");
                            for (int j = 0; i < audios.length; i++) {
                                audiolist.add(audios[i]);
                            }

                        }
                        videoLink = object.getString("VideoLink");
                        Log.e("videolink","-------------"+videoLink);

                        String video = object.getString("Video").replaceAll(" ", "%20");
                        if (video.equalsIgnoreCase("")) {

                        } else {
                            videos = video.split(",");
                            for (int k = 0; k < videos.length; k++) {
                                videolist.add(videos[k]);
                            }
                        }

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

                        Audio = Audiopath + audio;
                        Log.e("Audio file", "-----------------------------" + Audio);
                        Video = Videopath + video;
//                        Photo = Photopath + photo;

                        endaudio = Audio.length();
                        endvideo = Video.length();

                        audiosubstring = Audio.substring(48, endaudio);
                        videosubstring = Video.substring(48, endvideo);


                        Log.e("audiosubstring", "-------------" + audiosubstring);
                        Log.e("videosubstring", "-------------" + videosubstring);
                        Log.e("photosubstring", "-------------" + photosubstring);

                        txtvideolink.setText(videoLink);
                        event_dis.setText(CommonMethod.decodeEmoji(description));
                        et_event.setText(CommonMethod.decodeEmoji(title));
                        txtAddress.setText(CommonMethod.decodeEmoji(address));
                        txtDate.setText(CommonMethod.decodeEmoji(date));


                        if (!videoLink.equalsIgnoreCase("")){
                            String[] str = videoLink.split("/");
                            final String v_vode = str[3];
                            Log.e("code","----------------"+v_vode);
                            txtvideolink.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(EventDetailActivity.this,YoutubePlayActivity.class);
                                    intent.putExtra("vcode",v_vode);
                                    startActivity(intent);
                                }
                            });
                        }
                        
                    }
                    JSONArray jsonArray1 = jsonObject.getJSONArray("Photo");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        String photo = object.getString("Photo");
                        photolist.add(photo);
                        itemSplashArrayList.add(new ImageItemSplash(CommonURL.ImagePath + CommonAPI_Name.eventimage + photo, CommonURL.ImagePath + CommonAPI_Name.eventimage + photo));


                        Photo = Photopath + photo;
                        endimage = Photo.length();
                        photosubstring = Photo.substring(48, endimage);
                        if (photosubstring.equalsIgnoreCase("")) {
                            imgEvent.setVisibility(View.GONE);
                            Toast.makeText(EventDetailActivity.this, "This event image not available.", Toast.LENGTH_SHORT).show();
                        } else {

//                            Picasso.with(EventDetailActivity.this)
//                                    .load(Photo)
//                                    .placeholder(R.drawable.loader)
//                                    .resize(0, 200)
//                                    .error(R.drawable.no_image)
//                                    .into(imgEvent);

                            Glide.with(EventDetailActivity.this).load(Photo
                                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(imgEvent);



                            imgEvent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(EventDetailActivity.this, ImageViewActivity.class);
                                    intent.putExtra("imagePath", Photo);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                    new countView().execute();
                    /*if (click_action.equalsIgnoreCase("event_comment_click")) {
                        dialog = new Dialog(EventDetailActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_dialog_bypeople_comment);
                        if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                            new CommentList().execute(strEventId);
                        } else {
                            Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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

                                String strComment = etComment.getText().toString();
                                if (TextUtils.isEmpty(strComment)) {
                                    etComment.setError("Please enter your comment.");
                                    etComment.requestFocus();
                                } else {
                                    if (approve.equalsIgnoreCase("1")) {
                                        new CommentPost().execute(strComment);
                                        etComment.setText("");
                                    } else {
                                        Toast.makeText(EventDetailActivity.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
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
                    } else {
//                        click_action="event_comment_click";
                    }
*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
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
            nameValuePairs.add(new BasicNameValuePair("eid", strEventId));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "event/seteventviewed", nameValuePairs, EventDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response","-----------------"+s);
        }

    }


}
