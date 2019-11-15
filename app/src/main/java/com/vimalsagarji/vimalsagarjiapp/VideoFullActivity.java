package com.vimalsagarji.vimalsagarjiapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import static com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.TodayEventFragment.video_play_url;

/**
 * Created by Grapes-Pradip on 07-Jun-17.
 */

public class VideoFullActivity extends AppCompatActivity {

    VideoView videoView;
    // Declare variables
    ProgressBar progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplay);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        videoView = (VideoView) findViewById(R.id.videoView);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoFullActivity.this);
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(video_play_url);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
       /* videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

                progressbar.setVisibility(View.GONE);
                videoView.start();
            }
        });*/

        progressbar.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressbar.setVisibility(View.GONE);
                videoView.start();

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                            progressbar.setVisibility(View.VISIBLE);
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                            progressbar.setVisibility(View.GONE);
                        return false;
                    }
                });
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                finish();
            }
        });
    }

}
