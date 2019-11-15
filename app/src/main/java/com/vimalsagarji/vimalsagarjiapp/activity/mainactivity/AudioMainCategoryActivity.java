package com.vimalsagarji.vimalsagarjiapp.activity.mainactivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.adpter.CategoryAllAdapter;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.model.MainCategoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AudioMainCategoryActivity extends AppCompatActivity {
    private RecyclerView recycleview_category;
    private GridLayoutManager linearLayoutManager;
    CategoryAllAdapter audioAllAdapter;
    private ArrayList<MainCategoryItem> audioAllItems = new ArrayList<>();

    private ProgressBar progressbar;
    private TextView txt_nodata_today;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        toolbarClick();

        txt_nodata_today= (TextView) findViewById(R.id.txt_nodata_today);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        linearLayoutManager = new GridLayoutManager(AudioMainCategoryActivity.this,2);
        recycleview_category = (RecyclerView) findViewById(R.id.recycleview_category);
        recycleview_category.setLayoutManager(linearLayoutManager);

        audioAllAdapter = new CategoryAllAdapter(AudioMainCategoryActivity.this, audioAllItems);
        recycleview_category.setAdapter(audioAllAdapter);


        if (CommonMethod.isInternetConnected(AudioMainCategoryActivity.this)) {
            new GetAllCategory().execute();
        } else {
            //internet not connected
            Toast.makeText(this, R.string.internet, Toast.LENGTH_LONG).show();
        }


    }

    private void toolbarClick() {
        ImageView imgarrorback;
        TextView txt_title;
        ImageView img_search;

        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_search = (ImageView) findViewById(R.id.img_search);
        txt_title.setText("Audios");
        img_search.setVisibility(View.GONE);
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class GetAllCategory extends AsyncTask<String, Void, String> {
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            responseJson = CommonMethod.getStringResponse(CommonUrl.Main_url+"audio/getallcategory");
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String CAT_ID = "audio";
                    String ID = object.getString("ID");
                    String Name = object.getString("Name");
                    String CategoryIcon = CommonUrl.Main_url+"static/audiocategory/"+object.getString("CategoryIcon");
                    audioAllItems.add(new MainCategoryItem(CAT_ID,ID,Name,CategoryIcon));
                }

                audioAllItems.add(new MainCategoryItem("audio","e_alliamgeid","Event",CommonUrl.Main_url+"static/Gallery/event.png"));

                audioAllAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            audioAllAdapter.notifyDataSetChanged();
            progressbar.setVisibility(View.GONE);

            if (audioAllItems.isEmpty()) {

                txt_nodata_today.setVisibility(View.VISIBLE);
            }

        }


    }
}
