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
import com.vimalsagarji.vimalsagarjiapp.activity.AudioDetail;
import com.vimalsagarji.vimalsagarjiapp.activity.ByPeopleDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.EventDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.InformationDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.ThoughtsDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.VideoDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.NotificationItem;
import com.vimalsagarji.vimalsagarjiapp.model.VicharItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionList;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import java.util.ArrayList;


public class VisharMessageListAdapter extends RecyclerView.Adapter<VisharMessageListAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<VicharItem> itemArrayList;

    public VisharMessageListAdapter(Activity activity, ArrayList<VicharItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vishar, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VicharItem vicharItem = itemArrayList.get(position);

        holder.txt_content.setText(CommonMethod.decodeEmoji(vicharItem.getDescription()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(vicharItem.getDate()));
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView txt_content, txt_date;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
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
