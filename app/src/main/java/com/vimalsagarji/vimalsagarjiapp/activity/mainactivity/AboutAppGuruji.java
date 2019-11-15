package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uncopt.android.widget.text.justify.JustifiedTextView;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;


public class AboutAppGuruji extends AppCompatActivity implements View.OnClickListener {
    private TextView txt_eng, txt_hnd, txt_guj;
    JustifiedTextView txt_description;
    private LinearLayout lin_eng, lin_hnd, lin_guj;
    private TextView txt_title;
    private ImageView imgarrorback, img_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_guruji);
        bindId();

        txt_title.setText("About Guruji");
        img_search.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/calibri_regular.ttf");
        txt_description.setTypeface(face);


        txt_description.setText(CommonMethod.stripHtml(getResources().getString(R.string.about_guruji_hnd)));


//        txt_description.setText(getResources().getString(R.string.about_guruji_hnd));

    }

    private void bindId() {

        txt_title = (TextView) findViewById(R.id.txt_title);
        img_search = (ImageView) findViewById(R.id.img_search);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        txt_eng = (TextView) findViewById(R.id.txt_eng);
        txt_hnd = (TextView) findViewById(R.id.txt_hnd);
        txt_guj = (TextView) findViewById(R.id.txt_guj);
        txt_description = (JustifiedTextView) findViewById(R.id.txt_description);
        lin_eng = (LinearLayout) findViewById(R.id.lin_eng);
        lin_hnd = (LinearLayout) findViewById(R.id.lin_hnd);
        lin_guj = (LinearLayout) findViewById(R.id.lin_guj);

        txt_eng.setOnClickListener(this);
        txt_hnd.setOnClickListener(this);
        txt_guj.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            finish();
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_eng:

                txt_description.setText(getResources().getString(R.string.about_guruji_eng));
                txt_eng.setTextColor(Color.WHITE);
                txt_hnd.setTextColor(Color.BLACK);
                txt_guj.setTextColor(Color.BLACK);

                txt_eng.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
                txt_hnd.setBackgroundResource(R.drawable.round_rect_shapeone);
                txt_guj.setBackgroundResource(R.drawable.round_rect_shapeone);


                break;
            case R.id.txt_hnd:
                txt_description.setText(getResources().getString(R.string.about_guruji_hnd));

                txt_eng.setTextColor(Color.BLACK);
                txt_hnd.setTextColor(Color.WHITE);
                txt_guj.setTextColor(Color.BLACK);

                txt_eng.setBackgroundResource(R.drawable.round_rect_shapeone);
                txt_hnd.setBackgroundResource(R.drawable.round_rect_shapeoneselect);
                txt_guj.setBackgroundResource(R.drawable.round_rect_shapeone);


                break;
            case R.id.txt_guj:
                txt_description.setText(getResources().getString(R.string.about_guruji_guj));
                txt_eng.setTextColor(Color.BLACK);
                txt_hnd.setTextColor(Color.BLACK);
                txt_guj.setTextColor(Color.WHITE);

                txt_eng.setBackgroundResource(R.drawable.round_rect_shapeone);
                txt_hnd.setBackgroundResource(R.drawable.round_rect_shapeone);
                txt_guj.setBackgroundResource(R.drawable.round_rect_shapeoneselect);


                break;
        }
    }
}
