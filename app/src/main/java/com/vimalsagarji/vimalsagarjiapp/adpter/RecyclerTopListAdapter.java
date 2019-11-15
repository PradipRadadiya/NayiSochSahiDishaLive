package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.TopTenItem;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class RecyclerTopListAdapter extends RecyclerView.Adapter<RecyclerTopListAdapter.ViewHolder> {


    private final Activity activity;
    private final List<TopTenItem> itemArrayList;
    private String id;
    public static final ArrayList<String> questionid = new ArrayList<>();

    public RecyclerTopListAdapter(Activity activity, List<TopTenItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_result, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final TopTenItem questiinItem = itemArrayList.get(i);

        holder.txt_rank.setText(""+(i+1));
        holder.txt_name.setText(CommonMethod.decodeEmoji(questiinItem.getName()));
        holder.txt_user_marks.setText(CommonMethod.decodeEmoji("Getting Marks : "+questiinItem.getUser_marks()));
        holder.txt_total_marks.setText(CommonMethod.decodeEmoji("Total Marks : "+questiinItem.getTotal_mark()));

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView txt_name;
        final TextView txt_user_marks;
        final TextView txt_total_marks;
        final TextView txt_rank;

        public ViewHolder(View itemView) {
            super(itemView);
//            this.setIsRecyclable(false);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_user_marks = (TextView) itemView.findViewById(R.id.txt_user_marks);
            txt_total_marks = (TextView) itemView.findViewById(R.id.txt_total_marks);
            txt_rank = (TextView) itemView.findViewById(R.id.txt_rank);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();

            return false;
        }

        @Override
        public void onClick(View v) {

//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(v.getContext(), ParticularUserResultDetail.class);
//            intent.putExtra("competition_id", itemArrayList.get(getAdapterPosition()).getCompetition_id());
//            intent.putExtra("user_id", itemArrayList.get(getAdapterPosition()).getUser_id());
//
//            v.getContext().startActivity(intent);
//            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }


    }

}
