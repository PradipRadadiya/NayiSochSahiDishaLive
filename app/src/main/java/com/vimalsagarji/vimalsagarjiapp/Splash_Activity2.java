package com.vimalsagarji.vimalsagarjiapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vimalsagarji.vimalsagarjiapp.today_week_month_year.GalleryCategory.itemSplashArrayList;

/**
 * Created by Pradip-PC on 11/17/2016.
 */

@SuppressWarnings("ALL")
public class Splash_Activity2 extends AppCompatActivity {


    private static final String URL = CommonUrl.Main_url+"gallery/getallimages/?page=1&psize=1000";
    private static final String ImgURL = CommonUrl.Main_url+"static/Gallery/";
    private ViewPager viewpager_splash;
    //    private CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
//    private ArrayList<ImageItemSplash> itemSplashArrayList = new ArrayList<>();
    private CustomImageAdapter customImageAdapter;
    private TextView txt_skip;
    private LinearLayout lin_join_now;
    private KProgressHUD loadingProgressDialog;
    String cid;
    int item;
    String pos;
    private ImageView imgarrorback,imgHome;
    private TextView txt_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagesliding);
        viewpager_splash = (ViewPager) findViewById(R.id.viewpager_image);
        imgarrorback= (ImageView) findViewById(R.id.imgarrorback);
        imgHome= (ImageView) findViewById(R.id.imgHome);
        ImageView img_search= (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        txt_title= (TextView) findViewById(R.id.txt_title);
        txt_title.setText("Image Sliding");
        imgHome.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        pos = intent.getStringExtra("position");
        cid = intent.getStringExtra("cid");
        Log.e("position", "------------------" + pos);
        String u = "drawable/noimageavailable.png";
        //new JsonTask().execute();


        customImageAdapter = new CustomImageAdapter(Splash_Activity2.this, itemSplashArrayList);
        item = Integer.parseInt(pos);
        viewpager_splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewpager_splash.setCurrentItem(item, true);
            }
        }, 100);
        viewpager_splash.setAdapter(customImageAdapter);

        Log.e("image length", "" + itemSplashArrayList.size());
//

    }

    private class JsonTask extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(Splash_Activity2.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"gallery/getallimagesbycid/?cid=" + cid + "&page=1&psize=1000");

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    loadingProgressDialog.dismiss();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    itemSplashArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String cid = jsonObject1.getString("CID");
                        String photo = ImgURL + jsonObject1.getString("Photo");
                        String date = jsonObject1.getString("Date");
                        itemSplashArrayList.add(new ImageItemSplash(photo, photo));

                    }
                    customImageAdapter = new CustomImageAdapter(Splash_Activity2.this, itemSplashArrayList);
                    item = Integer.parseInt(pos);
                    viewpager_splash.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewpager_splash.setCurrentItem(item, true);
                        }
                    }, 100);
                    viewpager_splash.setAdapter(customImageAdapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }


        }
    }


}
