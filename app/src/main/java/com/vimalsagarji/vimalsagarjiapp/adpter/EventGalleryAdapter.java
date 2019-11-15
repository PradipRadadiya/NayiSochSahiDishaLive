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

import com.bumptech.glide.Glide;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.EventGalleryAllImagesActivity;
import com.vimalsagarji.vimalsagarjiapp.model.EventGalleryItem;

import java.util.ArrayList;

public class EventGalleryAdapter extends RecyclerView.Adapter<EventGalleryAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<EventGalleryItem> itemArrayList;

    public EventGalleryAdapter(Activity activity, ArrayList<EventGalleryItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_event_album_item, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {


        EventGalleryItem eventGalleryItem=itemArrayList.get(i);
        holder.txt_title.setText(eventGalleryItem.getName());
        Glide.with(activity).load(eventGalleryItem.getPhoto()
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(holder.img_album);

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img_album;
        private TextView txt_title;

        public ViewHolder(View itemView) {
            super(itemView);
            img_album = (ImageView) itemView.findViewById(R.id.img_album);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.e("onClick", "------------");
            Intent intent=new Intent(activity, EventGalleryAllImagesActivity.class);
            intent.putExtra("eid",itemArrayList.get(getAdapterPosition()).getId());
            activity.startActivity(intent);

        }


    }


}
