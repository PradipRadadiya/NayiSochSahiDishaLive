package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.ParticularUserResultDetail;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.TopTenCompetitionList;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.CompetitionItem;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionRulesActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ALL")
public class RecyclerCompetitionCategoryAdapter extends RecyclerView.Adapter<RecyclerCompetitionCategoryAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CompetitionItem> itemArrayList;
    private String id;
    public static final ArrayList<String> compcatid = new ArrayList<>();

    private String dates, times;

    Sharedpreferance sharedpreferance;

    public RecyclerCompetitionCategoryAdapter(Activity activity, ArrayList<CompetitionItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_competition, viewGroup, false);
        sharedpreferance = new Sharedpreferance(activity);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final CompetitionItem competitionItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(competitionItem.getTitle()));
        holder.txt_timer.setText(CommonMethod.decodeEmoji(competitionItem.getTotal_minute() + " : minutes"));

        String[] fullDateTime = competitionItem.getDate().split(" ");
        String[] fullDateTime2 = competitionItem.getTime().split(" ");

        String[] datesarray = fullDateTime[0].split("-");


        holder.txt_last_date.setText(CommonMethod.decodeEmoji(datesarray[2] + "-" + datesarray[1] + "-" + datesarray[0]));

        holder.txt_total_question.setText("Number of question : " + CommonMethod.decodeEmoji(competitionItem.getTotal_question()));

        holder.txt_viewresult.setVisibility(View.GONE);
        holder.txt_topten_list.setVisibility(View.GONE);


        String[] startTimeArray = fullDateTime[1].split(":");
        String[] endTimeArray = fullDateTime2[1].split(":");

        dates=startTimeArray[0]+":"+startTimeArray[1];
        times=endTimeArray[0]+":"+endTimeArray[1];

        Log.e("start time","--------------"+dates);
        Log.e("end time","--------------"+times);

        //End Time
        String status2 = "AM";
        if (Integer.parseInt(startTimeArray[0]) > 11) {
            // If the hour is greater than or equal to 12
            // Then the current AM PM status is PM
            status2 = "PM";
        }
        // Initialize a new variable to hold 12 hour format hour value
        int hour_of_12_hour_format2;

        if (Integer.parseInt(startTimeArray[0]) > 11) {
            // If the hour is greater than or equal to 12
            // Then we subtract 12 from the hour to make it 12 hour format time
            hour_of_12_hour_format2 = Integer.parseInt(startTimeArray[0]) - 12;
        } else {
            hour_of_12_hour_format2 = Integer.parseInt(startTimeArray[0]);
        }

        Log.e("start time", "----------------" + hour_of_12_hour_format2 + ":" + startTimeArray[1] + " " + status2);


        if (hour_of_12_hour_format2 == 0) {
            hour_of_12_hour_format2 = 12;
        }
        //End Time
        String status = "AM";
        if (Integer.parseInt(endTimeArray[0]) > 11) {
            // If the hour is greater than or equal to 12
            // Then the current AM PM status is PM
            status = "PM";
        }
        // Initialize a new variable to hold 12 hour format hour value
        int hour_of_12_hour_format;

        if (Integer.parseInt(endTimeArray[0]) > 11) {
            // If the hour is greater than or equal to 12
            // Then we subtract 12 from the hour to make it 12 hour format time
            hour_of_12_hour_format = Integer.parseInt(endTimeArray[0]) - 12;
        } else {
            hour_of_12_hour_format = Integer.parseInt(endTimeArray[0]);
        }

        Log.e("start time", "----------------" + hour_of_12_hour_format + ":" + endTimeArray[1] + " " + status);


        if (hour_of_12_hour_format == 0) {
            hour_of_12_hour_format = 12;
        }


        holder.txt_time.setText(hour_of_12_hour_format2 + ":" + startTimeArray[1] + " " + status2 + " - " + hour_of_12_hour_format + ":" + endTimeArray[1] + " " + status);

        holder.txt_viewresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ParticularUserResultDetail.class);
                intent.putExtra("competition_id", competitionItem.getId());
                intent.putExtra("user_id", sharedpreferance.getId());
                activity.startActivity(intent);
            }
        });

        holder.txt_topten_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TopTenCompetitionList.class);
                intent.putExtra("cid", competitionItem.getId());
                activity.startActivity(intent);
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
        final TextView txt_timer;
        final TextView txt_time;
        final TextView txt_last_date;
        final TextView txt_total_question;
        final TextView txt_viewresult;
        final TextView txt_topten_list;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_timer = (TextView) itemView.findViewById(R.id.txt_timer);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_last_date = (TextView) itemView.findViewById(R.id.txt_last_date);
            txt_total_question = (TextView) itemView.findViewById(R.id.txt_total_question);
            txt_viewresult = (TextView) itemView.findViewById(R.id.txt_viewresult);
            txt_topten_list = (TextView) itemView.findViewById(R.id.txt_topten_list);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();

            if (sharedpreferance.getId().equalsIgnoreCase("")) {

                Toast.makeText(activity, R.string.notregister, Toast.LENGTH_SHORT).show();

            } else {

                Intent intent = new Intent(v.getContext(), CompetitionRulesActivity.class);
                intent.putExtra("cid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("rules", itemArrayList.get(getAdapterPosition()).getRules());
                intent.putExtra("minute", itemArrayList.get(getAdapterPosition()).getTotal_minute());
                intent.putExtra("starttime", dates);
                intent.putExtra("endtime", times);
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
