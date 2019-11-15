package com.vimalsagarji.vimalsagarjiapp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

@SuppressWarnings("ALL")
public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj;
    static String json = "";


    public static JSONObject getJsonFromUrl(String url) {

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            is = httpEntity.getContent();


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");

            }
            is.close();
            json = sb.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jObj = new JSONObject(json);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jObj;
    }
}
