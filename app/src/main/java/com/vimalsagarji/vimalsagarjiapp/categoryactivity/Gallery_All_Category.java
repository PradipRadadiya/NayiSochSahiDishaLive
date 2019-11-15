package com.vimalsagarji.vimalsagarjiapp.categoryactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.EventGalleryAlbumActivity;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.GalleryCategory;
import com.vimalsagarji.vimalsagarjiapp.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Gallery_All_Category extends AppCompatActivity {
    private Toolbar toolbar_home;
    TextView grid_txtTitle;
    //ImageView imgarrorback,imghomeicon;
//    private KProgressHUD loadingProgressDialog;
    static final String TAG = Gallery_All_Category.class.getSimpleName();
    static String URL = Constant.GET_ALL_CATEGORY_AUDIO;
    static String ImgURL = Constant.GET_AUDIO_IMAGE_URL;
    private final ArrayList<String> listName = new ArrayList<String>();
    private final ArrayList<String> listIcon = new ArrayList<String>();
    private final ArrayList<String> listID = new ArrayList<String>();
    String strImageUrl = "";
    private String strName;
    String title = "";
    private RelativeLayout rel_audio;
    private GridView gridView;
    private TextView txt_nodata_today;
    private EditText etAudioCategory;
    private ProgressBar progressbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_audio_category);


        toolbar_home = (Toolbar) findViewById(R.id.toolbar_audio);
        setSupportActionBar(toolbar_home);
        progressbar= (ProgressBar) findViewById(R.id.progressbar);
        TextView txt_title = (TextView) toolbar_home.findViewById(R.id.txt_title);
        txt_title.setText("Gallery");
        ImageView imgarrorback = (ImageView) toolbar_home.findViewById(R.id.imgarrorback);
        ImageView img_search = (ImageView) toolbar_home.findViewById(R.id.img_search);

        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        ImageView imgHome = (ImageView) toolbar_home.findViewById(R.id.imgHome);
        rel_audio = (RelativeLayout) findViewById(R.id.rel_audio);
        etAudioCategory = (EditText) findViewById(R.id.etAudioCategory);
        etAudioCategory.setText("Gallery Category");
        etAudioCategory.setTextColor(getResources().getColor(R.color.black));

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Gallery_All_Category.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        if (CommonMethod.isInternetConnected(Gallery_All_Category.this)) {
            JsonTask jsonTask = new JsonTask();
            jsonTask.execute();
        } else {
            final Snackbar snackbar = Snackbar
                    .make(rel_audio, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        }


    }


    private class JsonTask extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
           /* loadingProgressDialog = KProgressHUD.create(Gallery_All_Category.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url+"gallery/getallcategory");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("respone", "------------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strID = object.getString("ID");
                        listID.add(strID);
                        strName = object.getString("Name");
                        listName.add(strName);
                        String strCategoryIcon = object.getString("CategoryIcon");
                        listIcon.add(CommonUrl.Main_url+"static/gallerycategory/" + strCategoryIcon);
                    }
                    listID.add("e_alliamgeid");
                    listName.add("Event");
                    listIcon.add(CommonUrl.Main_url+"static/Gallery/event.png");

                    listID.add("bypeopleidid");
                    listName.add("ByPeople");
                    listIcon.add(CommonUrl.Main_url+"static/Gallery/bypeople.png");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
           progressbar.setVisibility(View.GONE);
            gridView = (GridView) findViewById(R.id.grid_audio);
            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(Gallery_All_Category.this, listName);
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);
                    customAdpter.notifyDataSetChanged();

                } else {
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
                }


            } else {
//                Toast.makeText(Gallery_All_Category.this, "Gridview is null", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<String> {

        final List<String> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<String> items) {
            super(context, R.layout.custom_audio_gridview, items);
            this.context = context;
            this.resource = R.layout.custom_audio_gridview;
            this.items = items;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                //holder.txt_ID = (TextView) convertView.findViewById(R.id.txtID);
                holder.grid_txtTitle = (TextView) convertView.findViewById(R.id.grid_txtTitle);
                holder.grid_img = (ImageView) convertView.findViewById(R.id.grid_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // holder.txt_ID.setText(items.get(position));
            holder.grid_txtTitle.setText(CommonMethod.decodeEmoji(items.get(position)));


//            Picasso.with(Gallery_All_Category.this).load(listIcon.get(position).replaceAll(" ", "%20")).placeholder(R.drawable.loader).error(R.drawable.bypeople_error).into(holder.grid_img);

            Glide.with(Gallery_All_Category.this).load(listIcon.get(position)
                    .replaceAll(" ", "%20")).placeholder(R.drawable.loader).dontAnimate().into(holder.grid_img);


            Log.e("galerry category","--------------"+listIcon.get(position)
                    .replaceAll(" ", "%20"));


            holder.grid_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listID.get(position).equalsIgnoreCase("e_alliamgeid")){
                        Intent intent = new Intent(Gallery_All_Category.this, EventGalleryAlbumActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else {
                        Intent intent = new Intent(Gallery_All_Category.this, GalleryCategory.class);
                        intent.putExtra("cid", listID.get(position));
                        intent.putExtra("catname", listName.get(position));
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
            return convertView;

        }

        private class ViewHolder {
            TextView grid_txtTitle;
            ImageView grid_img;

        }
    }
}
