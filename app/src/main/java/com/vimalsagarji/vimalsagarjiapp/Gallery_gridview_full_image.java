package com.vimalsagarji.vimalsagarjiapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("ALL")
public class Gallery_gridview_full_image extends AppCompatActivity {

    //    ImageView grid_fullImage;
    private RelativeLayout rel_gallary_grid;
    private SubsamplingScaleImageView imageView;
    private Bitmap myBitmap;
    private KProgressHUD loadingProgressDialog;
    private String image;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_gallery_gridview_full_image);
        imageView = (SubsamplingScaleImageView) findViewById(R.id.grid_fullImage);
        rel_gallary_grid = (RelativeLayout) findViewById(R.id.rel_gallry_grid);
        image = getIntent().getStringExtra("imagePath");

        new ImageLoad().execute();

    }


    private class ImageLoad extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(Gallery_gridview_full_image.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(image);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            imageView.setImage(ImageSource.bitmap(myBitmap));
            loadingProgressDialog.dismiss();
        }
    }

}
