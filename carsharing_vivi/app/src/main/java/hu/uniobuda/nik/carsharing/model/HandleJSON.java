package hu.uniobuda.nik.carsharing.model;

/**
 * Created by igyartoimre on 2017. 05. 06..
 */

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandleJSON {
    private Integer distance =Integer.MAX_VALUE;
    private String urlString = null;
    public volatile boolean parsingComplete = true;

    public Integer getDistance() {
        return distance;
    }

    public HandleJSON(String url) {
        this.urlString = url;
    }


    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {
        try {
            JSONObject reader = new JSONObject(in);
            JSONArray rows = reader.getJSONArray("rows");
            JSONObject row =  rows.getJSONObject(0);

            JSONArray elements = row.getJSONArray("elements");

            for(int i=0;i<elements.length();i++) {
                JSONObject element = elements.getJSONObject(i);
                JSONObject Distance = element.getJSONObject("distance");
                if(distance<Distance.getInt("value"))
                    distance= Distance.getInt("value");
            }

            parsingComplete = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void fetchJSON() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    String data = convertStreamToString(stream);
                    readAndParseJSON(data);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
