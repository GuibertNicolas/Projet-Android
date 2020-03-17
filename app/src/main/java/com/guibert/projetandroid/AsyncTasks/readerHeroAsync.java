package com.guibert.projetandroid.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.guibert.projetandroid.Adapters.ListHeroInComicAdapter;
import com.guibert.projetandroid.Adapters.ListHeroViewAdapter;
import com.guibert.projetandroid.Data.Hero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class readerHeroAsync extends AsyncTask<String, Integer, ArrayList> {
    private ArrayList<Hero> vHero = new ArrayList<>();
    private int offset;
    private boolean isHeroInComic; //
    private ListHeroViewAdapter adpt;
    private ListHeroInComicAdapter adptHC;
    private ProgressBar progress;
    private LinearLayout layout;
    private int nbHeros;

    //get all heros
    public readerHeroAsync(ListHeroViewAdapter adpt, int off, ProgressBar p, LinearLayout l, int c){
        this.adpt = adpt;
        this.offset = off;
        this.progress = p;
        this.layout = l;
        this.nbHeros = c;
        this.isHeroInComic = false;
    }

    //get the heros only for one comic
    public readerHeroAsync(ListHeroInComicAdapter adpt, int off){
        this.adptHC = adpt;
        this.offset = off;
        this.isHeroInComic = true;
    }
    @Override
    protected ArrayList<Hero> doInBackground(String... strings) {
        URL url;
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return vHero;
    }
    @Override
    protected void onPostExecute(ArrayList results) {
        //pour la liste des heros dans une  comics
        if (isHeroInComic) {
            for (int i = 0; i < results.size(); i++){
                adptHC.add((Hero) results.get(i));
            }
        //pour la liste principale
        } else {
            for (int i = 0; i < results.size(); i++){
                adpt.add((Hero) results.get(i));
            }
            //on cache la bar de progression
            progress.setVisibility(View.GONE);
            //afficher le bouton more comics i nécéssaire
            if (offset + 100 < nbHeros) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //on affiche la bar de progression
        if (!isHeroInComic) progress.setVisibility(View.VISIBLE);
    }

    private static String readStream(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
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
