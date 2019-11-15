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
import com.vimalsagarji.vimalsagarjiapp.model.EventGalleryItem;

import java.util.ArrayList;

public class EventAllImageGalleryAdapter extends RecyclerView.Adapter<EventAllImageGalleryAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<EventGalleryItem> itemArrayList;

    public EventAllImageGalleryAdapter(Activity activity, ArrayList<EventGalleryItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_all_image_galleryi_tem, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {


        EventGalleryItem eventGalleryItem=itemArrayList.get(i);
        Glide.with(activity).load(eventGalleryItem.getPhoto()
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(holder.img_album);


        holder.img_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Splash_Activity2.class);
//                itemSplashArrayList.clear();
                intent.putExtra("position", String.valueOf(i));
                intent.putExtra("cid", "");
//                intent.putExtra("imagePath", CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position).replaceAll(" ", "%20"));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img_album;

        public ViewHolder(View itemView) {
            super(itemView);
            img_album = (ImageView) itemView.findViewById(R.id.img_album);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.e("onClick", "------------");







        }


    }


}
