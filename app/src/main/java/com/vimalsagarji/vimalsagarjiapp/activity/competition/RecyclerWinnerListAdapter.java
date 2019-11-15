package com.vimalsagarji.vimalsagarjiapp.activity.competition;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;

import java.util.ArrayList;
import java.util.List;



@SuppressWarnings("ALL")
public class RecyclerWinnerListAdapter extends RecyclerView.Adapter<RecyclerWinnerListAdapter.ViewHolder> {

    private final Activity activity;
    private final List<CompetitionWinner.Datum> itemArrayList;
    private String id;
    public static final ArrayList<String> questionid = new ArrayList<>();

    public RecyclerWinnerListAdapter(Activity activity, List<CompetitionWinner.Datum> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_winner_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        final CompetitionWinner.Datum data = itemArrayList.get(i);
        holder.txt_index.setText("[ " + String.valueOf(i + 1) + " ]");
        holder.txt_name.setText(data.getName());
        holder.txt_phone.setText(data.getPhone());
        holder.txt_attend_comp.setText("Total attend competition - " + data.getTotalAttentedCompetition());
        holder.txt_getting_mark.setText("Total getting marks - " + data.getTotalReceivedMarks());
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        final TextView txt_name, txt_phone, txt_attend_comp, txt_getting_mark, txt_index;

        public ViewHolder(View itemView) {
            super(itemView);
//            this.setIsRecyclable(false);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_phone = (TextView) itemView.findViewById(R.id.txt_phone);
            txt_attend_comp = (TextView) itemView.findViewById(R.id.txt_attend_comp);
            txt_getting_mark = (TextView) itemView.findViewById(R.id.txt_getting_mark);
            txt_index = (TextView) itemView.findViewById(R.id.txt_index_count);
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

        }


    }


}
