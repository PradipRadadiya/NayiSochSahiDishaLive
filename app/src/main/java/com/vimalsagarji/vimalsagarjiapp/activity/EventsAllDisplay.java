package com.vimalsagarji.vimalsagarjiapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.AudioListAdapter;
import com.vimalsagarji.vimalsagarjiapp.adpter.PhotoAudioVideoAdapter;
import com.vimalsagarji.vimalsagarjiapp.adpter.VideoListAdapter;
import com.vimalsagarji.vimalsagarjiapp.model.PhotoAudioVideoItem;

import java.util.ArrayList;
/**
 * Created by BHARAT on 04/02/2016.
 */
public class EventsAllDisplay extends AppCompatActivity {

    RecyclerView recycleview;
    GridLayoutManager gridLayoutManager;
    ImageView imgarrorback, imgHome, img_search;
    TextView txt_title;
    ArrayList<PhotoAudioVideoItem> photoAudioVideoItems;
    AudioListAdapter audioListAdapter;
    VideoListAdapter videoListAdapter;
    PhotoAudioVideoAdapter photoAudioVideoAdapter;
    ArrayList<String> myList;
    ArrayList<String> photolist;
    TextView textview;
    String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventsall);
        myList = (ArrayList<String>) getIntent().getSerializableExtra("mylist");
        status = getIntent().getStringExtra("status");
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        textview = (TextView) findViewById(R.id.textview);
        gridLayoutManager = new GridLayoutManager(EventsAllDisplay.this, 2);
        recycleview.setLayoutManager(gridLayoutManager);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        txt_title = (TextView) findViewById(R.id.txt_title);
        if (status.equalsIgnoreCase("a")) {
            txt_title.setText("Event Audios");
        }
        if (status.equalsIgnoreCase("v")) {
            txt_title.setText("Event Videos");
        }

        imgHome.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.e("mylist", "-------------" + myList);
        if (status.equalsIgnoreCase("p")) {
            Log.e("mylist","------------"+myList);
            if (myList.isEmpty()) {
                textview.setVisibility(View.VISIBLE);
                textview.setText("No Images Avalable.");
            } else {
                setPhotos();
            }
        } else if (status.equalsIgnoreCase("a")) {
            Log.e("mylistaudio","------------"+photoAudioVideoItems);

            if (myList.isEmpty()) {
                textview.setVisibility(View.VISIBLE);
                textview.setText("No Audio Avalable.");
            } else {
                setAudios();
            }

        } else if (status.equalsIgnoreCase("v")) {
            Log.e("videomylist","------------"+photoAudioVideoItems);

            if (myList.isEmpty()) {
                textview.setVisibility(View.VISIBLE);
                textview.setText("No Video Avalable.");
            } else {
                setVideos();
            }

        }

    }

    private void setAudios() {
        photoAudioVideoItems = new ArrayList<>();
        for (int i = 0; i < myList.size(); i++) {
            photoAudioVideoItems.add(new PhotoAudioVideoItem(myList.get(i), "Audio", "Audio"));
        }
        if (recycleview != null) {
            audioListAdapter = new AudioListAdapter(EventsAllDisplay.this, photoAudioVideoItems);
            if (audioListAdapter.getItemCount() != 0) {
                recycleview.setVisibility(View.VISIBLE);
                recycleview.setAdapter(audioListAdapter);

            } else {
                recycleview.setVisibility(View.GONE);
            }
        }
    }

    private void setVideos() {

        photoAudioVideoItems = new ArrayList<>();
        for (int i = 0; i < myList.size(); i++) {
            photoAudioVideoItems.add(new PhotoAudioVideoItem(myList.get(i), "Video", "Video"));

        }
        if (recycleview != null) {
            videoListAdapter = new VideoListAdapter(EventsAllDisplay.this, photoAudioVideoItems);
            if (videoListAdapter.getItemCount() != 0) {
                recycleview.setVisibility(View.VISIBLE);
                recycleview.setAdapter(videoListAdapter);
            } else {
                recycleview.setVisibility(View.GONE);
            }
        }

    }

    private void setPhotos() {

        if (recycleview != null) {
            photoAudioVideoAdapter = new PhotoAudioVideoAdapter(EventsAllDisplay.this, photolist);
            if (photoAudioVideoAdapter.getItemCount() != 0) {
                recycleview.setVisibility(View.VISIBLE);
                recycleview.setAdapter(photoAudioVideoAdapter);

            } else {
                recycleview.setVisibility(View.GONE);
            }
        }

        photoAudioVideoItems = new ArrayList<>();
        photoAudioVideoAdapter = new PhotoAudioVideoAdapter(EventsAllDisplay.this, photolist);
        recycleview.setAdapter(photoAudioVideoAdapter);

    }


}
