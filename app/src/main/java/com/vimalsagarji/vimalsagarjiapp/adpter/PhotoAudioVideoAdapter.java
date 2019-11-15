package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.Splash_Activity2;
import com.vimalsagarji.vimalsagarjiapp.util.CommonAPI_Name;
import com.vimalsagarji.vimalsagarjiapp.util.CommonURL;

import java.util.ArrayList;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class PhotoAudioVideoAdapter extends RecyclerView.Adapter<PhotoAudioVideoAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<String> itemArrayList;
    private String id;
    Intent intent;

    public PhotoAudioVideoAdapter(Activity activity, ArrayList<String> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photoaudiovideoitem, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.e("images", "---------------" + itemArrayList.get(position));


//        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position).replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loader).into(holder.img_item);

        Glide.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position)
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(holder.img_item);



        holder.img_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Splash_Activity2.class);
//                itemSplashArrayList.clear();
                intent.putExtra("position", String.valueOf(position));
                intent.putExtra("cid", "");
//                intent.putExtra("imagePath", CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position).replaceAll(" ", "%20"));
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

        public ViewHolder(View itemView) {
            super(itemView);

            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            img_play = (ImageView) itemView.findViewById(R.id.img_play);

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
