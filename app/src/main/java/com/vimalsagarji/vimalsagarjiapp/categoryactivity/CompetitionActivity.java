package com.vimalsagarji.vimalsagarjiapp.categoryactivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vimalsagarji.vimalsagarjiapp.JSONParser;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.activity.mainactivity.SearchActivity;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.today_week_month_year.CompetitionList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class CompetitionActivity extends AppCompatActivity {

    private TextView grid_txtTitle;
    //    private KProgressHUD loadingProgressDialog;
    private static final String TAG = CompetitionActivity.class.getSimpleName();
    private static final String URL = CommonUrl.Main_url + "competition/getallcategory/?page=1&psize=1000";
    private static final String ImgURL = CommonUrl.Main_url + "static/competitioncategory/";
    private final ArrayList<String> listID = new ArrayList<String>();
    private final ArrayList<String> listName = new ArrayList<String>();
    private final ArrayList<String> listIcon = new ArrayList<String>();
    private String strImageUrl = "";
    String strImgExtention = "jpg";
    TextView txtTitle;
    private String cid;
    private RelativeLayout rel_compe;
    private TextView txt_nodata_today;
    Sharedpreferance sharedpreferance;
    private ProgressBar progressbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferance = new Sharedpreferance(CompetitionActivity.this);
        if (savedInstanceState == null) {
            setContentView(R.layout.content_competition);
        } else if (savedInstanceState != null) {
            onResume();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_competition);
        setSupportActionBar(toolbar);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        ImageView imgarrorback = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.VISIBLE);

        TextView txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        txt_title.setText("Competition");
        rel_compe = (RelativeLayout) findViewById(R.id.rel_compe);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
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

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompetitionActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


//        Temparory comment
        /*
        if (CommonMethod.isInternetConnected(CompetitionActivity.this)) {
            JsonTask jsonTask = new JsonTask();
            jsonTask.execute(URL, ImgURL);
        } else {
            final Snackbar snackbar = Snackbar
                    .make(rel_compe, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        }
        */


    }

    private class JsonTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
         /*   loadingProgressDialog = KProgressHUD.create(CompetitionAllActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(false);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            String strUrl = params[0];
            String ImageURL = params[1];
            try {
                JSONObject jsonObject = JSONParser.getJsonFromUrl(strUrl);
                JSONArray array = jsonObject.getJSONArray("data");
                for (int j = 0; j < array.length(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    cid = object.getString("ID");
                    listID.add(cid);
                    String strName = object.getString("Name");
                    listName.add(strName);
                    Log.d(TAG, "list Name data:" + listName);
                    String strCategoryIcon = object.getString("CategoryIcon");

                    Log.d(TAG, "list Icon Image:" + listIcon);
                    boolean checkJpg = strCategoryIcon.contains("jpg");

                    strImageUrl = ImageURL + strCategoryIcon;
                    listIcon.add(strImageUrl);
                }

                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);
            GridView gridView = (GridView) findViewById(R.id.grid_audio);
            if (gridView != null) {

                CustomAdpter customAdpter = new CustomAdpter(CompetitionActivity.this, listName);
                if (customAdpter.getCount() != 0) {
                    gridView.setAdapter(customAdpter);
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            grid_txtTitle = (TextView) findViewById(R.id.grid_txtTitle);
                            String strCompetitionTitle = grid_txtTitle.getText().toString();
//                            if (sharedpreferance.getId().equalsIgnoreCase("")) {
//                                Snackbar.make(view, R.string.notregister, Snackbar.LENGTH_SHORT).show();
//                            }else{
                            Intent intent = new Intent(CompetitionActivity.this, CompetitionList.class);
                            intent.putExtra("categoryID", listID.get(position));
                            Log.e("category id", "--------------------" + listID.get(position));
                            intent.putExtra("listTitle", listName.get(position));
                            Log.e("Title", "----------------------" + strCompetitionTitle);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                            }
                        }
                    });
                } else {
                    txt_nodata_today.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                }


            } else {
//                Toast.makeText(CompetitionAllActivity.this, "Gridview is null", Toast.LENGTH_SHORT).show();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.grid_txtTitle = (TextView) convertView.findViewById(R.id.grid_txtTitle);
                holder.grid_img = (ImageView) convertView.findViewById(R.id.grid_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.grid_txtTitle.setText(items.get(position));

            Picasso.with(CompetitionActivity.this).load(listIcon.get(position).replaceAll(" ", "%20")).placeholder(R.drawable.loader).resize(0, 200).error(R.drawable.loader).into(holder.grid_img);

            return convertView;

        }


        private class ViewHolder {
            TextView grid_txtTitle;
            ImageView grid_img;

        }
    }

}
