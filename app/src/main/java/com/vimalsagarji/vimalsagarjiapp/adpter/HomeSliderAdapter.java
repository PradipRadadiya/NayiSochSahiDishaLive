package com.vimalsagarji.vimalsagarjiapp.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.model.SliderItem;

import java.util.ArrayList;


public class HomeSliderAdapter extends android.support.v4.view.PagerAdapter {
    private final Context mcontext;
    private final LayoutInflater layoutInflater;
    private final ArrayList<SliderItem> imageItemArrayList;

    public HomeSliderAdapter(Context context, ArrayList<SliderItem> imageItemArrayList) {
        mcontext = context;
        this.imageItemArrayList = imageItemArrayList;
        layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageItemArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.home_slider_item, container, false);
        SliderItem item = imageItemArrayList.get(position);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        final TextView txt_quote = (TextView) itemView.findViewById(R.id.txt_quote);

        if (imageItemArrayList.size() == 0) {
            imageView.setImageResource(R.drawable.noimageavailable);
        }
        Log.e("image url", "------------------" + item.getImageUrl());

//        Picasso.with(mcontext).load(item.getImageUrl()).into(imageView);

        Glide.with(mcontext).load(item.getImageUrl()
                .replaceAll(" ", "%20")).crossFade().dontAnimate().into(imageView);


        imageView.setTag(imageItemArrayList.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
