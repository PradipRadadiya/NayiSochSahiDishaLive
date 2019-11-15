package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.QuestionAllItem;
import com.vimalsagarji.vimalsagarjiapp.retrofit.APIClient;
import com.vimalsagarji.vimalsagarjiapp.retrofit.ApiInterface;
import com.vimalsagarji.vimalsagarjiapp.utils.AllQuestionDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vimalsagarji.vimalsagarjiapp.fcm.MyFirebaseMessagingService.questionid;

@SuppressWarnings("ALL")
public class RecyclerQuestionAllAdapter extends RecyclerView.Adapter<RecyclerQuestionAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<QuestionAllItem> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;
    Sharedpreferance sharedpreferance;

    public RecyclerQuestionAllAdapter(Activity activity, ArrayList<QuestionAllItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
        sharedpreferance=new Sharedpreferance(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_answer_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final QuestionAllItem questionAllItem=itemArrayList.get(i);

        holder.txt_index.setText("[ "+(i+1)+" ]");
        holder.txt_views.setText(questionAllItem.getView());
        holder.txtQuestion.setText("Q : "+ CommonMethod.decodeEmoji(questionAllItem.getQuestion()));
        holder.txtAnswer.setText("A : "+CommonMethod.decodeEmoji(questionAllItem.getAnswer()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(questionAllItem.getDate()));
        holder.txt_postby.setText("Ques. By : " + CommonMethod.decodeEmoji(questionAllItem.getName()));


        if (questionAllItem.getIs_viewed().equalsIgnoreCase("true")){
            holder.img_new.setVisibility(View.GONE);
        }
        else{
            holder.img_new.setVisibility(View.VISIBLE);
        }

        holder.lin_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
                    String sAux = "\n Q & A \n" + CommonMethod.decodeEmoji(questionAllItem.getQuestion()) + "\n\n" +CommonUrl.Main_url+"questiondetail?key="+questionAllItem.getID()+ "\n\n" + activity.getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + activity.getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    activity.startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView txtQuestion, txtAnswer, txt_date, txt_postby, txt_views,txt_index;
        ImageView img_new;
        LinearLayout lin_share;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_index = (TextView) itemView.findViewById(R.id.txt_index);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
            txtAnswer = (TextView) itemView.findViewById(R.id.txtAnswer);
            txt_date = (TextView) itemView.findViewById(R.id.txt_datess);
            txt_postby = (TextView) itemView.findViewById(R.id.txt_postby);
            img_new = (ImageView) itemView.findViewById(R.id.img_new);
            lin_share=itemView.findViewById(R.id.lin_share);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

            QuestionAllItem questionAllItem=itemArrayList.get(getAdapterPosition());
            questionAllItem.setIs_viewed("true");
            notifyItemChanged(getAdapterPosition());

            Intent intent = new Intent(activity, AllQuestionDetail.class);
            Log.e("Question", questionAllItem.getQuestion());
            Log.e("Answer", questionAllItem.getAnswer());
            intent.putExtra("Question", questionAllItem.getQuestion());
            intent.putExtra("Answer", questionAllItem.getAnswer());
            intent.putExtra("view", questionAllItem.getView());
            intent.putExtra("qid",questionAllItem.getID());
            questionid=questionAllItem.getID();

            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    private void addFavoriteInfo(String id) {
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.favQuestion(id, sharedpreferance.getId());
        callApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                Log.e("reponse", "-----------------" + response.body());
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        } else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(activity, R.string.reopen, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
