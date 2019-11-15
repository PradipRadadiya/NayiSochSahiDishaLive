package com.vimalsagarji.vimalsagarjiapp.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.util.EntityUtils;

@SuppressWarnings("ALL")
public class CommonMethod {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getStringResponse(String url) {
        String result = "";
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer;
        URL jsonurl;
        try {
            jsonurl = new URL(url);
            Log.e("URL", jsonurl.toString());
            stringBuffer = new StringBuffer("");
            URLConnection connection = jsonurl.openConnection();
            connection.setConnectTimeout(500000);
            connection.setReadTimeout(50000);
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
            result = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    public static String postStringResponse(String url, ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs, Context ctx) {
        HttpClient httpclient = new DefaultHttpClient();


        HttpPost httppost = new HttpPost(url);
        Log.e("url postStringResponse", "" + url);

        String responseStr = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            responseStr = EntityUtils.toString(response.getEntity());

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        }

        return responseStr;
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static Date convert_date(String date) {
        Date mDate = null;
        try {
            mDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate;
    }

    public static String giveDate(String time) {
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }
    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    public static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static Spanned stripHtml(String html) {
        if (!TextUtils.isEmpty(html)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
            } else {
                return Html.fromHtml(html);
            }
        }
        return null;
    }

    public static String encodeEmoji (String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }


    public static String decodeEmoji (String message) {
        String myString= null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }


   /* public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return secret = new SecretKeySpec(password.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        *//* Encrypt the message. *//*
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        *//* Decrypt the message, given derived encContentValues and initialization vector. *//*
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }
*/


    //character capital of white space
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }


}
