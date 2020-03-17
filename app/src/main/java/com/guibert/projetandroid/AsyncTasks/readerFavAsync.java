package com.guibert.projetandroid.AsyncTasks;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.guibert.projetandroid.Adapters.ListComicViewAdapter;
import com.guibert.projetandroid.Adapters.ListFavComicAdapter;
import com.guibert.projetandroid.Data.Comic;
import com.guibert.projetandroid.Data.MyDatabase;

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

public class readerFavAsync extends AsyncTask<String, Comic, ArrayList> {
    private ArrayList<Comic> vComic = new ArrayList<Comic>();
    private ListFavComicAdapter adpt;
    private MyDatabase mydb;

    public readerFavAsync(ListFavComicAdapter a, MyDatabase mydb){
        this.adpt = a;
        this.mydb = mydb;
    }
    @Override
    protected ArrayList<Comic> doInBackground(String... strings) {
        if (mydb.readData().size() > 0) {
            vComic = mydb.readData();
            return vComic;
        }
        return new ArrayList<Comic>();
    }
    @Override
    protected void onPostExecute(ArrayList results) {
        for (int i = 0; i < results.size(); i++){
            adpt.add((Comic) results.get(i));
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}