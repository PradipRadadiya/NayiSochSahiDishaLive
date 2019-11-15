package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

/**
 * Created by Grapes-Pradip on 26-Oct-17.
 */

public class SettingActivity extends AppCompatActivity {
    Sharedpreferance sharedpreferance;
    private ImageView imgarrorback;
    private TextView txt_title;
    private ImageView img_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
        sharedpreferance = new Sharedpreferance(SettingActivity.this);


        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Setting");

        final ToggleButton pushonoff = (ToggleButton) findViewById(R.id.pushonoff);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);

        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                    Toast.makeText(SettingActivity.this, "You will get notifications from now!", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    sharedpreferance.savePushNotification("pushoff");
                    pushonoff.setChecked(false);
                    Toast.makeText(SettingActivity.this, "You will not receive any notification from now!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        LinearLayout lin_privacy_policy;
        lin_privacy_policy = (LinearLayout) findViewById(R.id.lin_privacy_policy);
        lin_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }
}
