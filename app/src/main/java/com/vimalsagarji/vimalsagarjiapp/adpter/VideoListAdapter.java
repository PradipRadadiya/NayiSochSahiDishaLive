package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.VideoFullActivity;
import com.vimalsagarji.vimalsagarjiapp.model.PhotoAudioVideoItem;
import com.vimalsagarji.vimalsagarjiapp.util.CommonAPI_Name;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import java.util.ArrayList;

import static com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.TodayEventFragment.video_play_url;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<PhotoAudioVideoItem> itemArrayList;
    private String id;
    Intent intent;

    public VideoListAdapter(Activity activity, ArrayList<PhotoAudioVideoItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.videoitem, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final PhotoAudioVideoItem photoAudioVideoItem = itemArrayList.get(position);
//        Log.e("video", "---------------" + CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
//        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position).replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_item);
//        Picasso.with(activity).load(CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_item);
        Log.e("video", "---------------" + CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
        holder.txt_videoname.setText(photoAudioVideoItem.getUrl());

        holder.img_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VideoFullActivity.class);
                intent.putExtra("videourl", CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
                video_play_url=CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20");
                Log.e("video url","---------------"+video_play_url);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        holder.img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VideoFullActivity.class);
                intent.putExtra("videourl", CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
                video_play_url=CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20");
                Log.e("video url","---------------"+video_play_url);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        /*if (photoAudioVideoItem.getSpecification().equalsIgnoreCase("photo")) {
            holder.img_play.setVisibility(View.GONE);
            Log.e("url","-----------"+photoAudioVideoItem.getUrl());
//            intent=new Intent(activity, ImageViewActivity.class);
//            intent.putExtra("imagePath",photoAudioVideoItem.getUrl());
//            activity.startActivity(intent);
        } else if (photoAudioVideoItem.getSpecification().equalsIgnoreCase("audio")) {


        } else if (photoAudioVideoItem.getSpecification().equalsIgnoreCase("video")) {
//            intent=new Intent(activity, VideoFullActivity.class);
//            intent.putExtra("videopath",photoAudioVideoItem.getUrl());
//            activity.startActivity(intent);

        }*/


    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView img_item, img_play;
        TextView txt_videoname;

        public ViewHolder(View itemView) {
            super(itemView);

            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            img_play = (ImageView) itemView.findViewById(R.id.img_play);
            txt_videoname = (TextView) itemView.findViewById(R.id.txt_videoname);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
