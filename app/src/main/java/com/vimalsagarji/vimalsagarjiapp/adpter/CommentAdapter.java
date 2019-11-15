package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommentsList;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<CommentsList> itemArrayList;
    private String id;

    public CommentAdapter(Activity activity, ArrayList<CommentsList> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final CommentsList commentList = itemArrayList.get(i);

        if (commentList.getName().equalsIgnoreCase("null")) {
            holder.txt_unm.setText("Admin");
        } else {
            holder.txt_unm.setText(CommonMethod.decodeEmoji(commentList.getName()));
        }
        holder.txt_post.setText(CommonMethod.decodeEmoji(commentList.getComment()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(commentList.getDate()));
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView txt_unm;
        final TextView txt_post;
        final TextView txt_date;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_unm = (TextView) itemView.findViewById(R.id.txt_unm);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_post = (TextView) itemView.findViewById(R.id.txt_post);
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
