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
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.AudioPlayActivity;
import com.vimalsagarji.vimalsagarjiapp.model.AudioAllItem;
import com.vimalsagarji.vimalsagarjiapp.util.AllOurCommonUrl;

import java.util.ArrayList;

import static com.vimalsagarji.vimalsagarjiapp.fragment.event_fragment.TodayEventFragment.video_play_url;

public class AudioAllAdapter extends RecyclerView.Adapter<AudioAllAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<AudioAllItem> itemArrayList;

    public AudioAllAdapter(Activity activity, ArrayList<AudioAllItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.audio_all_item, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        AudioAllItem audioAllItem = itemArrayList.get(i);
        holder.txt_audio_title.setText(audioAllItem.getTitle());

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView txt_audio_title;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_audio);
            txt_audio_title = (TextView) itemView.findViewById(R.id.txt_audio_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.e("onClick", "------------");
            if (itemArrayList.get(getAdapterPosition()).getAudio().equalsIgnoreCase("")) {
                Toast.makeText(activity, "Audio not available.", Toast.LENGTH_LONG).show();
            } else {
                video_play_url = AllOurCommonUrl.videopath + itemArrayList.get(getAdapterPosition()).getAudio();

                Intent intent = new Intent(activity, AudioPlayActivity.class);
                intent.putExtra("audiopath", AllOurCommonUrl.audiopath + itemArrayList.get(getAdapterPosition()).getAudio().replaceAll(" ", "%20"));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }

        }


    }


}
