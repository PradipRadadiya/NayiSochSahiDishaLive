package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestionCorrectWrongItem;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class RecyclerCompetitionQuestionCorrectWrongAnswerAdapter extends RecyclerView.Adapter<RecyclerCompetitionQuestionCorrectWrongAnswerAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CompetitionQuestionCorrectWrongItem> itemArrayList;
    private String id;

    public RecyclerCompetitionQuestionCorrectWrongAnswerAdapter(Activity activity, ArrayList<CompetitionQuestionCorrectWrongItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_particular_user, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final CompetitionQuestionCorrectWrongItem competitionQuestionItem = itemArrayList.get(i);

//        holder.img_option.setVisibility(View.VISIBLE);
        holder.txt_index.setText(CommonMethod.decodeEmoji(i + 1 + ") "));
        holder.txt_title.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getQuestion()));


        Log.e("competitionQuestionItem.getAnswer()", competitionQuestionItem.getAnswer());
        if (competitionQuestionItem.getAnswer().equalsIgnoreCase("")) {
            holder.txt_type.setVisibility(View.GONE);
            holder.lin_answer.setVisibility(View.GONE);
        } else {
            if (competitionQuestionItem.getAnswer().equalsIgnoreCase("null")) {
                holder.txt_type.setText("--");
            } else {
                holder.txt_type.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getAnswer()));
            }
        }
        holder.txt_answer.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getTrue_answer()));

        Log.e("options user", "------------------" + competitionQuestionItem.getOptions());

        String[] options = competitionQuestionItem.getOptions().split("\\|");

        for (int j = 0; j < options.length; j++) {

            Log.e("single options", "---------------" + options[j]);

            holder.txt_option.append(options[j]+"\n\n");
        }

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView txt_index;
        final TextView txt_title;
        final TextView txt_type;
        final TextView txt_answer;
        final TextView txt_option;
        final ImageView img_view;
        final LinearLayout lin_answer;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_index = (TextView) itemView.findViewById(R.id.txt_index);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            txt_answer = (TextView) itemView.findViewById(R.id.txt_answer);
            txt_option = (TextView) itemView.findViewById(R.id.txt_option);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            lin_answer = (LinearLayout) itemView.findViewById(R.id.lin_answer);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(v.getContext(), AudioDetailActivity.class);
//            intent.putExtra("aid", itemArrayList.get(getAdapterPosition()).getId());
//            intent.putExtra("categoryname", itemArrayList.get(getAdapterPosition()).getCategoryname());
//            v.getContext().startActivity(intent);
//            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

}
