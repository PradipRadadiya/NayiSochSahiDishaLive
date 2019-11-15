package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionQuestionItem;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerCompetitionQuestionAllAdapter extends RecyclerView.Adapter<RecyclerCompetitionQuestionAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CompetitionQuestionItem> itemArrayList;
    private String id;

    //    public ArrayList<String> qidlist = new ArrayList<>();
//    public ArrayList<String> answerlist = new ArrayList<>();
    public String[] questionarr;
    public String[] answerarr;

    public int mSelectedItem = -1;
    int a=65;

    public RecyclerCompetitionQuestionAllAdapter(Activity activity, ArrayList<CompetitionQuestionItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
        Log.e("layout", "---------------call");
        questionarr = new String[itemArrayList.size()];
        answerarr = new String[itemArrayList.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_competition_all, viewGroup, false);


        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final CompetitionQuestionItem competitionQuestionItem = itemArrayList.get(i);

        holder.txt_answer.setVisibility(View.GONE);
        holder.txt_type.setVisibility(View.GONE);


        holder.txt_index.setText(String.valueOf(i + 1) + ") ");
        holder.txt_title.setText(CommonMethod.decodeEmoji(competitionQuestionItem.getQuestion()));


        String[] options;
        options = null;
        holder.lin.setVisibility(View.GONE);
        options = competitionQuestionItem.getOptions().split("\\|");


        Log.e("lenth", "----------------" + options.length);

        holder.radiogroup.removeAllViews();

        for (int j = 0; j < options.length; j++) {

            holder.radioButton = new RadioButton(activity);
//            holder.radioButton.setPadding(0,5,0,5);
            holder.radioButton.setTextSize(17);

           // holder.textview=new TextView(activity);

//            Log.e("id", "----------------" + holder.radioButton.getId());
//            Log.e("option", "----------------" + options[j]);

          //  holder.textview.setText(""+j);
            holder.radioButton.setText("\n"+options[j]+"\n");

//            holder.radioButton.setText(((char)j+a)+options[j]);

            if (competitionQuestionItem.isSelected()) {

                Log.e("if position", "--------------" + i);
                Log.e("if checked", "--------------" + itemArrayList.get(i).getCheckedId());
                Log.e("radio button idd", "--------------" + holder.radioButton.getId());
                holder.radiogroup.setOnCheckedChangeListener(null);
                holder.radiogroup.check(itemArrayList.get(i).getCheckedId());

                if (holder.radioButton.getText().toString().equalsIgnoreCase(itemArrayList.get(i).getAnswer())){
                    holder.radioButton.setChecked(true);
                }else{
                    holder.radioButton.setChecked(false);
                }


//                holder.radioButton.setChecked(true);
//                holder.radiogroup.check(itemArrayList.get(i).getCheckedId());



            } else {
                Log.e("else position", "--------------" + i);
                Log.e("else checked", "--------------");
                holder.radiogroup.setOnCheckedChangeListener(null);
                holder.radiogroup.clearCheck();
            }

            holder.radiogroup.addView(holder.radioButton);

        }
        holder.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

//                    if (checkedId == -1) {
//                        Log.v("onCheck", "Android bug since RadioButton doesn't get unchecked normally!");
//                    } else {
//                        competitionQuestionItem.setSelected(!competitionQuestionItem.isSelected());
//                        competitionQuestionItem.setCheckedId(group.getCheckedRadioButtonId());
//                    }




                competitionQuestionItem.setSelected(true);
                competitionQuestionItem.setCheckedId(checkedId);


                holder.radioButton = (RadioButton) group.findViewById(checkedId);

                String rbtext = (String) holder.radioButton.getText();
                competitionQuestionItem.setAnswer(rbtext);
                notifyDataSetChanged();
//                    String rbtext = "abc";
//                    Log.e("position", "---------------" + i);


                questionarr[i] = competitionQuestionItem.getId();
                answerarr[i] = rbtext;


                Log.e("Question id array", "----------" + questionarr[i]);
                Log.e("Answer list array", "----------" + answerarr[i]);

            }

        });


    }


    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final ImageView img_audio_category;
        final TextView txt_title;
        final TextView txt_index;
        final TextView txt_type;
        final TextView txt_answer;
        final TextView txt_option;
        final TextView txt_yes;
        final TextView txt_no;
        final LinearLayout lin;
        final ImageView img_view;
        final ImageView img_yes, img_no;
        RadioGroup radiogroup;
        RadioButton radioButton;
        //TextView textview;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_index = (TextView) itemView.findViewById(R.id.txt_index);
            txt_yes = (TextView) itemView.findViewById(R.id.txt_yes);
            txt_no = (TextView) itemView.findViewById(R.id.txt_no);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            txt_answer = (TextView) itemView.findViewById(R.id.txt_answer);
            txt_option = (TextView) itemView.findViewById(R.id.txt_option);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            img_yes = (ImageView) itemView.findViewById(R.id.img_yes);
            img_no = (ImageView) itemView.findViewById(R.id.img_no);
            lin = (LinearLayout) itemView.findViewById(R.id.lin);
            radiogroup = (RadioGroup) itemView.findViewById(R.id.radiogroup);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

    }


}
