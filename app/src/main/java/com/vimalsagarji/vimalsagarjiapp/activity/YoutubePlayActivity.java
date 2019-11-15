package com.vimalsagarji.vimalsagarjiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.Config;

public class YoutubePlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerView playerView;
    String vcode, title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        vcode=getIntent().getStringExtra("vcode");
        playerView = (YouTubePlayerView) findViewById(R.id.player_view);
        playerView.initialize(Config.API_KEY, this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.loadVideo(vcode);
//            player.cueVideo("cn9PwcMUtSI");
//            player.loadVideo("cn9PwcMUtSI");
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
// shows dialog if user interaction may fix the error
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            // displays the error occured during the initialization
//            String error = String.format(getString(R.string.error_string), errorResult.toString());
            Toast.makeText(this, "video not play something are wrong.", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(Config.API_KEY, this);
        }
    }


    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.player_view);
    }

    private final class EventListener implements YouTubePlayer.PlaybackEventListener {


        /**
         * Called when video starts playing
         */
        @Override
        public void onPlaying() {
            Log.e("Status", "Playing");
        }


        /**
         * Called when video stops playing
         */
        @Override
        public void onPaused() {
            Log.e("Status", "Paused");
        }


        /**
         * Called when video stopped for a reason other than paused
         */
        @Override
        public void onStopped() {
            Log.e("Status", "Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
        }

        @Override
        public void onSeekTo(int i) {
        }
    }

    private final class StateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        /**
         * Called when player begins loading a video. During this duration, player
         * won't accept any command that may affect the video playback
         */
        @Override
        public void onLoading() {
        }

        /**
         * Called when video is loaded. After this player can accept
         * the playback affecting commands
         *
         * @param s Video Id String
         */
        @Override
        public void onLoaded(String s) {
        }


        /**
         * Called when YouTube ad is started
         */
        @Override
        public void onAdStarted() {
        }


        /**
         * Called when video starts playing
         */
        @Override
        public void onVideoStarted() {
        }


        /**
         * Called when video is ended
         */
        @Override
        public void onVideoEnded() {
        }


        /**
         * Called when any kind of error occurs
         *
         * @param errorReason Error string showing the reason behind it
         */
        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
        }
    }
}
