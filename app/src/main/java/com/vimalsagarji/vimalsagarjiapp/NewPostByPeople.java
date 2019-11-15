package com.vimalsagarji.vimalsagarjiapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;
import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;
import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;
import com.vimalsagarji.vimalsagarjiapp.util.MarshMallowPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;


@SuppressWarnings("ALL")
public class NewPostByPeople extends AppCompatActivity implements View.OnClickListener {

    private Sharedpreferance sharedpreferance;
    //    private ProgressDialog progressDialog;
    private EditText e_title;
    private EditText e_description;
    private EditText edit_videolink;
    private TextView txt_photo;
    private TextView txt_audio;
    private TextView txt_video;
    private Button btn_add;
    private String picturePath = null;
    private String audioPath = null;
    private String videoPath = null;
    private boolean flag = false;
    private Intent intent;
//    private MarshMallow_Permission permission;
    private ImageView imgarrorback;
    private TextView txt_title;
    private ImageView img_disp;
    KProgressHUD loadingProgressDialog;
    Bitmap thumbnail;
    private MarshMallowPermission permission;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post_bypeople);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newpostbypeople);
//        permission = new MarshMallow_Permission(this);
        permission=new MarshMallowPermission(NewPostByPeople.this);
        setSupportActionBar(toolbar);

        sharedpreferance = new Sharedpreferance(NewPostByPeople.this);

        ImageView imgBack = (ImageView) toolbar.findViewById(R.id.imgarrorback);
        ImageView imgHome = (ImageView) toolbar.findViewById(R.id.imgHome);
        ImageView img_search = (ImageView) toolbar.findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);

        imgHome.setVisibility(View.GONE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        e_title = (EditText) findViewById(R.id.e_title);
        e_description = (EditText) findViewById(R.id.e_description);
        edit_videolink = (EditText) findViewById(R.id.edit_videolink);
        txt_photo = (TextView) findViewById(R.id.txt_photo);
        txt_audio = (TextView) findViewById(R.id.txt_audio);
        txt_video = (TextView) findViewById(R.id.txt_video);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_title = (TextView) findViewById(R.id.txt_title);
        imgarrorback = (ImageView) findViewById(R.id.imgarrorback);
        img_disp = (ImageView) findViewById(R.id.img_disp);
        txt_photo.setOnClickListener(this);
        txt_audio.setOnClickListener(this);
        txt_video.setOnClickListener(this);

        txt_title.setText("New Post");
        imgarrorback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

        }else{
            if (!permission.checkPermissionForCamera()) {
                permission.requestPermissionForCamera();
            }
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            }
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(e_title.getText().toString())) {
                    e_title.setError("Please enter valid title to your post!");
                    e_title.requestFocus();
                } else if (TextUtils.isEmpty(e_description.getText().toString())) {
                    e_description.setError(" Please enter description!");
                    e_description.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(NewPostByPeople.this)) {

                        /*try {
                            String str = "Your string";
                            byte[] array = str.getBytes();
                            byte[] plaintext = array;
                            KeyGenerator keygen = KeyGenerator.getInstance("AES");
                            keygen.init(256);
                            SecretKey key = keygen.generateKey();
                            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                            cipher.init(Cipher.ENCRYPT_MODE, key);
                            byte[] ciphertext = cipher.doFinal(plaintext);
                            byte[] iv = cipher.getIV();

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        }
*/
                        new NewPost().execute();
                    }
                }

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_photo:
                checkPermissionImage();
                break;

            case R.id.txt_audio:
                checkPermissionAudio();
                break;

            case R.id.txt_video:
                checkPermissionVideo();
                break;
        }
    }


    private void checkPermissionImage() {

        selectImage();

    }

    private void checkPermissionAudio() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }
    }

    private void checkPermissionVideo() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            flag = true;
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                flag = true;
                Cursor c = NewPostByPeople.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + imagepath);
                txt_photo.setText("Selected photo : " + imagepath);


                try {
                    decodeFile(picturePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*thumbnail = (BitmapFactory.decodeFile(picturePath));
                img_disp.setVisibility(View.VISIBLE);
                img_disp.setImageBitmap(thumbnail);*/

                Log.e("picturepath", "--------------" + picturePath);
                Log.e("audioPath", "--------------" + audioPath);
                Log.e("videoPath", "--------------" + videoPath);

                //Log.w("path of image from gallery......******************.........", picturePath + "");
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Audio.Media.DATA};
                flag = true;
                Cursor c = NewPostByPeople.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                audioPath = c.getString(columnIndex);
                c.close();
                String audiopath = audioPath.substring(audioPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + audiopath);
                txt_audio.setText("Selected audio : " + audiopath);


                //Log.w("path of image from gallery......******************.........", picturePath + "");
                Log.e("picturepath", "--------------" + picturePath);
                Log.e("audioPath", "--------------" + audioPath);
                Log.e("videoPath", "--------------" + videoPath);
            } else if (requestCode == 3) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Video.Media.DATA};
                flag = true;
                Cursor c = NewPostByPeople.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                videoPath = c.getString(columnIndex);
                c.close();
                String videopath = videoPath.substring(videoPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + videopath);
                txt_video.setText("Selected video : " + videopath);
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                Log.e("picturepath", "--------------" + picturePath);
                Log.e("audioPath", "--------------" + audioPath);
                Log.e("videoPath", "--------------" + videoPath);
            } else if (requestCode == 4) {
                flag = true;

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
//                        profile_image.setImageBitmap(bitmap);
                    img_disp.setVisibility(View.VISIBLE);
                    img_disp.setImageBitmap(bitmap);

                    picturePath = android.os.Environment
                            .getExternalStorageDirectory() + "/VimalsagarjiImage"
                            + File.separator;


//                            + "Phoenix" + File.separator + "default";

                    f.delete();
                    OutputStream outFile = null;

                    File root = android.os.Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath() + "/VimalsagarjiImage" + File.separator);
                    dir.mkdirs();
                    String pic = CommonMethod.getRandomString(30);
                    File file = new File(dir, String.valueOf(pic + ".jpg"));




                    /*picturePath = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator;
//                            + "Phoenix" + File.separator + "default";

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(picturePath, String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                    picturePath = picturePath + String.valueOf(pic) + ".jpg";
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
                        outFile.flush();
                        outFile.close();

                        String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                        Log.e("result", "--------------" + imagepath);
                        txt_photo.setText("Selected photo : " + imagepath);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Log.w("path of image from gallery......******************.........", picturePath + "");
                Log.e("picturepath", "--------------" + picturePath);
                Log.e("audioPath", "--------------" + audioPath);
                Log.e("videoPath", "--------------" + videoPath);
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(NewPostByPeople.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 4);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForCamera()) {
                            permission.requestPermissionForCamera();
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 4);
                        }
                    }


                } else if (options[item].equals("Choose from Gallery")) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForExternalStorage()) {
                            permission.requestPermissionForExternalStorage();
                        } else {

                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 1);

                        }
                    }


                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });

        builder.show();


    }

    private class NewPost extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingProgressDialog = KProgressHUD.create(NewPostByPeople.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait..uploading")
                    .setCancellable(false);
            loadingProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            String title = CommonMethod.encodeEmoji(e_title.getText().toString());
            String description = CommonMethod.encodeEmoji(e_description.getText().toString());
            String VideoLink = CommonMethod.encodeEmoji(edit_videolink.getText().toString());

            Log.e("title", "----------" + title);
            Log.e("description", "----------" + description);
            Log.e("VideoLink", "----------" + VideoLink);
            String Audio = "no audio";
            String Video = "no video";
            String Photo = "no photo";
            String Videolink = "";

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonUrl.Main_url + "bypeople/addpost/");
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                multipartEntity.addPart("uid", new StringBody(sharedpreferance.getId()));
                multipartEntity.addPart("Title", new StringBody(title));
                multipartEntity.addPart("Post", new StringBody(description));
                multipartEntity.addPart("VideoLink", new StringBody(VideoLink));

                if (picturePath == null) {
                    Log.e("if call", "-----------");
                } else {
                    Log.e(" else call", "-----------");
                    File file1 = new File(picturePath);
                    FileBody fileBody1 = new FileBody(file1);
                    multipartEntity.addPart("Photo", fileBody1);
                }


                if (audioPath == null) {
                    Log.e("if call", "-----------");
                } else {
                    File file2 = new File(audioPath);
                    FileBody fileBody2 = new FileBody(file2);
                    multipartEntity.addPart("Audio", fileBody2);
                }


                if (videoPath == null) {
                    Log.e("if call", "-----------");

                } else {

                    File file3 = new File(videoPath);
                    FileBody fileBody3 = new FileBody(file3);
                    multipartEntity.addPart("Video", fileBody3);
                }

                httpPost.setEntity(multipartEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                    responseJSON = s.toString();
                }


            } catch (Exception e) {
                Log.e("exception", "------------" + e.toString());
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(NewPostByPeople.this, "Thank you! Your post will be live after approval of admin!.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(NewPostByPeople.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                } else {
                    Toast.makeText(NewPostByPeople.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadingProgressDialog.dismiss();
        }
    }

    public void decodeFile(String filePath) throws IOException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }


        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        thumbnail = BitmapFactory.decodeFile(filePath, o2);

        img_disp.setVisibility(View.VISIBLE);
        img_disp.setImageBitmap(thumbnail);
        OutputStream outFile = null;
        File file = new File(picturePath);
        outFile = new FileOutputStream(file);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
        outFile.flush();
        outFile.close();

    }
}
