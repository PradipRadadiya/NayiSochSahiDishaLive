package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private TextView txt_header;
    ImageView imgarrorback,img_serach;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        txt_header= (TextView) findViewById(R.id.txt_title);
        txt_header.setText("Privacy Policy");
        imgarrorback= (ImageView) findViewById(R.id.imgarrorback);
        img_serach= (ImageView) findViewById(R.id.img_search);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_serach.setVisibility(View.GONE);
    }
}
