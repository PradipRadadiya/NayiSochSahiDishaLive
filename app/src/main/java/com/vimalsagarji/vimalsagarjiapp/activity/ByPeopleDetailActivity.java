package com.vimalsagarji.vimalsagarjiapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.ImageViewActivity;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.VideoFullActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.jcplayer.JcPlayerView;
import com.vimalsagarji.vimalsagarjiapp.util.CommonAPI_Name;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;
import com.vimalsagarji.vimalsagarjiapp.utils.MarshMallowPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.vimalsagarji.vimalsagarjiapp.R.id.imgarrorback;
import static com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.TodayEventFragment.video_play_url;
/**
 * Created by BHARAT on 04/02/2016.
 */
@SuppressWarnings("ALL")
public class ByPeopleDetailActivity extends AppCompatActivity implements View.OnClickListener {

    // private static ImageView imgHome, imgHomeBack;
    private KProgressHUD loadingProgressDialog;
    private EditText etTitle;
    private ImageView img_video_icon;
    private TextView txtDate, txtDescri;
    private static final String TAG = ThoughtsDetailActivity.class.getSimpleName();
    private Button btnLike;
    private Button btnComment;
    private Button btnLikeClick;
    private final ArrayList<String> listID = new ArrayList<String>();
    private final ArrayList<String> listComment = new ArrayList<String>();
    private final ArrayList<String> listThoughtID = new ArrayList<String>();
    private final ArrayList<String> listIs_Approved = new ArrayList<String>();
    private ArrayList<String> listUserID = new ArrayList<String>();
    private final ArrayList<String> listname = new ArrayList<String>();
    private Dialog dialog;
    private ListView listView;
    private ImageView imgBack, imgHome, img_bypeople, img_photo, img_audio;
    private String video;
    private String image;
    private String audio;
    //    private JCVideoPlayerStandard videoView;
    private String strtitle;
    private String status;
    private String message;
    private LinearLayout lin_like, lin_comment;

    private TextView txt_like;
    private TextView txt_comment;

    private SeekBar mSeekBarPlayer;
    private LinearLayout lin_music;
    private LinearLayout rl_layout;
    private Sharedpreferance sharedpreferance;
    private JcPlayerView jcplayer;
    private String audiosubstring;
    private String videosubstring;
    private String photosubstring;
    private String approve = "1";
    private String pid;
    private String view;
    TextView txt_title;
    private final String AudioPath = CommonUrl.Main_url+"static/bypeopleaudio/";
    private final String VideoPath = CommonUrl.Main_url+"static/bypeoplevideo/";
    private final String imagepath = CommonUrl.Main_url+"static/bypeopleimage/";
    String click_action;
    RelativeLayout rel_video;
    TextView txt_nodata, txtvideolink;

    String p, a, vi;
    int commentsize;

