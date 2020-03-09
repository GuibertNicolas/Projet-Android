package com.guibert.projetandroid;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

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

public class readerHeroAsync extends AsyncTask<String, Integer, ArrayList> {
    private ArrayList<Hero> vHero = new ArrayList<Hero>();
    private int offset;
    private boolean isHeroInComic;
    private ListHeroViewAdapter adpt;
    private ListHeroInComicAdapter adptHC;
    public readerHeroAsync(ListHeroViewAdapter adpt, int off){
        this.adpt = adpt;
        this.offset = off;
        this.isHeroInComic = false;
    }

    public readerHeroAsync(ListHeroInComicAdapter adpt, int off){
        this.adptHC = adpt;
        this.offset = off;
        this.isHeroInComic = true;
    }
    @Override
    protected ArrayList<Hero> doInBackground(String... strings) {
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
                        JSONArray results = data.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++){
                            int id = results.getJSONObject(i).getInt("id");
                            String name = results.getJSONObject(i).getString("name");
                            JSONObject thumbnail = results.getJSONObject(i).getJSONObject("thumbnail");
                            String path = thumbnail.getString("path");
                            String ext = thumbnail.getString("extension");
                            String img = path+"/standard_large."+ext;
                            JSONObject comics = results.getJSONObject(i).getJSONObject("comics");
                            int nbComics = comics.getInt("available");
                            String comicsUrl = comics.getString("collectionURI");
                            //recuperation de la list des comics
                            String description = results.getJSONObject(i).getString("description");
                            Hero h = new Hero(id, name, img, nbComics, description, comicsUrl);
                            vHero.add(h);
                            //Log.i("guibert-json", id + " " + name + " " + img + " " + nbComics + "\n");
                        }
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
            return vHero;
    }
    @Override
    protected void onPostExecute(ArrayList results) {
        if (isHeroInComic) {
            for (int i = 0; i < results.size(); i++){
                adptHC.add((Hero) results.get(i));
            }
        } else {
            for (int i = 0; i < results.size(); i++){
                adpt.add((Hero) results.get(i));
            }
        }
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
