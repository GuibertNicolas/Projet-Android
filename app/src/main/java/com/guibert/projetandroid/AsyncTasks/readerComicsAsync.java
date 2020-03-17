package com.guibert.projetandroid.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.guibert.projetandroid.Adapters.ListComicViewAdapter;
import com.guibert.projetandroid.Data.Comic;

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

public class readerComicsAsync extends AsyncTask<String, Comic, ArrayList> {
    private ArrayList<Comic> vComic = new ArrayList<Comic>();
    private int offset;
    private ListComicViewAdapter adpt;
    private ProgressBar progress;
    private LinearLayout layout;
    private int nbComics;

    public readerComicsAsync(ListComicViewAdapter a, int off, ProgressBar p, LinearLayout l, int c){
        this.adpt = a;
        this.offset = off;
        this.progress = p;
        this.layout = l;
        this.nbComics = c;
    }
    @Override
    protected ArrayList<Comic> doInBackground(String... strings) {
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
                        //recuperation de la list des comics
                        int id = results.getJSONObject(i).getInt("id");
                        String name = results.getJSONObject(i).getString("title");
                        String description = results.getJSONObject(i).getString("description");
                        JSONObject thumbnail = results.getJSONObject(i).getJSONObject("thumbnail");
                        String path = thumbnail.getString("path");
                        String ext = thumbnail.getString("extension");
                        JSONObject characters = results.getJSONObject(i).getJSONObject("characters");
                        String charactersUri = characters.getString("collectionURI");
                        int nbCharacters = characters.getInt("available");
                        int nbPages = results.getJSONObject(i).getInt("pageCount");
                        String purchase = results.getJSONObject(i).getJSONArray("urls").getJSONObject(0).getString("url");
                        String format = results.getJSONObject(i).getString("format");

                        //Création du comic
                        Comic c = new Comic(id, name, description,path+"/portrait_xlarge."+ext, charactersUri, nbCharacters, nbPages, purchase, format);
                        vComic.add(c);
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
        Log.i("guibert", "fin");
        return vComic;
    }
    @Override
    protected void onPostExecute(ArrayList results) {
        for (int i = 0; i < results.size(); i++){
            adpt.add((Comic) results.get(i));
        }
        //on cache la bar de progression
        progress.setVisibility(View.GONE);
        //afficher le bouton more comics i nécéssaire
        if (offset + 100 < nbComics) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //on affiche la bar de progression
        progress.setVisibility(View.VISIBLE);
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