    private LinearLayout img_share;
    String title;
    String post;
    private ProgressBar progressBar;
//    String videoLink = "";


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            jcplayer.kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_allby_people_fragment_second);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sharedpreferance = new Sharedpreferance(ByPeopleDetailActivity.this);
//        new CheckUserApprove().execute();
        createID();
        getPhotoFromCamera();
        imgHome.setVisibility(View.GONE);


        Intent intent1 = getIntent();
        pid = intent1.getStringExtra("postId");
        click_action = intent1.getStringExtra("click_action");
        Log.e("pid", "--------------------" + pid);
        txt_title.setText("By People Detail");

        lin_music.setVisibility(View.GONE);
        rel_video.setVisibility(View.GONE);
        mSeekBarPlayer.setClickable(false);


        imgBack.setOnClickListener(this);
        img_photo.setOnClickListener(this);
        img_video_icon.setOnClickListener(this);
        img_audio.setOnClickListener(this);


        if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
            if (sharedpreferance.getId().equalsIgnoreCase("")) {
//                new CheckLike().execute(sharedpreferance.getId(), pid);
            } else {
                new CheckLike().execute(sharedpreferance.getId(), pid);
                new checkViewed().execute();
            }
            new GetPostDetail().execute();
            new CommentList2().execute(pid);
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


        lin_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {
                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {
                    if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                        if (status.equalsIgnoreCase("0")) {
                            new LikeComment().execute(pid);
//                            new getLikeCount().execute(pid);
//                            new CheckLike().execute(sharedpreferance.getId(), pid);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.likealready, Toast.LENGTH_SHORT).show();
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
            public void onClick(final View v) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {

                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {

                    dialog = new Dialog(ByPeopleDetailActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_bypeople_comment);
                    progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar);
                    if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                        new CommentList().execute(pid);
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
                    final RelativeLayout rl_layout = (RelativeLayout) dialog.findViewById(R.id.layout_byPeople);

                    dialog.show();
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    btnPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sharedpreferance.getId().equalsIgnoreCase("")) {

                                Toast.makeText(ByPeopleDetailActivity.this, R.string.notregister, Toast.LENGTH_SHORT).show();
//                            Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                            } else {
                                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                                    String strComment = etComment.getText().toString();
                                    if (TextUtils.isEmpty(strComment)) {
                                        etComment.setError("Please enter your comments!");
                                        etComment.requestFocus();
                                    } else {
                                        if (approve.equalsIgnoreCase("1")) {
                                            new CommentPost().execute(CommonMethod.encodeEmoji(etComment.getText().toString().replaceAll("%","percent")));
                                            etComment.setText("");
                                        } else {
//                                        Toast.makeText(ByPeopleDetailActivity.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
                                        }

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

    private void getPhotoFromCamera() {
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        } else {
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();
            } else {

            }
        }
    }

    private void createID() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_allbyPepoleSecondActivity);
        setSupportActionBar(toolbar);
//        videoView = (JCVideoPlayerStandard) findViewById(R.id.video_bypeople);
        txt_nodata = (TextView) findViewById(R.id.txt_nodata);
        txtvideolink = (TextView) findViewById(R.id.txtvideolink);
        rel_video = (RelativeLayout) findViewById(R.id.rel_video);
        jcplayer = (JcPlayerView) findViewById(R.id.jcplayer);
        imgBack = (ImageView) toolbar.findViewById(imgarrorback);
        imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        txt_title = (TextView) findViewById(R.id.txt_title);
        btnLike = (Button) findViewById(R.id.btnLike);
        btnComment = (Button) findViewById(R.id.btnComment);
        btnLikeClick = (Button) findViewById(R.id.btnLikeClick);
        etTitle = (EditText) findViewById(R.id.etTitle);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDescri = (TextView) findViewById(R.id.txtDescri);
        img_video_icon = (ImageView) findViewById(R.id.img_video_icon);
        img_bypeople = (ImageView) findViewById(R.id.img_bypeople);
        img_photo = (ImageView) findViewById(R.id.img_photo);
        img_audio = (ImageView) findViewById(R.id.img_audio);
        mSeekBarPlayer = (SeekBar) findViewById(R.id.progress_bar);
        lin_music = (LinearLayout) findViewById(R.id.lin_music);


        //Like Comment Id
        lin_like = (LinearLayout) findViewById(R.id.lin_like);
        lin_comment = (LinearLayout) findViewById(R.id.lin_comment);
        txt_like = (TextView) findViewById(R.id.txt_like_event);
        txt_comment = (TextView) findViewById(R.id.txt_comment_event);
        rl_layout = (LinearLayout) findViewById(R.id.rl_layout);
        img_share = (LinearLayout) findViewById(R.id.img_share);
        img_share.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_share:
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n By People \n" + CommonMethod.decodeEmoji(title) + "\n\n" + getResources().getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.imgarrorback:
                onBackPressed();
                onPause();
                break;
            case R.id.img_photo:
                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                    try {
                        jcplayer.kill();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lin_music.setVisibility(View.GONE);
                    rel_video.setVisibility(View.GONE);
                    jcplayer.setVisibility(View.GONE);
//                    img_bypeople.setVisibility(View.VISIBLE);

//                    img_photo.setBackgroundColor(R.color.kprogresshud_grey_color);
//                    img_audio.setBackgroundColor(R.color.back);
//                    img_video_icon.setBackgroundColor(R.color.back);
                    img_photo.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.kprogresshud_grey_color));
                    img_audio.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.back));
                    img_video_icon.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.back));


                    Log.e("photo","---------------------"+p);


                    if (p.equalsIgnoreCase("null")) {
                        img_bypeople.setVisibility(View.GONE);
                        txt_nodata.setVisibility(View.VISIBLE);
                        txt_nodata.setText("No Image\nAvalable.");
//                        Toast.makeText(ByPeopleDetailActivity.this, "Image not available.", Toast.LENGTH_SHORT).show();

                    } else {
                        txt_nodata.setVisibility(View.GONE);
                        img_bypeople.setVisibility(View.VISIBLE);

//                        Picasso.with(ByPeopleDetailActivity.this)
//                                .load(image.replaceAll(" ", "%20"))
//                                .placeholder(R.drawable.loader)
//                                .resize(0, 200)
//                                .error(R.drawable.no_image)
//                                .into(img_bypeople);


                        Glide.with(ByPeopleDetailActivity.this).load(image
                                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).into(img_bypeople);


//                        Intent intent = new Intent(ByPeopleDetailActivity.this, ImageViewActivity.class);
//                        intent.putExtra("imagePath", image.replaceAll(" ", "%20"));
//                        startActivity(intent);
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
                break;
            case R.id.img_video_icon:
                img_photo.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.back));
                img_audio.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.back));
                img_video_icon.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.kprogresshud_grey_color));
                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                    try {
                        jcplayer.kill();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jcplayer.setVisibility(View.GONE);
                    lin_music.setVisibility(View.GONE);
//                    rel_video.setVisibility(View.VISIBLE);
                    img_bypeople.setVisibility(View.GONE);
                    img_bypeople.setVisibility(View.GONE);


                    if (vi.equalsIgnoreCase("")) {
                        rel_video.setVisibility(View.GONE);
                        txt_nodata.setVisibility(View.VISIBLE);
                        txt_nodata.setText("Video not\n Avalable");

//                        Toast.makeText(ByPeopleDetailActivity.this, "Video not available.", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        txt_nodata.setVisibility(View.GONE);
                        rel_video.setVisibility(View.VISIBLE);
                        video_play_url = video;
                        rel_video.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ByPeopleDetailActivity.this, VideoFullActivity.class);
                                startActivity(intent);
                            }
                        });
