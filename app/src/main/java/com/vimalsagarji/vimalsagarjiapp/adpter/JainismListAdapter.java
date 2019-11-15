package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.JainismDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.JainismItem;
import com.vimalsagarji.vimalsagarjiapp.model.VicharItem;

import java.io.Serializable;
import java.util.ArrayList;


public class JainismListAdapter extends RecyclerView.Adapter<JainismListAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<JainismItem> itemArrayList;

    public JainismListAdapter(Activity activity, ArrayList<JainismItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_jainism, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JainismItem vicharItem = itemArrayList.get(position);
        holder.txt_title.setText(CommonMethod.decodeEmoji(vicharItem.getTitle()));
        holder.txt_content.setText(CommonMethod.decodeEmoji(vicharItem.getDescription()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(vicharItem.getDate()));
        holder.txt_views.setText(CommonMethod.decodeEmoji(vicharItem.getCountViews()+" Views"));

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView txt_content, txt_date, txt_title,txt_views;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            JainismItem jainismItem=itemArrayList.get(getAdapterPosition());
            Intent intent = new Intent(activity, JainismDetailActivity.class);
            intent.putExtra("itemList", jainismItem);
            activity.startActivity(intent);

        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}
