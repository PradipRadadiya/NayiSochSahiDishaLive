package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.AllEventFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.ThisMonthEventFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.ThisWeekEventFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.TodayEventFragment;

@SuppressWarnings("ALL")
public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_today;
    private TextView txt_thisweek;
    private TextView txt_thismonth;
    private TextView txt_all;
    private FrameLayout framecontent;
    private View view_today;
    private View view_week;
    private View view_month;
    private View view_all;
    LinearLayout lin_today, lin_week, lin_month, lin_all;
    String title,cid;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_information_category1);
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        cid=intent.getStringExtra("event_category_id");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_information);
        setSupportActionBar(toolbar);
        txt_today = (TextView) findViewById(R.id.txt_today);
        txt_today.setText("Upcoming");
        ImageView imgBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        TextView txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        txt_title.setText(CommonMethod.decodeEmoji(title));
        EditText etText = (EditText) findViewById(R.id.etText);
        txt_thisweek = (TextView) findViewById(R.id.txt_thisweek);
        txt_thismonth = (TextView) findViewById(R.id.txt_thismonth);
        txt_all = (TextView) findViewById(R.id.txt_all);
        framecontent = (FrameLayout) findViewById(R.id.framecontent);
        view_today = findViewById(R.id.view_today);
        view_week = findViewById(R.id.view_week);
        view_month = findViewById(R.id.view_month);
        view_all = findViewById(R.id.view_all);
        imgBack.setOnClickListener(this);
        imgHome.setOnClickListener(this);
        txt_today.setOnClickListener(this);
        txt_thisweek.setOnClickListener(this);
        txt_thismonth.setOnClickListener(this);
        txt_all.setOnClickListener(this);
        img_search.setOnClickListener(this);
        etText.setHint("Search Event");
        lin_today = (LinearLayout) findViewById(R.id.lin_today);
        lin_week = (LinearLayout) findViewById(R.id.lin_week);
        lin_month = (LinearLayout) findViewById(R.id.lin_month);
        lin_all = (LinearLayout) findViewById(R.id.lin_all);
        openAllInfromation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_search:
                Intent intent = new Intent(EventActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.imgarrorback:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.imgHome:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.txt_today:
                openTodayInfromation();
                break;
            case R.id.txt_thisweek:
                openWeekInfromation();
                break;
            case R.id.txt_thismonth:
                openMonthInfromation();
                break;
            case R.id.txt_all:
                openAllInfromation();
                break;
            default:
                break;
        }
    }

    private void openTodayInfromation() {
        txt_today.setTextColor(Color.WHITE);
        txt_thisweek.setTextColor(Color.BLACK);
        txt_thismonth.setTextColor(Color.BLACK);
        txt_all.setTextColor(Color.BLACK);


        lin_today.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
        lin_week.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_month.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_all.setBackgroundResource(R.drawable.round_rect_shapeone);





//        // set Fragmentclass Arguments
//        TodayEventFragment fragobj = new TodayEventFragment();
//        fragobj.setArguments(bundle);


        Fragment fr = null;
        fr = new TodayEventFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid",cid);
        fr.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openWeekInfromation() {

        txt_today.setTextColor(Color.BLACK);
        txt_thisweek.setTextColor(Color.WHITE);
        txt_thismonth.setTextColor(Color.BLACK);
        txt_all.setTextColor(Color.BLACK);

        lin_today.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_week.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
        lin_month.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_all.setBackgroundResource(R.drawable.round_rect_shapeone);

        Fragment fr = null;
        fr = new ThisWeekEventFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid",cid);
        fr.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openMonthInfromation() {

        txt_today.setTextColor(Color.BLACK);
        txt_thisweek.setTextColor(Color.BLACK);
        txt_thismonth.setTextColor(Color.WHITE);
        txt_all.setTextColor(Color.BLACK);

        lin_today.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_week.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_month.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
        lin_all.setBackgroundResource(R.drawable.round_rect_shapeone);

        Fragment fr = null;
        fr = new ThisMonthEventFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid",cid);
        fr.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openAllInfromation() {
        txt_today.setTextColor(Color.BLACK);
        txt_thisweek.setTextColor(Color.BLACK);
        txt_thismonth.setTextColor(Color.BLACK);
        txt_all.setTextColor(Color.WHITE);

        lin_today.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_week.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_month.setBackgroundResource(R.drawable.round_rect_shapeone);
        lin_all.setBackgroundResource(R.drawable.round_rect_shapeoneselect);

        Fragment fr = null;
        fr = new AllEventFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid",cid);
        fr.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

