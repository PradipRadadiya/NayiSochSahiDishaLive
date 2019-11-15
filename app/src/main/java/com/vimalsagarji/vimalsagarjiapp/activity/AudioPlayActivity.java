package com.vimalsagarji.vimalsagarjiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.jcplayer.JcPlayerView;


public class AudioPlayActivity extends AppCompatActivity {
    private JcPlayerView jcplayer_audio;
    String audio;
    ImageView imgarrorback,imgHome,img_search;
    TextView txt_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_play);
        imgarrorback= (ImageView) findViewById(R.id.imgarrorback);
        imgHome= (ImageView) findViewById(R.id.imgHome);
        img_search= (ImageView) findViewById(R.id.img_search);
        imgHome.setVisibility(View.GONE);
        img_search.setVisibility(View.GONE);
        txt_title= (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Play Audio");

        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    jcplayer_audio.kill();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onPause();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        Intent intent=getIntent();
        audio=intent.getStringExtra("audiopath");
        jcplayer_audio = (JcPlayerView) findViewById(R.id.jcplayer_audio);
        try {
            jcplayer_audio.playAudio(audio, "music");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            jcplayer_audio.kill();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onPause();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
