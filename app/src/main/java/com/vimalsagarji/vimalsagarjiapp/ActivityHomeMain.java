package com.vimalsagarji.vimalsagarjiapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.AboutAppGuruji;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.AboutAppInfo;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.GurujiMissionActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.GurujiVisionActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.JainismActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.NotificationActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.ProfileActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SettingActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.VisharMessageActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.splash.ThirdSpalshScreenActivity;
import com.vimalsagarji.vimalsagarjiapp.adpter.HomeSliderAdapter;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.AudioCategory;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.CompetitionActivity;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.EventCategory;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.Gallery_All_Category;
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.VideoCategory;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.SliderItem;
import com.vimalsagarji.vimalsagarjiapp.retrofit.APIClient;
import com.vimalsagarji.vimalsagarjiapp.retrofit.ApiInterface;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ByPeople;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionAllActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.InformationCategory;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.QuestionAnswerActivity;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.ThoughtsActivity;
import com.vimalsagarji.vimalsagarjiapp.utils.DashboardCirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHomeMain extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rel_info;
    private RelativeLayout rel_event;
    private RelativeLayout rel_audio;
    private RelativeLayout rel_video;
    private RelativeLayout rel_thought;
    private RelativeLayout rel_gallery;
    private RelativeLayout rel_qa;
    private RelativeLayout rel_comp;
    private RelativeLayout rel_bypeople;
    private ViewPager viewpager_slider;
    private DashboardCirclePageIndicator indicator;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private final ArrayList<SliderItem> itemSplashArrayList = new ArrayList<>();
    private View headerLayout;
    private LinearLayout lin_alert;
    private LinearLayout lin_vichar;
    private LinearLayout lin_info;
    private LinearLayout lin_about;
    private LinearLayout lin_event;
    private LinearLayout lin_audio;
    private LinearLayout lin_video;
    private LinearLayout lin_thought;
    private LinearLayout lin_gallery;
    private LinearLayout lin_qa;
    private LinearLayout lin_comp;
    private LinearLayout lin_op;
    private LinearLayout lin_bypeople;
    private LinearLayout lin_setting;
    private LinearLayout lin_show_hide;
    private LinearLayout lin_my_profile;

    private TextView txt_aboutinfo, txt_aboutguruji, txt_aboutmission, txt_appinfo;
    private CircleImageView img_youtube, img_facebook;
    private boolean doubleBackToExitPressedOnce = false;
    private Sharedpreferance sharedpreferance;

    private ImageView img_hide_show;
    private int flag = 0;

    private DrawerLayout drawer_layout;
    private ImageView img_menu_drawer, img_search, img_notification;

    private ImageView img_slide;
    private String currentVersion;
    ImageView img_height;

    //Count posts
    private TextView txt_latestposts_count, txt_d_latest_posts, txt_d_infomation, txt_d_event, txt_d_thought, txt_d_audio, txt_d_video, txt_d_qa, txt_d_bypeople;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        String heightWidth = getScreenResolution(ActivityHomeMain.this);
        Log.e("heightwidth", "---------------" + heightWidth);


        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = pInfo.versionName;
            Log.e("version", "------------------" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        sharedpreferance = new Sharedpreferance(ActivityHomeMain.this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        headerLayout = navigationView.getHeaderView(0);
        findID();
        idClick();

        if (heightWidth.equalsIgnoreCase("480,800")) {
            Log.e("height", "--------");
            img_height.getLayoutParams().height = 130;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.width = 220;
            params.gravity = Gravity.RIGHT;
            params.setMargins(0, 0, 20, 138);

            indicator.setLayoutParams(params);
        }

//        drawer_layout.setPadding(0, getStatusBarHeight(), 0, 0);
        if (CommonMethod.isInternetConnected(ActivityHomeMain.this)) {
            new LoadSlideImage().execute();
            new GetAllNotes().execute();
        } else {
            img_slide.setVisibility(View.VISIBLE);
        }
    }

    //Drawer method navigate slider
    @SuppressLint("RtlHardcoded")
    private void openDrawerSlider() {
        if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
            drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            drawer_layout.openDrawer(Gravity.LEFT);
        }
        if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
            drawer_layout.closeDrawer(Gravity.RIGHT);
        }
    }

    private void findID() {
        img_height = findViewById(R.id.img_height);
        img_slide = findViewById(R.id.img_slide);
        viewpager_slider = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        rel_info = findViewById(R.id.rel_info);
        rel_event = findViewById(R.id.rel_event);
        rel_audio = findViewById(R.id.rel_audio);
        rel_video = findViewById(R.id.rel_video);
        rel_thought = findViewById(R.id.rel_thought);
        rel_gallery = findViewById(R.id.rel_gallery);
        rel_qa = findViewById(R.id.rel_qa);
        rel_comp = findViewById(R.id.rel_comp);
        rel_bypeople = findViewById(R.id.rel_bypeople);
        img_youtube = headerLayout.findViewById(R.id.img_youtube);
        img_facebook = headerLayout.findViewById(R.id.img_facebook);
        lin_alert = headerLayout.findViewById(R.id.lin_alert);
        lin_vichar = headerLayout.findViewById(R.id.lin_vichar);
        lin_my_profile = headerLayout.findViewById(R.id.lin_my_profile);
        lin_info = headerLayout.findViewById(R.id.lin_info);
        lin_about = headerLayout.findViewById(R.id.lin_about);
        lin_event = headerLayout.findViewById(R.id.lin_event);
        lin_audio = headerLayout.findViewById(R.id.lin_audio);
        lin_video = headerLayout.findViewById(R.id.lin_video);
        lin_thought = headerLayout.findViewById(R.id.lin_thought);
        lin_gallery = headerLayout.findViewById(R.id.lin_gallery);
        lin_qa = headerLayout.findViewById(R.id.lin_qa);
        lin_comp = headerLayout.findViewById(R.id.lin_comp);
        lin_op = headerLayout.findViewById(R.id.lin_op);
        lin_bypeople = headerLayout.findViewById(R.id.lin_bypeople);
        lin_setting = headerLayout.findViewById(R.id.lin_setting);
        lin_show_hide = headerLayout.findViewById(R.id.lin_show_hide);
        txt_aboutinfo = headerLayout.findViewById(R.id.txt_aboutinfo);
        txt_aboutguruji = headerLayout.findViewById(R.id.txt_aboutguruji);
        txt_aboutmission = headerLayout.findViewById(R.id.txt_aboutmission);
        txt_appinfo = headerLayout.findViewById(R.id.txt_appinfo);
        img_hide_show = headerLayout.findViewById(R.id.img_adds);
        txt_latestposts_count = findViewById(R.id.txt_latestposts_count);
        txt_d_latest_posts = headerLayout.findViewById(R.id.txt_d_latest_posts);
        txt_d_infomation = headerLayout.findViewById(R.id.txt_d_infomation);
        txt_d_event = headerLayout.findViewById(R.id.txt_d_event);
        txt_d_thought = headerLayout.findViewById(R.id.txt_d_thought);
        txt_d_audio = headerLayout.findViewById(R.id.txt_d_audio);
        txt_d_video = headerLayout.findViewById(R.id.txt_d_video);
        txt_d_qa = headerLayout.findViewById(R.id.txt_d_qa);
        txt_d_bypeople = headerLayout.findViewById(R.id.txt_d_bypeople);
        drawer_layout = findViewById(R.id.drawer_layout);
        img_menu_drawer = findViewById(R.id.img_menu_drawer);
        img_search = findViewById(R.id.img_search);
        img_notification = findViewById(R.id.img_notification);

    }

    private void idClick() {
        rel_info.setOnClickListener(this);
        rel_event.setOnClickListener(this);
        rel_audio.setOnClickListener(this);
        rel_video.setOnClickListener(this);
        rel_thought.setOnClickListener(this);
        rel_gallery.setOnClickListener(this);
        rel_qa.setOnClickListener(this);
        rel_comp.setOnClickListener(this);
        rel_bypeople.setOnClickListener(this);
        lin_alert.setOnClickListener(this);
        lin_vichar.setOnClickListener(this);
        lin_my_profile.setOnClickListener(this);
        lin_vichar.setVisibility(View.GONE);
        lin_info.setOnClickListener(this);
        lin_about.setOnClickListener(this);
        lin_event.setOnClickListener(this);
        lin_audio.setOnClickListener(this);
        lin_video.setOnClickListener(this);
        lin_thought.setOnClickListener(this);
        lin_gallery.setOnClickListener(this);
        lin_qa.setOnClickListener(this);
        lin_comp.setOnClickListener(this);
        lin_op.setOnClickListener(this);
        lin_bypeople.setOnClickListener(this);
        lin_setting.setOnClickListener(this);
        img_youtube.setOnClickListener(this);
        img_facebook.setOnClickListener(this);
        txt_aboutinfo.setOnClickListener(this);
        txt_aboutguruji.setOnClickListener(this);
        txt_aboutmission.setOnClickListener(this);
        txt_appinfo.setOnClickListener(this);
        img_menu_drawer.setOnClickListener(this);
        img_search.setOnClickListener(this);
        img_notification.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Log.e("double back click", "----------------");
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                    Log.e("back click", "----------------");

                }
            }, 2000);

        }

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_info:
                Intent intent1 = new Intent(ActivityHomeMain.this, InformationCategory.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;


            case R.id.rel_event:
                intent1 = new Intent(ActivityHomeMain.this, EventCategory.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_audio:
                intent1 = new Intent(ActivityHomeMain.this, AudioCategory.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_video:
                intent1 = new Intent(ActivityHomeMain.this, VideoCategory.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;


            case R.id.rel_thought:
                intent1 = new Intent(ActivityHomeMain.this, ThoughtsActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_gallery:
                intent1 = new Intent(ActivityHomeMain.this, Gallery_All_Category.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_qa:
                intent1 = new Intent(ActivityHomeMain.this, QuestionAnswerActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_comp:
                intent1 = new Intent(ActivityHomeMain.this, CompetitionAllActivity.class);
//                intent1 = new Intent(ActivityHomeMain.this, CompetitionActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.rel_bypeople:
                intent1 = new Intent(ActivityHomeMain.this, JainismActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

              /*  intent1 = new Intent(ActivityHomeMain.this, ByPeople.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/


                break;

            case R.id.img_menu_drawer:
                openDrawerSlider();
                break;

            case R.id.img_search:
                intent1 = new Intent(ActivityHomeMain.this, SearchActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.img_notification:
                intent1 = new Intent(ActivityHomeMain.this, NotificationActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.lin_alert:
                intent1 = new Intent(ActivityHomeMain.this, NotificationActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_vichar:
                intent1 = new Intent(ActivityHomeMain.this, VisharMessageActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_my_profile:
                if (sharedpreferance.getId().equalsIgnoreCase("")){
                    onBackPressed();
                    Toast.makeText(this, R.string.notregister, Toast.LENGTH_LONG).show();
                }else {
                    intent1 = new Intent(ActivityHomeMain.this, ProfileActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    onBackPressed();
                }
                break;

            case R.id.lin_info:
                Log.e("lin_info", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, InformationCategory.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_about:
                Log.e("lin_about", "------------------" + "click");
                if (flag == 0) {
                    flag = 1;
                    img_hide_show.setImageResource(R.drawable.ic_remove_black_24dp);
                    lin_show_hide.setVisibility(View.VISIBLE);

                } else {
                    flag = 0;
                    img_hide_show.setImageResource(R.drawable.ic_add_black_24dp);
                    lin_show_hide.setVisibility(View.GONE);
                }
                break;

            case R.id.lin_event:
                Log.e("information", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, EventCategory.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_audio:
                Log.e("lin_event", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, AudioCategory.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_video:
                Log.e("lin_video", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, VideoCategory.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_thought:
                Log.e("lin_video", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, ThoughtsActivity.class);
                startActivity(intent1);
                onBackPressed();
                break;

            case R.id.lin_gallery:
                Log.e("lin_gallery", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, Gallery_All_Category.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_qa:
                Log.e("lin_qa", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, QuestionAnswerActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_comp:
                Log.e("lin_comp", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, CompetitionAllActivity.class);
//                intent1 = new Intent(ActivityHomeMain.this, CompetitionActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_op:
                Log.e("lin_op", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, OpinionPoll.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_bypeople:
                Log.e("lin_bypeople", "------------------" + "click");
                intent1 = new Intent(ActivityHomeMain.this, ByPeople.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();
                break;

            case R.id.lin_setting:
                intent1 = new Intent(ActivityHomeMain.this, SettingActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                onBackPressed();

                /*onBackPressed();
                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_setting);
                dialog.show();
                pushonoff = (ToggleButton) dialog.findViewById(R.id.pushonoff);
                img_back = (ImageView) dialog.findViewById(R.id.img_back);

                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {
                    pushonoff.setChecked(true);
                } else {
                    pushonoff.setChecked(false);
                }
                pushonoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // The toggle is enabled
                            sharedpreferance.savePushNotification("pushon");
                            pushonoff.setChecked(true);
                            Toast.makeText(MainActivity.this, "Push notification on.", Toast.LENGTH_SHORT).show();
                        } else {
                            // The toggle is disabled
                            sharedpreferance.savePushNotification("pushoff");
                            pushonoff.setChecked(false);
                            Toast.makeText(MainActivity.this, "Push notification off.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getWindow().setAttributes(attrs);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
                break;

            case R.id.txt_aboutinfo:
                intent1 = new Intent(ActivityHomeMain.this, AboutAppGuruji.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                onBackPressed();
                break;

            case R.id.txt_aboutguruji:
                intent1 = new Intent(ActivityHomeMain.this, GurujiVisionActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                onBackPressed();
                break;

            case R.id.txt_aboutmission:
                intent1 = new Intent(ActivityHomeMain.this, GurujiMissionActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                onBackPressed();
                break;

            case R.id.txt_appinfo:
                intent1 = new Intent(ActivityHomeMain.this, AboutAppInfo.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                onBackPressed();
                break;

            case R.id.img_youtube:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCBl49_j0js41gkXG6lhcUmQ"));
                    startActivity(intent);
                } catch (Exception ignored) {
                }
                onBackPressed();
                break;

            case R.id.img_facebook:
//                try {
//
//
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aacharyavimalsagarsooriji/"));
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aacharyavimalsagarsooriji/"));
//                }
////                openFB("100006434383261");
//                onBackPressed();
//

             /*   try
                {
                    Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/aacharyavimalsagarsooriji"));
                    startActivity(followIntent);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() {
                            Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/aacharyavimalsagarsooriji"));
                            startActivity(followIntent);
                        }
                    }, 1000 * 2);

                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aacharyavimalsagarsooriji")));
                    String errorMessage = (e.getMessage()==null)?"Message is empty":e.getMessage();
                    Log.e("Unlock_ScreenActivityd" ,errorMessage);
                }*/


/*
                try
                {
                    Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/Vimalsagarsuruji"));
                    startActivity(followIntent);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run() {
                            Intent followIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/Vimalsagarsuruji"));
                            startActivity(followIntent);
                        }
                    }, 1000 * 2);

                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aacharyavimalsagarsuruji")));
                    String errorMessage = (e.getMessage()==null)?"Message is empty":e.getMessage();
                    Log.e("Unlock_Activity:FacebookAppNot" ,errorMessage);
                }*/

                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//
//        if (CommonMethod.isInternetConnected(ActivityHomeMain.this)) {
////            new LoadSlideImage().execute();
//            img_slide.setVisibility(View.GONE);
//        } else {
//            img_slide.setVisibility(View.VISIBLE);
//        }

        if (sharedpreferance.getId().equalsIgnoreCase("")) {
            txt_latestposts_count.setVisibility(View.GONE);
            txt_d_latest_posts.setVisibility(View.GONE);
            txt_d_infomation.setVisibility(View.GONE);
            txt_d_event.setVisibility(View.GONE);
            txt_d_thought.setVisibility(View.GONE);
            txt_d_audio.setVisibility(View.GONE);
            txt_d_video.setVisibility(View.GONE);
            txt_d_qa.setVisibility(View.GONE);
            txt_d_bypeople.setVisibility(View.GONE);
        } else {
            new checkCount().execute();
        }
//
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadSlideImage extends AsyncTask<String, Void, String> {
        String response = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityHomeMain.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {


                response = CommonMethod.getStringResponse(CommonUrl.Main_url + "gallery/getallbanner");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            try {
                new CheckVersion().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("response", "---------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        itemSplashArrayList.add(new SliderItem(CommonUrl.Main_url + "static/bannerimage/" + jsonObject1.getString("image")));
                    }


                }
            } catch (JSONException e) {

                e.printStackTrace();
            }


            HomeSliderAdapter customImageAdapter = new HomeSliderAdapter(ActivityHomeMain.this, itemSplashArrayList);

            viewpager_slider.setAdapter(customImageAdapter);
            indicator.setViewPager(viewpager_slider);

            Log.e("image length", "" + itemSplashArrayList.size());

            if (itemSplashArrayList.size() > 1) {
                NUM_PAGES = itemSplashArrayList.size();
                final android.os.Handler handler = new android.os.Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == NUM_PAGES) {
                            currentPage = 0;
                        }
                        viewpager_slider.setCurrentItem(currentPage++);
                    }
                };

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);

                    }

                }, 5000, 5000);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @SuppressLint("StaticFieldLeak")
    private class CheckVersion extends AsyncTask<String, Void, String> {
        String responseString = "";


        @Override
        protected String doInBackground(String... strings) {
            try {
                responseString = CommonMethod.getStringResponse(CommonUrl.Main_url + "info/getversion");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    String playStoreVersion = jsonObject.getString("message");

                    //noinspection StatementWithEmptyBody,StatementWithEmptyBody
                    if (!playStoreVersion.equalsIgnoreCase(currentVersion)) {
                        final AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(ActivityHomeMain.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(ActivityHomeMain.this);
                        }
                        builder.setCancelable(false);
                        builder.setTitle("Alert")

                                .setMessage("This apps latest update version available in play store, so please download it.")
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete]

                                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }

                                    }
                                })
                                .setNegativeButton("Remind me later", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing

                                        dialog.dismiss();

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class checkCount extends AsyncTask<String, Void, String> {

        String responseJson = "";


        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid", sharedpreferance.getId()));

            responseJson = CommonMethod.postStringResponse(CommonUrl.Main_url + "countviews/getuserviewcount", nameValuePairs, ActivityHomeMain.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONObject object = jsonObject.getJSONObject("data");
                    int audio = object.getInt("audio");
                    int bypeople = object.getInt("bypeople");
                    int event = object.getInt("event");
                    int info = object.getInt("info");
                    int thought = object.getInt("thought");
                    int video = object.getInt("video");
                    int qa = object.getInt("qa");

                    int total = audio + bypeople + event + info + thought + video + qa;
                    if (total == 0) {
                        txt_d_latest_posts.setVisibility(View.GONE);
                        txt_latestposts_count.setVisibility(View.GONE);
                    } else {
                        txt_d_latest_posts.setVisibility(View.VISIBLE);
                        txt_latestposts_count.setVisibility(View.VISIBLE);
                        txt_d_latest_posts.setText(String.valueOf(total));
                        txt_latestposts_count.setText(String.valueOf(total));
                    }

                    if (audio == 0) {
                        txt_d_audio.setVisibility(View.GONE);
                    } else {
                        txt_d_audio.setVisibility(View.VISIBLE);
                        txt_d_audio.setText(String.valueOf(audio));
                    }

                    if (bypeople == 0) {
                        txt_d_bypeople.setVisibility(View.GONE);

                    } else {
                        txt_d_bypeople.setVisibility(View.VISIBLE);
                        txt_d_bypeople.setText(String.valueOf(bypeople));

                    }

                    if (event == 0) {
                        txt_d_event.setVisibility(View.GONE);
                    } else {
                        txt_d_event.setVisibility(View.VISIBLE);
                        txt_d_event.setText(String.valueOf(event));
                    }

                    if (info == 0) {
                        txt_d_infomation.setVisibility(View.GONE);
                    } else {
                        txt_d_infomation.setVisibility(View.VISIBLE);
                        txt_d_infomation.setText(String.valueOf(info));
                    }

                    if (thought == 0) {
                        txt_d_thought.setVisibility(View.GONE);
                    } else {
                        txt_d_thought.setVisibility(View.VISIBLE);
                        txt_d_thought.setText(String.valueOf(thought));
                    }

                    if (video == 0) {
                        txt_d_video.setVisibility(View.GONE);
                    } else {
                        txt_d_video.setVisibility(View.VISIBLE);
                        txt_d_video.setText(String.valueOf(video));

                    }

                    if (qa == 0) {
                        txt_d_qa.setVisibility(View.GONE);
                    } else {
                        txt_d_qa.setVisibility(View.VISIBLE);
                        txt_d_qa.setText(String.valueOf(qa));
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void checkUserLogin() {
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.checkUserLogin(sharedpreferance.getEmail(), sharedpreferance.getMobile(), sharedpreferance.getToken());
        callApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                Log.e("reponse", "-----------------" + response.body());
                if (response.isSuccessful()) {

                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Log.e(" retrofit status", "-----------------success");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Log.e("array", "-----------------success");
                                String uid = object.getString("ID");
                                String email = object.getString("EmailID");
                                String mobile = object.getString("Phone");
                                String Address = object.getString("Address");
                                sharedpreferance.saveId(uid);
                                sharedpreferance.saveEmail(email);
                                sharedpreferance.saveMobile(mobile);
                                sharedpreferance.saveFirst_name(Address);

                                Log.e("email", "---------------" + sharedpreferance.getEmail());
                                Log.e("uid", "---------------" + sharedpreferance.getId());


                            }

                        } else {
                            Intent intent = new Intent(ActivityHomeMain.this, RegisterActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                Toast.makeText(ActivityHomeMain.this, R.string.reopen, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class GetAllNotes extends AsyncTask<String, Void, String> {
        String responseJSON = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url + "competition/gettodaycompetitionnote");
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    usersItems=new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e("id", "---------------" + id);
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String date = jsonObject1.getString("datetime");
//                        String time = jsonObject1.getString("time");
                        final Dialog dialog = new Dialog(ActivityHomeMain.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_alert);
                        dialog.show();
                        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close_popup);

                        img_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
                        TextView txt_description = (TextView) dialog.findViewById(R.id.txt_description);
                        TextView txt_date = (TextView) dialog.findViewById(R.id.txt_date);
                        TextView txt_time = (TextView) dialog.findViewById(R.id.txt_time);
                        txt_title.setText(CommonMethod.decodeEmoji(title));
                        txt_description.setText(CommonMethod.decodeEmoji(description));
                        String[] datesarray = CommonMethod.decodeEmoji(date).split("-");
                        txt_date.setText(CommonMethod.decodeEmoji(datesarray[2] + "-" + datesarray[1] + "-" + datesarray[0]));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(dialog.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private static String getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return width + "," + height;
    }


}
