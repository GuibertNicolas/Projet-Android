package com.guibert.projetandroid.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.guibert.projetandroid.Adapters.ListHeroInComicAdapter;
import com.guibert.projetandroid.Adapters.ListHeroViewAdapter;
import com.guibert.projetandroid.Hero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class nbHeroAsync extends AsyncTask<String, Integer, Integer> {
    private int offset;
    private int nbResults;

    public nbHeroAsync(int off){
        this.offset = off;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        URL url = null;
        try {
            url = new URL(strings[0]+"&offset="+offset);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String s = readStream(in);
                try {
                    JSONObject json = new JSONObject(s);
                    JSONObject data = json.getJSONObject("data");
                    nbResults = data.getInt("total") ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } finally {
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nbResults;
    }
    @Override
    protected void onPostExecute(Integer results) {

    }


    private static String readStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e("GUIBERT", "IOException", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e("GUIBERT", "IOException", e);
            }
        }
        return sb.toString();
    }
}
