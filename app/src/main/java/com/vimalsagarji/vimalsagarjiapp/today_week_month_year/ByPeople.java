package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.NewPostByPeople;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.RegisterActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.fragment.bypeople_fragment.AllbyPeopleFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.bypeople_fragment.ThisMonthbyPeopleFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.bypeople_fragment.ThisWeekbyPeopleFragment;
import com.vimalsagarji.vimalsagarjiapp.fragment.bypeople_fragment.TodaybyPeopleFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public class ByPeople extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_today;
    private TextView txt_thisweek;
    private TextView txt_thismonth;
    private TextView txt_all;
    @SuppressWarnings("unused")
    private FrameLayout framecontent;
    private View view_today;
    private View view_week;
    private View view_month;
    private View view_all;
    private String approve = "";
    Sharedpreferance sharedpreferance;
    Button btn_addpost;
    RelativeLayout main;
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
        main = (RelativeLayout) findViewById(R.id.main);
        sharedpreferance = new Sharedpreferance(ByPeople.this);
        new CheckUserApprove().execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_information);
        setSupportActionBar(toolbar);
        ImageView imgBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        TextView txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        txt_title.setText("By People");
        EditText etText = (EditText) findViewById(R.id.etText);
        btn_addpost = (Button) findViewById(R.id.btn_addpost);
        btn_addpost.setVisibility(View.VISIBLE);
        ImageView imgSerch = (ImageView) findViewById(R.id.imgSerch);
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
        txt_all.setOnClickListener(this);
        img_search.setOnClickListener(this);
        btn_addpost.setOnClickListener(this);
        etText.setHint("By People");
        imgSerch.setOnClickListener(this);
        lin_today = (LinearLayout) findViewById(R.id.lin_today);
        lin_week = (LinearLayout) findViewById(R.id.lin_week);
        lin_month = (LinearLayout) findViewById(R.id.lin_month);
        lin_all = (LinearLayout) findViewById(R.id.lin_all);
        openAllInfromation();

    }


    //Method to show the snackbar
    private void showSnackbar(View v) {
        //Creating snackbar
        Snackbar snackbar = Snackbar.make(v, R.string.notregister, Snackbar.LENGTH_LONG);

        //Adding action to snackbar
        snackbar.setAction("Register", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ByPeople.this, RegisterActivity.class);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addpost:
                if (sharedpreferance.getId().equalsIgnoreCase("")) {

//                    Snackbar.make(v, "Please first register then add post.", Snackbar.LENGTH_SHORT).show();
                    showSnackbar(v);
                } else {
                    Intent intent2 = new Intent(ByPeople.this, NewPostByPeople.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
            case R.id.img_search:
                Intent intent = new Intent(ByPeople.this, SearchActivity.class);
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

            case R.id.imgSerch:
                if (approve.equalsIgnoreCase("1")) {
                    Intent intent2 = new Intent(ByPeople.this, NewPostByPeople.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
//                    Toast.makeText(ByPeople.this, "You are not approved user.", Toast.LENGTH_SHORT).show();
                }
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
            default:
                break;
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
//                    Toast.makeText(ByPeople.this, "" + jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        fr = new TodaybyPeopleFragment();
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
        fr = new ThisWeekbyPeopleFragment();
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
        fr = new ThisMonthbyPeopleFragment();
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
        fr = new AllbyPeopleFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.framecontent, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}


