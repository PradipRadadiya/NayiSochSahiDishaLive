package com.vimalsagarji.vimalsagarjiapp.today_week_month_year;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vimalsagarji.vimalsagarjiapp.CustomImageAdapter;
import com.vimalsagarji.vimalsagarjiapp.ImageItemSplash;
import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.ReadMoreOption;
import com.vimalsagarji.vimalsagarjiapp.Splash_Activity2;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.vimalsagarji.vimalsagarjiapp.fcm.MyFirebaseMessagingService.questionid;

@SuppressWarnings("ALL")
public class GalleryCategory extends AppCompatActivity {

    static final String TAG = GalleryCategory.class.getSimpleName();
    private static final String URL = CommonUrl.Main_url + "gallery/getallimages/?page=1&psize=1000";
    private static final String ImgURL = CommonUrl.Main_url + "static/Gallery/";
    private final ArrayList<String> listIcon = new ArrayList<String>();
    String strImageUrl = "";
    static Bitmap bitmap = null;
    private GridView gridView;
    //    private KProgressHUD loadingProgressDialog;
    private RelativeLayout rel_gallary;
    private String cid, catname;
    private TextView txt_nodata_today;
    public static ArrayList<ImageItemSplash> itemSplashArrayList = new ArrayList<>();
    private CustomImageAdapter customImageAdapter;
    private ProgressBar progressbar;
    private TextView etGalleryTitle, txt_gal_title;
    private NestedScrollView nestedScrollview;
    LinearLayout img_share;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_gallery_category);

        nestedScrollview = findViewById(R.id.nestedScrollview);
        customImageAdapter = new CustomImageAdapter(GalleryCategory.this, itemSplashArrayList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_gallery);
        setSupportActionBar(toolbar);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        gridView = (GridView) findViewById(R.id.gallery_gridview);
        gridView.setNestedScrollingEnabled(true);
        txt_nodata_today = (TextView) findViewById(R.id.txt_nodata_today);
        rel_gallary = (RelativeLayout) findViewById(R.id.rel_gallary);
        etGalleryTitle = (TextView) findViewById(R.id.etGalleryTitle);
        txt_gal_title = (TextView) findViewById(R.id.gal_title);
        ImageView imgarrorback = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        TextView txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        img_share = findViewById(R.id.img_share);

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


        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        Intent intent = getIntent();
        if (appLinkData != null) {
            Log.e("appLinkData", "--------------" + appLinkData.getQueryParameter("key"));

            cid = appLinkData.getQueryParameter("key");
            catname = appLinkData.getQueryParameter("cname");
            txt_title.setText(CommonMethod.decodeEmoji(catname));
            txt_gal_title.setText(CommonMethod.decodeEmoji(catname));

        } else {
            cid = getIntent().getStringExtra("cid");
            catname = getIntent().getStringExtra("catname");
            txt_title.setText(CommonMethod.decodeEmoji(catname));
            txt_gal_title.setText(CommonMethod.decodeEmoji(catname));
        }




       /* if (CommonMethod.isInternetConnected(GalleryCategory.this)) {
            JsonTask jsonTask = new JsonTask();
            jsonTask.execute(URL, ImgURL);
        }*/

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String sAux = "\n Gallery \n" + CommonMethod.decodeEmoji(catname) + "\n\n" + CommonUrl.Main_url + "gallerydetail?key=" + cid +"&cname=" + CommonMethod.decodeEmoji(catname).replaceAll(" ","%20") + getResources().getString(R.string.app_name) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    intent.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(intent, "Choose One"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        if (cid.equalsIgnoreCase("e_alliamgeid")) {
            Log.e("event click", "-----------");
            etGalleryTitle.setText("Gallery");
            if (CommonMethod.isInternetConnected(GalleryCategory.this)) {
                new EventImage().execute();
            } else {
                final Snackbar snackbar = Snackbar
                        .make(rel_gallary, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
            }
        } else if (cid.equalsIgnoreCase("bypeopleidid")) {
            Log.e("bypeople click", "-----------");
            etGalleryTitle.setText("Gallery");
            if (CommonMethod.isInternetConnected(GalleryCategory.this)) {
                new ByPeopleImage().execute();
            } else {
                final Snackbar snackbar = Snackbar
                        .make(rel_gallary, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
            }

        } else {
            if (CommonMethod.isInternetConnected(GalleryCategory.this)) {

                new getCategoryDescription().execute();
                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(URL, ImgURL);
            } else {
                final Snackbar snackbar = Snackbar
                        .make(rel_gallary, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
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

    }

    private class JsonTask extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(GalleryCategory.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url + "gallery/getallimagesbycid/?cid=" + cid + "&page=1&psize=1000");

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    itemSplashArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String cid = jsonObject1.getString("CID");
                        String photo = ImgURL + jsonObject1.getString("Photo");
                        String date = jsonObject1.getString("Date");
                        listIcon.add(photo);
                        itemSplashArrayList.add(new ImageItemSplash(photo, photo));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
          /*  if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/
            progressbar.setVisibility(View.GONE);


            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(GalleryCategory.this, listIcon);
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);

                } else {
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);

                }
            }

        }
    }

    private class ByPeopleImage extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
           /* loadingProgressDialog = KProgressHUD.create(GalleryCategory.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url + "bypeople/getallappposts/?page=1&psize=1000");

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    itemSplashArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        if (jsonObject1.getString("Photo").equalsIgnoreCase("null")) {

                        } else {

                            String photo = CommonUrl.Main_url + "static/bypeopleimage/" + jsonObject1.getString("Photo");
                            itemSplashArrayList.add(new ImageItemSplash(photo, photo));
                            listIcon.add(photo);
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressbar.setVisibility(View.GONE);
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }
*/

            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(GalleryCategory.this, listIcon);
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);

                } else {
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);

                }
            }

        }
    }

    private class EventImage extends AsyncTask<String, Void, String> {

        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
          /*  loadingProgressDialog = KProgressHUD.create(GalleryCategory.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please Wait")
                    .setCancellable(true);
            loadingProgressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = CommonMethod.getStringResponse(CommonUrl.Main_url + "event/geteventsbycategoryyear/?page=1&psize=1000");

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    loadingProgressDialog.dismiss();
                    progressbar.setVisibility(View.GONE);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    itemSplashArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        if (jsonObject1.getString("Photo").equalsIgnoreCase("null")) {

                        } else {
                            String photo = jsonObject1.getString("Photo");
                            String[] photoarray = photo.split(",");

                            for (int j = 0; j < photoarray.length; j++) {
                                Log.e("photo array ", "--------------" + photoarray[j]);
                                itemSplashArrayList.add(new ImageItemSplash(CommonUrl.Main_url + "static/eventimage/" + photoarray[j], CommonUrl.Main_url + "static/eventimage/" + photoarray[j]));
                                listIcon.add(CommonUrl.Main_url + "static/eventimage/" + photoarray[j]);
                            }

                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (loadingProgressDialog != null) {
                loadingProgressDialog.dismiss();
            }*/

            progressbar.setVisibility(View.GONE);

            if (gridView != null) {
                CustomAdpter customAdpter = new CustomAdpter(GalleryCategory.this, listIcon);
                if (customAdpter.getCount() != 0) {
                    gridView.setVisibility(View.VISIBLE);
                    txt_nodata_today.setVisibility(View.GONE);
                    gridView.setAdapter(customAdpter);

                } else {
                    gridView.setVisibility(View.GONE);
                    txt_nodata_today.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class getCategoryDescription extends AsyncTask<String, Void, String> {
        String jsonresponse = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("gid", cid));
            jsonresponse = CommonMethod.postStringResponse(CommonUrl.Main_url + "gallery/getdescription", nameValuePairs, GalleryCategory.this);

            return jsonresponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject object = jsonArray.getJSONObject(0);
                    ReadMoreOption readMoreOption = new ReadMoreOption.Builder(GalleryCategory.this)
                            .textLength(150)
                            .moreLabel("Read More")
                            .lessLabel("Read Less")
                            .moreLabelColor(Color.RED)
                            .lessLabelColor(Color.BLUE)
                            .labelUnderLine(true)
                            .build();

                    readMoreOption.addReadMoreTo(etGalleryTitle, CommonMethod.decodeEmoji(object.getString("Description")));


//                    etGalleryTitle.setText(CommonMethod.decodeEmoji(object.getString("Description")));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

//            etGalleryTitle.setText("");
        }

    }

    @SuppressWarnings("NullableProblems")
    public class CustomAdpter extends ArrayAdapter<String> {

        final List<String> items;
        final Context context;
        final int resource;

        public CustomAdpter(Context context, List<String> items) {
            super(context, R.layout.custom_gallery_gridview, items);
            this.context = context;
            this.resource = R.layout.custom_gallery_gridview;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, null, false);

                holder.grid_img = (ImageView) convertView.findViewById(R.id.grid_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Log.e("gallery image", "------------" + items.get(position));

//            Picasso.with(GalleryCategory.this)
//                    .load(items.get(position)
//                            .replaceAll(" ", "%20"))
//                    .placeholder(R.drawable.loader)
//                    .resize(0, 200)
////                    .error(R.drawable.loader)
//                    .into(holder.grid_img);


            Glide.with(GalleryCategory.this).load(items.get(position)
                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loader).into(holder.grid_img);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(GalleryCategory.this, Splash_Activity2.class);
                    intent.putExtra("imagePath", listIcon.get(position));
                    String pos = String.valueOf(position);
                    intent.putExtra("position", pos);
                    intent.putExtra("cid", cid);
                    Log.e("position", "------------" + position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            return convertView;

        }

        private class ViewHolder {
            ImageView grid_img;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
//        listIcon.clear();

    }

}

