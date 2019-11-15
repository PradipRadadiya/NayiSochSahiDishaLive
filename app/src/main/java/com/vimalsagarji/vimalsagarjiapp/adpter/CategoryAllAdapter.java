package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.model.MainCategoryItem;

import java.util.ArrayList;

public class CategoryAllAdapter extends RecyclerView.Adapter<CategoryAllAdapter.ViewHolder> {

    private final Activity activity;
    private final ArrayList<MainCategoryItem> itemArrayList;

    public CategoryAllAdapter(Activity activity, ArrayList<MainCategoryItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_audio_gridview, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        MainCategoryItem mainCategoryItem = itemArrayList.get(i);

        holder.grid_txtTitle.setText(CommonMethod.decodeEmoji(mainCategoryItem.getName()));
        Glide.with(activity).load(mainCategoryItem.getName()
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).into(holder.grid_img);

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView grid_txtTitle;
        ImageView grid_img;

        public ViewHolder(View itemView) {
            super(itemView);

            grid_txtTitle = (TextView) itemView.findViewById(R.id.grid_txtTitle);
            grid_img = (ImageView) itemView.findViewById(R.id.grid_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.e("onClick", "------------");


        }


    }


}
