package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment.AllInformationFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment.InformationAllFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment.InformationMonthFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment.InformationTodayFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment.InformationWeekFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment.ThisMonthInformationFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment.ThisWeekInformationFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.information_fragment.TodayInformationFragment;
import com.vimalsagarji.vimalsagarjiapp.util.NetworkChangeReceiver;

@SuppressWarnings("ALL")
public class InformationCategory extends AppCompatActivity implements View.OnClickListener, NetworkChangeReceiver.NetworkChange {

    private TextView txt_today;
    private TextView txt_thisweek;
    private TextView txt_thismonth;
    private TextView txt_all;
    private FrameLayout framecontent;
    private View view_today;
    private View view_week;
    private View view_month;
    private View view_all;
    private static Dialog csDialog = null;
    private static Context context = null;

    LinearLayout lin_today, lin_week, lin_month, lin_all;

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
        InformationCategory.context = InformationCategory.this;
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_information);
//        setSupportActionBar(toolbar);

        ImageView imgBack = (ImageView) findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) findViewById(R.id.img_search);
        TextView txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Information");
        EditText etText = (EditText) findViewById(R.id.etText);
        txt_today = (TextView) findViewById(R.id.txt_today);
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



        img_search.setOnClickListener(this);
        txt_all.setOnClickListener(this);


        lin_today = (LinearLayout) findViewById(R.id.lin_today);
        lin_week = (LinearLayout) findViewById(R.id.lin_week);
        lin_month = (LinearLayout) findViewById(R.id.lin_month);
        lin_all = (LinearLayout) findViewById(R.id.lin_all);


        etText.setHint("Search Information");
        openAllInfromation();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_search:
                Intent intent = new Intent(InformationCategory.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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


        Fragment fr = null;
        fr = new InformationTodayFragment();
//        fr = new TodayInformationFragment();
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
        fr = new InformationWeekFragment();
//        fr = new ThisWeekInformationFragment();
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
        fr = new InformationMonthFragment();
//        fr = new ThisMonthInformationFragment();
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
        fr = new InformationAllFragment();
//        fr = new AllInformationFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onNetworkChange(String status) {
        try {
            if (csDialog == null && status.equals("FALSE")) {
                csDialog = new Dialog(context);

                //FrameLayout csMainLay = null;
                Button csTryAgainBtn;

                csDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                csDialog.setContentView(R.layout.dialog_network_connectivity);

                csTryAgainBtn = (Button) csDialog.findViewById(R.id.networkConnectivity_tryagainBtn);
                //csMainLay.setAlpha(0.9f);
                //((TextView)(csDialog.findViewById(R.id.dialog_textView))).setText("Please check your internet connection");
                csDialog.setCancelable(false);
                csDialog.setCanceledOnTouchOutside(false);

                csTryAgainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new NetworkChangeReceiver();
                    }
                });

                if (status.equals("FALSE")) {
                    csDialog.show();
                    Window window = csDialog.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            } else //noinspection StatementWithEmptyBody
                if (status.equals("TRUE") && csDialog != null && csDialog.isShowing()) {
                    csDialog.dismiss();
                    csDialog = null;
                } else {

                }
        } catch (Exception e) {
            if (csDialog != null && csDialog.isShowing())
                csDialog.dismiss();

            csDialog = null;
        }
    }
}