//                        videoView.setUp(video, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, strtitle);
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
                break;
            case R.id.img_audio:
//                lin_music.setVisibility(View.VISIBLE);
//                img_photo.setBackgroundColor(R.color.back);
//                img_audio.setBackgroundColor(R.color.kprogresshud_grey_color);
//                img_video_icon.setBackgroundColor(R.color.back);
                img_photo.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.back));
                img_audio.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.kprogresshud_grey_color));
                img_video_icon.setBackgroundColor(ContextCompat.getColor(ByPeopleDetailActivity.this, R.color.back));
                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                    rel_video.setVisibility(View.GONE);
                    img_bypeople.setVisibility(View.GONE);
//                    jcplayer.setVisibility(View.VISIBLE);
                    if (a.equalsIgnoreCase("")) {
//                        Toast.makeText(ByPeopleDetailActivity.this, "Audio not available.", Toast.LENGTH_SHORT).show();
                        jcplayer.setVisibility(View.GONE);
                        txt_nodata.setVisibility(View.VISIBLE);
                        txt_nodata.setText("Audio not\n Avalable");
                    } else {
                        txt_nodata.setVisibility(View.GONE);

                        jcplayer.setVisibility(View.VISIBLE);
                        try {
                            jcplayer.playAudio(audio.replaceAll(" ", "%20"), "music");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                break;
        }
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
            if (listname.get(position).equalsIgnoreCase("null")) {
                holder.txtCommentUserName.setText("Admin");
                holder.txtCommentDescription.setText(CommonMethod.decodeEmoji(listComment.get(position)));
            } else {
                holder.txtCommentUserName.setText(CommonMethod.decodeEmoji(listname.get(position)));
                holder.txtCommentDescription.setText(CommonMethod.decodeEmoji(listComment.get(position)));
            }
            return convertView;
        }

        private class ViewHolder {
            TextView txtCommentUserName, txtCommentDescription;

        }
    }


    //Comment List
    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*loadingProgressDialog = KProgressHUD.create(ByPeopleDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("pid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"bypeople/getallappcomments/?page=1&psize=1000", nameValuePairs, ByPeopleDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    listUserID = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strThoughtID = object.getString("PostID");
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
                        listname.add(name);

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
            TextView txt_nodata_today = (TextView) dialog.findViewById(R.id.txt_nocomment);
            if (listView != null) {
                CustomAdpter adpter = new CustomAdpter(getApplicationContext(), listUserID);
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
           /* loadingProgressDialog = KProgressHUD.create(ByPeopleDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("pid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"bypeople/getallappcomments/?page=1&psize=1000", nameValuePairs, ByPeopleDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    listUserID = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strThoughtID = object.getString("PostID");
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
                        listname.add(name);

                    }
                    commentsize = listComment.size();
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

    /*private class CountComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ByPeopleDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("pid", params[0]));
            responseJSON = CommonMethod.postStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/bypeople/getallappcomments/?page=1&psize=1000", nameValuePairs, ByPeopleDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    listUserID = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strThoughtID = object.getString("PostID");
                        String strComment = object.getString("Comment");
                        String strIs_Approved = object.getString("Is_Approved");
                        String strUserID = object.getString("UserID");
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

                    }
                    int count = listUserID.size();
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
    }*/

    //Comment Post
    private class CommentPost extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ByPeopleDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("pid", "-----------------" + pid);
            Log.e("uid", "-----------------" + sharedpreferance.getId());

            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("pid", pid));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("Comment", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"bypeople/comment/", nameValuePairs, ByPeopleDetailActivity.this);

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
                    Toast.makeText(ByPeopleDetailActivity.this, R.string.commentsuccess, Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    new CommentList().execute(pid);
//                    Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                loadingProgressDialog.dismiss();
            }
        }
    }

    //Like
    private class LikeComment extends AsyncTask<String, Void, String> {
        String responeJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ByPeopleDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("pid", params[0]));
            responeJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"bypeople/postlike/", nameValuePairs, ByPeopleDetailActivity.this);
            return responeJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------------------" + s);
            status = "1";
            loadingProgressDialog.dismiss();
            new getLikeCount().execute(pid);
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
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("pid", params[0]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"bypeople/countlikes/", nameValuePairs, ByPeopleDetailActivity.this);
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
            nameValuePairs.add(new ch.boye.httpclientandroidlib.message.BasicNameValuePair("pid", params[1]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"bypeople/checklike/", nameValuePairs, ByPeopleDetailActivity.this);
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
//                    Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
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
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"countviews/bypeople/?bypid=" + pid + "&view=" + view);
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
                try {
                    jcplayer.kill();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ByPeopleDetailActivity.this, RegisterActivity.class);
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

    private class GetPostDetail extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(ByPeopleDetailActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getpostbyidforadmin + "?pid=" + pid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (loadingProgressDialog != null) {
                    loadingProgressDialog.dismiss();
                }
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    String commentcount = jsonObject.getString("commentcount");
                    Log.e("comment", "-----------------" + commentcount);
//                    txt_comment.setText(commentcount);

                    String likecount = jsonObject.getString("likecount");
                    Log.e("like", "-----------------" + likecount);
                    txt_like.setText(likecount);


                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String photo = object.getString("Photo").replaceAll(" ", "%20");
                        p = object.getString("Photo");
                        title = object.getString("Title");
                        post = object.getString("Post");
                        String uid = object.getString("UserID");
                        String dates = object.getString("Date");
                        view = object.getString("View");
                        String audios = object.getString("Audio").replaceAll(" ", "%20");
                        a = object.getString("Audio").replaceAll(" ", "%20");
                        String audioimage = object.getString("AudioImage").replaceAll(" ", "%20");
                        String videoLink = CommonMethod.decodeEmoji(object.getString("VideoLink"));
                        String videos = object.getString("Video").replaceAll(" ", "%20");
                        vi = object.getString("Video").replaceAll(" ", "%20");
                        String videoimage = object.getString("VideoImage").replaceAll(" ", "%20");
                        view = object.getString("View");

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


                        strtitle = title;
                        Log.e("title", "--------------------" + strtitle);
                        String strdate = date;
                        Log.e("strdate", "--------------------" + strdate);
                        String strdescri = post;
                        Log.e("strdescri", "--------------------" + strdescri);
                        video = VideoPath + videos;
                        Log.e("video", "--------------------" + video);
                        image = imagepath + photo;
                        Log.e("image", "--------------------" + image);
                        audio = AudioPath + audios;
                        Log.e("audio", "--------------------" + audio);


                        int endaudio = audio.length();
                        int endvideo = video.length();
                        int endimage = image.length();
                        audiosubstring = audio.substring(51, endaudio);
                        videosubstring = video.substring(51, endvideo);
                        photosubstring = image.substring(51, endimage);
                        Log.e("audiosubstring", "-------------" + audiosubstring);
                        Log.e("videosubstring", "-------------" + videosubstring);
                        Log.e("photosubstring", "-------------" + photosubstring);


                        etTitle.setText(CommonMethod.decodeEmoji(strtitle));
                        txtDate.setText(CommonMethod.decodeEmoji(strdate));
                        txtDescri.setText(CommonMethod.decodeEmoji(strdescri));
                        txtvideolink.setText(CommonMethod.decodeEmoji(videoLink));
                        rel_video.setVisibility(View.GONE);


                        Log.e("videolink","---------------------"+videoLink);
                        if (!videoLink.equalsIgnoreCase("")) {
                            String[] str = videoLink.split("/");
                            final String v_vode = str[3];
                            Log.e("code", "----------------" + v_vode);
                            txtvideolink.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ByPeopleDetailActivity.this, YoutubePlayActivity.class);
                                    intent.putExtra("vcode", v_vode);
                                    startActivity(intent);
                                }
                            });

                        }else{
                            txtvideolink.setText("----");
                        }


                        Log.e("photo", "---------------" + photosubstring);
                        if (p.equalsIgnoreCase("null")) {
                            img_bypeople.setVisibility(View.GONE);
                            txt_nodata.setVisibility(View.VISIBLE);
//                            Toast.makeText(ByPeopleDetailActivity.this, "Image not available.", Toast.LENGTH_SHORT).show();
                        } else {
                            img_bypeople.setVisibility(View.VISIBLE);
                            txt_nodata.setVisibility(View.GONE);
                            txt_nodata.setText("No Image\nAvalable.");

//                            Picasso.with(ByPeopleDetailActivity.this)
//                                    .load(image.replaceAll(" ", "%20"))
//                                    .placeholder(R.drawable.loader)
//                                    .resize(0, 200)
//                                    .error(R.drawable.no_image)
//                                    .into(img_bypeople);



                            Glide.with(ByPeopleDetailActivity.this).load(image
                                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(img_bypeople);

                            img_bypeople.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ByPeopleDetailActivity.this, ImageViewActivity.class);
                                    intent.putExtra("imagePath", image.replaceAll(" ", "%20"));
                                    startActivity(intent);
                                }
                            });
                        }
                        new countView().execute();


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
            nameValuePairs.add(new BasicNameValuePair("bid", pid));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "bypeople/setbypeopleviewed", nameValuePairs, ByPeopleDetailActivity.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response","-----------------"+s);
        }

    }

}

