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

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.jcplayer.JcPlayerView;
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
public class AudioDetail extends AppCompatActivity {

    private KProgressHUD loadingProgressDialog;
    private TextView txt_like;
    private TextView txt_comment;
    private String status;
    private Sharedpreferance sharedpreferance;
    private List<ThisMonthVideo> listComments = new ArrayList<>();
    private Dialog dialog;
    private ListView listView;
    private JcPlayerView jcPlayerView;
    private String approve = "";
    private String id;
    private String view;
    EditText et_event;
    TextView txt_title;
    private final String Imagepath = CommonUrl.Main_url+"static/audioimage/";
    private final String AudioPath = CommonUrl.Main_url+"static/audios/";
    TextView txtDate;
    String click_action;
    int commentsize;
    private final ArrayList<String> listname = new ArrayList<>();
    private ProgressBar progressbar;
    private TextView txt_description;
    LinearLayout img_share;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            jcPlayerView.kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_playing_activity);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sharedpreferance = new Sharedpreferance(AudioDetail.this);
        img_share = (LinearLayout) findViewById(R.id.img_share);
        jcPlayerView = (JcPlayerView) findViewById(R.id.jcplayerview_audio);
        txt_description = (TextView) findViewById(R.id.txt_description);
        et_event = (EditText) findViewById(R.id.et_event);
        txt_title = (TextView) findViewById(R.id.txt_title);

        txt_title.setText("\t Audio Detail");
        txtDate = (TextView) findViewById(R.id.txtDate);

        ImageView imgHomeBack = (ImageView) findViewById(R.id.imgarrorback);
        imgHomeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    jcPlayerView.kill();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        ImageView imgHome = (ImageView) findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        imgHome.setVisibility(View.GONE);
        img_search.setVisibility(View.GONE);

        //Like Comment Id
        LinearLayout lin_like = (LinearLayout) findViewById(R.id.lin_like);
        LinearLayout lin_comment = (LinearLayout) findViewById(R.id.lin_comment);
        ImageView img_like = (ImageView) findViewById(R.id.img_like);
        ImageView img_comment = (ImageView) findViewById(R.id.img_comment);
        txt_like = (TextView) findViewById(R.id.txt_like_event);
        txt_comment = (TextView) findViewById(R.id.txt_comment_event);

        Intent intent = getIntent();
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();


        if (appLinkData != null) {
            Log.e("appLinkData", "--------------" + appLinkData.getQueryParameter("key"));

            id = appLinkData.getQueryParameter("key");
            click_action="information_click";
        } else {

            id = intent.getExtras().getString("id");
            click_action = intent.getExtras().getString("click_action");
        }







        Log.e("id", "------------------" + id);

        if (id.equalsIgnoreCase("eid")) {
            lin_like.setVisibility(View.GONE);
            lin_comment.setVisibility(View.GONE);
            String audioname = intent.getExtras().getString("AudioName");
            et_event.setText(CommonMethod.decodeEmoji(audioname));
            Log.e("audioname", "------------------" + audioname);
            String category_id = intent.getExtras().getString("CategoryID");
            Log.e("category_id", "------------------" + category_id);
            String audio = intent.getExtras().getString("Audio");
            Log.e("audio", "------------------" + audio);
            String photo = intent.getExtras().getString("Photo");
            Log.e("photo", "------------------" + photo);
            String duration = intent.getExtras().getString("Duration");
            Log.e("duration", "------------------" + duration);
            String date = intent.getExtras().getString("Date");
            Log.e("date", "------------------" + date);
            view = intent.getStringExtra("view");
            if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                assert audio != null;
                try {
                    jcPlayerView.playAudio(audio.replaceAll(" ", "%20"), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            txtDate.setText(date);

        } else if (id.equalsIgnoreCase("bid")) {
            lin_like.setVisibility(View.GONE);
            lin_comment.setVisibility(View.GONE);
            String audioname = intent.getExtras().getString("AudioName");
            et_event.setText(CommonMethod.decodeEmoji(audioname));
            Log.e("audioname", "------------------" + audioname);
            String category_id = intent.getExtras().getString("CategoryID");
            Log.e("category_id", "------------------" + category_id);
            String audio = intent.getExtras().getString("Audio");
            Log.e("audio", "------------------" + audio);
            String photo = intent.getExtras().getString("Photo");
            Log.e("photo", "------------------" + photo);
            String duration = intent.getExtras().getString("Duration");
            Log.e("duration", "------------------" + duration);
            String date = intent.getExtras().getString("Date");
            Log.e("date", "------------------" + date);
            view = intent.getStringExtra("view");
            if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                assert audio != null;
                try {
                    jcPlayerView.playAudio(audio.replaceAll(" ", "%20"), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            txtDate.setText(date);
        } else {
            if (CommonMethod.isInternetConnected(AudioDetail.this)) {


                if (sharedpreferance.getId().equalsIgnoreCase("")) {

                } else {
                    new CheckUserApprove().execute();
//                new getLikeCount().execute(id);
                    new CheckLike().execute(sharedpreferance.getId(), id);
                    new checkViewed().execute();
                }
                new AudioDetailId().execute();
                new CommentList2().execute(id);
//                new CheckUserApprove().execute();
//                new getLikeCount().execute(id);
//                new CheckLike().execute(sharedpreferance.getId(), id);
//                new AudioDetailId().execute();
//                new CountComment().execute(id);
            }

        }


        lin_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedpreferance.getId().equalsIgnoreCase("")) {

                    showSnackbar(v);
//                    Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_SHORT).show();
                } else {

                    if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                        if (status.equalsIgnoreCase("0")) {
                            new LikeComment().execute(id);
                            new getLikeCount().execute(id);
                            new CheckLike().execute(sharedpreferance.getId(), id);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.likealready, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(v, R.string.internet, Snackbar.LENGTH_SHORT).show();
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

                    dialog = new Dialog(AudioDetail.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialog_bypeople_comment);
                    progressbar = (ProgressBar) dialog.findViewById(R.id.progressbar);
                    if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                        new CommentList().execute(id);


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
                                Toast.makeText(AudioDetail.this, R.string.notregister, Toast.LENGTH_SHORT).show();

                            } else {
                                String strComment = etComment.getText().toString();
                                if (TextUtils.isEmpty(strComment)) {
                                    etComment.setError("Please enter your comments!");
                                    etComment.requestFocus();
                                } else {
                                    if (approve.equalsIgnoreCase("1")) {
                                        new CommentPost().execute(CommonMethod.encodeEmoji(etComment.getText().toString().replaceAll("%","percent")));
                                        etComment.setText("");
                                    } else {
//                                    Toast.makeText(AudioDetail.this, R.string.notregister, Toast.LENGTH_SHORT).show();
                                        showSnackbar(v);
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


        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n Audio \n" + CommonMethod.decodeEmoji(et_event.getText().toString()) + "\n\n" +CommonUrl.Main_url+"audiodetail?key="+id+"\n\n"+ getResources().getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });



    }

    //Method to show the snackbar
    private void showSnackbar(View v) {
        //Creating snackbar
        Snackbar snackbar = Snackbar.make(v, "Please register to proceed!", Snackbar.LENGTH_LONG);
        //Adding action to snackbar
        snackbar.setAction("Register", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Displaying another snackbar when user click the action for first snackbar
//                Snackbar s = Snackbar.make(v, "Register", Snackbar.LENGTH_LONG);
//                s.show();
                try {
                    jcPlayerView.kill();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(AudioDetail.this, RegisterActivity.class);
                startActivity(intent);finishAffinity();

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

    //Comment List
    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();

*/
            progressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"audio/getallappcomments/?page=1&psize=1000", nameValuePairs, AudioDetail.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressbar.setVisibility(View.GONE);
                    listComments = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
//                        loadingProgressDialog.dismiss();
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strInformationID = object.getString("AudioID");
                        String strComment = object.getString("Comment");
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
            progressbar.setVisibility(View.GONE);
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
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
            /*loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"audio/getallappcomments/?page=1&psize=1000", nameValuePairs, AudioDetail.this);
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
//                        loadingProgressDialog.dismiss();
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        String strInformationID = object.getString("AudioID");
                        String strComment = object.getString("Comment");
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
            /*if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

        }
    }

  /*  private class CountComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));
            responseJSON = CommonMethod.postStringResponse("http://www.aacharyavimalsagarsuriji.com/vimalsagarji/audio/getallappcomments/?page=1&psize=1000", nameValuePairs, AudioDetail.this);
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
                        String strInformationID = object.getString("AudioID");
                        String strComment = object.getString("Comment");
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
                        int count = listComments.size();
                        String c = String.valueOf(count);
                        txt_comment.setText(c);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }

        }
    }*/

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

            loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", id));
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"audio/comment/", nameValuePairs, AudioDetail.this);

            return responseJSON;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(AudioDetail.this, R.string.commentsuccess, Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    loadingProgressDialog.dismiss();
                    new CommentList().execute(id);
                } else {
                    loadingProgressDialog.dismiss();
                    Toast.makeText(AudioDetail.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
            loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));
            responeJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"audio/audiolike/", nameValuePairs, AudioDetail.this);
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

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("aid", params[0]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"audio/countlikes/", nameValuePairs, AudioDetail.this);
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

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("aid", params[1]));

            responseJSON = CommonMethod.postStringResponse(CommonUrl.Main_url+"audio/checklike/", nameValuePairs, AudioDetail.this);
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
//                    Toast.makeText(AudioDetail.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
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
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"countviews/audio/?aid=" + id + "&view=" + view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "----------------" + s);
//            new CommentList2().execute(id);
        }

    }

    private class AudioDetailId extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(AudioDetail.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getaudiobyidforadmin + "?aid=" + id);
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

                    String commentcount = jsonObject.getString("commentcount");
                    Log.e("like", "-----------------" + commentcount);
//                    txt_comment.setText(commentcount);

                    String likecount = jsonObject.getString("likecount");
                    Log.e("like", "-----------------" + likecount);
                    txt_like.setText(likecount);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String audioname = jsonObject1.getString("AudioName");
                        String cid = jsonObject1.getString("CategoryID");
                        String audio = jsonObject1.getString("Audio").replaceAll(" ", "%20");
                        String photo = jsonObject1.getString("Photo").replaceAll(" ", "%20");
                        String duration = jsonObject1.getString("Duration");
                        String date = jsonObject1.getString("Date");
                        String description = jsonObject1.getString("Description");
                        view = jsonObject1.getString("View");


                        String[] string = date.split(" ");
                        Log.e("str1", "--------" + string[0]);
                        Log.e("str2", "--------" + string[1]);

                        Date dt = CommonMethod.convert_date(date);
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


                        et_event.setText(CommonMethod.decodeEmoji(audioname));
                        Log.e("audioname", "------------------" + audioname);
                        String audios = AudioPath + audio;
                        Log.e("audio", "------------------" + audio);
                        String photos = Imagepath + photo;
                        Log.e("photo", "------------------" + photo);
                        Log.e("duration", "------------------" + duration);
                        String dates = fulldate;
                        Log.e("date", "------------------" + date);

                        if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                            assert audios != null;
                            try {
                                jcPlayerView.playAudio(audios.replaceAll(" ", "%20"), "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        txtDate.setText(dates);

                        txt_description.setText(description);
                        new countView().execute();

//                        txt_title.setText(audioname);
//                        txt_date.setText(fulldate);
//                        txt_views.setText(view);
//                        Picasso.with(AudioDetailActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.audioimage + jsonObject1.getString("Photo").replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(img_photo);
//                        jcplayer_audio.playAudio(CommonURL.AudioPath + CommonAPI_Name.audios + jsonObject1.getString("Audio").replaceAll(" ", "%20"), jsonObject1.getString("AudioName"));
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
       /* if (id.equalsIgnoreCase("eid")) {

        } else if (id.equalsIgnoreCase("bid")) {

        } else {
            if (CommonMethod.isInternetConnected(AudioDetail.this)) {
                if (sharedpreferance.getId().equalsIgnoreCase("")) {

                } else {
                    new CheckUserApprove().execute();
//                new getLikeCount().execute(id);
                    new CheckLike().execute(sharedpreferance.getId(), id);
                    new checkViewed().execute();
                }
                new AudioDetailId().execute();
                new CommentList2().execute(id);
//                new CountComment().execute(id);
            }
        }*/
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
            nameValuePairs.add(new BasicNameValuePair("aid", id));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "audio/setaudioviewed", nameValuePairs, AudioDetail.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("check viewed response","-----------------"+s);
        }

    }

}


