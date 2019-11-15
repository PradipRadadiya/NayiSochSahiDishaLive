package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.vimalsagarji.vimalsagarjiapp.categoryactivity.Gallery_All_Category;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.SearchItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionList;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.OpinionPoll;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import java.util.ArrayList;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<SearchItem> itemArrayList;
    private String id;

    public SearchListAdapter(Activity activity, ArrayList<SearchItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchItem searchItem = itemArrayList.get(position);

        if (searchItem.getTable().equalsIgnoreCase("Information")) {
            holder.search_image.setImageResource(R.drawable.infromation);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        } else if (searchItem.getTable().equalsIgnoreCase("Events")) {
            holder.search_image.setImageResource(R.drawable.event);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        } else if (searchItem.getTable().equalsIgnoreCase("Audio")) {
            holder.search_image.setImageResource(R.drawable.audio);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        } else if (searchItem.getTable().equalsIgnoreCase("Video")) {
            holder.search_image.setImageResource(R.drawable.video);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        } else if (searchItem.getTable().equalsIgnoreCase("Thoughts")) {
            holder.search_image.setImageResource(R.drawable.thoughts);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        } else if (searchItem.getTable().equalsIgnoreCase("ByPeople")) {
            holder.search_image.setImageResource(R.drawable.bypeople);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        } else if (searchItem.getTable().equalsIgnoreCase("QuestionAnswer")) {
            holder.search_image.setImageResource(R.drawable.qa);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        } else if (searchItem.getTable().equalsIgnoreCase("CompetitionMain")) {
            holder.search_image.setImageResource(R.drawable.competition);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        } else if (searchItem.getTable().equalsIgnoreCase("Gallery")) {
            holder.search_image.setImageResource(R.drawable.gallary);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
        }else if (searchItem.getTable().equalsIgnoreCase("OpinionPollMain")) {
            holder.search_image.setImageResource(R.drawable.opinionpoll);
            holder.txt_title.setText(CommonMethod.decodeEmoji(searchItem.getTitle()));
            holder.txt_date.setText(CommonMethod.decodeEmoji(searchItem.getDate()));
            holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
            if (searchItem.getDescription().equalsIgnoreCase("")){
                holder.txt_content.setText("Yes & No Answer.");
            }else {
                holder.txt_content.setText(CommonMethod.decodeEmoji(searchItem.getDescription()));
            }
        }

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView search_image;
        TextView txt_title, txt_date, txt_content;

        public ViewHolder(View itemView) {
            super(itemView);
            search_image = (ImageView) itemView.findViewById(R.id.search_image);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Information")) {
                Intent intent = new Intent(v.getContext(), InformationDetailActivity.class);
                intent.putExtra("listID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Events")) {
                Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
                intent.putExtra("listID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Audio")) {
                Intent intent = new Intent(v.getContext(), AudioDetail.class);
                intent.putExtra("id", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Video")) {
                Intent intent = new Intent(v.getContext(), VideoDetailActivity.class);
                intent.putExtra("id", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Thoughts")) {
                Intent intent = new Intent(v.getContext(), ThoughtsDetailActivity.class);
                intent.putExtra("thoughtid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("ByPeople")) {
                Intent intent = new Intent(v.getContext(), ByPeopleDetailActivity.class);
                intent.putExtra("postId", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("QuestionAnswer")) {
                Intent intent = new Intent(v.getContext(), AllQuestionDetail.class);
                intent.putExtra("qid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("CompetitionMain")) {
                Intent intent = new Intent(v.getContext(), CompetitionList.class);
                intent.putExtra("categoryID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("listTitle", itemArrayList.get(getAdapterPosition()).getTitle());
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("Gallery")) {
                Intent intent = new Intent(v.getContext(), Gallery_All_Category.class);
                intent.putExtra("listID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }else if (itemArrayList.get(getAdapterPosition()).getTable().equalsIgnoreCase("OpinionPollMain")) {
                Intent intent = new Intent(v.getContext(), OpinionPoll.class);
                intent.putExtra("listID", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("click_action", "");
                v.getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }


        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
