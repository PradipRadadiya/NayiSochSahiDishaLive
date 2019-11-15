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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.ThoughtsDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.VideoDetailActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.model.ThoughtItem;
import com.vimalsagarji.vimalsagarjiapp.model.VideoItem;
import com.vimalsagarji.vimalsagarjiapp.retrofit.APIClient;
import com.vimalsagarji.vimalsagarjiapp.retrofit.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class RecyclerVideoAllAdapter extends RecyclerView.Adapter<RecyclerVideoAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<VideoItem> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;
    Sharedpreferance sharedpreferance;

    public RecyclerVideoAllAdapter(Activity activity, ArrayList<VideoItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
        sharedpreferance=new Sharedpreferance(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_category_list_item, viewGroup, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        VideoItem videoItem = itemArrayList.get(i);

        holder.txt_views.setText(CommonMethod.decodeEmoji(videoItem.getView()));
        holder.txtVideoName.setText(CommonMethod.decodeEmoji(videoItem.getVideoName()));
        holder.txtVideoDate.setText(CommonMethod.decodeEmoji(videoItem.getDate()));

        Glide.with(activity).load(videoItem.getPhoto()
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).dontAnimate().into(holder.imgVideo);


       /* holder.txt_views.setText(CommonMethod.decodeEmoji(questionAllItem.getView()));
        holder.txt_Title.setText(CommonMethod.decodeEmoji(questionAllItem.getTitle()));
        holder.txt_Description.setText(CommonMethod.decodeEmoji(questionAllItem.getDescription()));
        holder.txt_Date.setText(CommonMethod.decodeEmoji(questionAllItem.getDate()));
*/
        if (videoItem.getIs_viewed().equalsIgnoreCase("true")) {
            holder.img_new.setVisibility(View.GONE);
        } else {
            holder.img_new.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txtVideoName, txtVideoDate, txt_views;
        ImageView imgVideo, imgPlayVideo, imgPlayPush, imgPlayVideo1, img_new;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            txtVideoName = (TextView) itemView.findViewById(R.id.txtVideoName);
            txtVideoDate = (TextView) itemView.findViewById(R.id.txtVideoDate);
            imgVideo = (ImageView) itemView.findViewById(R.id.imgVideo);
            imgPlayVideo = (ImageView) itemView.findViewById(R.id.imgPlayVideo);
            imgPlayPush = (ImageView) itemView.findViewById(R.id.imgPlayPush);
            imgPlayVideo1 = (ImageView) itemView.findViewById(R.id.imgPlayVideo1);
            img_new = (ImageView) itemView.findViewById(R.id.img_new);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            VideoItem videoItem = itemArrayList.get(getAdapterPosition());

            videoItem.setIs_viewed("true");
            notifyItemChanged(getAdapterPosition());

            Intent intent = new Intent(activity, VideoDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("video", videoItem.getVideo());
            intent.putExtra("videoname", videoItem.getVideoName());
            intent.putExtra("id", videoItem.getID());
            intent.putExtra("catid", videoItem.getCategoryID());
            intent.putExtra("photo", videoItem.getPhoto());
            intent.putExtra("date", videoItem.getDate());
            intent.putExtra("view", videoItem.getView());

            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void addFavoriteVideo(String id) {
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.favVideo(id, sharedpreferance.getId());
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
