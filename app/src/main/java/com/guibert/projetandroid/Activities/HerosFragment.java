package com.guibert.projetandroid.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.guibert.projetandroid.Adapters.ListHeroViewAdapter;
import com.guibert.projetandroid.AsyncTasks.nbHeroAsync;
import com.guibert.projetandroid.AsyncTasks.readerComicsAsync;
import com.guibert.projetandroid.AsyncTasks.readerHeroAsync;
import com.guibert.projetandroid.Hero;
import com.guibert.projetandroid.R;
import com.guibert.projetandroid.ShakeDetector;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class HerosFragment extends Fragment {
    private nbHeroAsync asyncTask;
    private ArrayList<Hero> vHero = new ArrayList<Hero>();
    private ListHeroViewAdapter adaptateur;
    private ListView lv;
    private int nbResults;
    private int offset;
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private View root;
    private ProgressBar progress;

    public HerosFragment() {
    }

    public static HerosFragment newInstance() {
        return (new HerosFragment());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vHero = new ArrayList<>();
        root =  inflater.inflate(R.layout.fragment_heros, container, false);
        adaptateur = new ListHeroViewAdapter(getContext(), vHero);
        lv = root.findViewById(R.id.listhero);
        lv.setAdapter(adaptateur);
        progress = root.findViewById(R.id.simpleProgressBarHerofragment);
        progress.setVisibility(View.GONE);
        offset = 0;

        LinearLayout linearLayout = root.findViewById(R.id.fragment_news_rootview);
        SharedPreferences prefs = getActivity().getSharedPreferences("colorstyle", getContext().MODE_PRIVATE );
        if (prefs.getBoolean("nightMode", false)) {
            linearLayout.setBackgroundColor(getActivity().getColor(R.color.colorBackGroundNigth));
        } else {
            linearLayout.setBackgroundColor(getActivity().getColor(R.color.colorBackGroundLigth));
        }

        try {
            nbResults = new nbHeroAsync(0).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Hero hero = adaptateur.getHero(position);
                Intent intent = new Intent(getContext(), HeroActivity.class);
                intent.putExtra("hero", hero);
                startActivity(intent);
            }
        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(count -> {
            //get page of random characters
            Random r = new Random();
            int i = r.nextInt(lv.getAdapter().getCount());
            Hero hero = adaptateur.getHero(i);
            Intent intent = new Intent(getContext(), HeroActivity.class);
            intent.putExtra("hero", hero);
            startActivity(intent);
        });
        //création d'un bouton "more comics" à la fin de la liste
        Button endButton = new Button(getContext());
        endButton.setText(R.string.seeMore);
        endButton.setBackgroundColor(Color.parseColor("#e23636"));
        endButton.setTextColor(Color.parseColor("#FCFBFB"));
        Typeface face = ResourcesCompat.getFont(getContext(), R.font.starguard);
        endButton.setTypeface(face);
        endButton.setPadding(10, 10, 10, 10);
        endButton.setTextSize(20);

        //Création d'un layout pour intégrer le bouton
        final LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(10,10,10,10);

        //Action du bouton
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on lance la requête
                new readerHeroAsync(adaptateur, offset, progress, layout, nbResults).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
                //on incrémente offset pour les résultats suivant
                offset += 100;
                if (offset > nbResults){
                    layout.setVisibility(View.GONE);
                }

            }
        });
        endButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(endButton);
        layout.setVisibility(View.GONE);
        if (nbResults > 100){
            lv.addFooterView(layout);
        }
        new readerHeroAsync(adaptateur, offset, progress, layout, nbResults).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
        offset += 100;
        /*
        //création d'un bouton "more comics" à la fin de la liste
        Button endButton = new Button(getActivity());
        endButton.setText(R.string.seeMore);
        endButton.setBackgroundColor(Color.parseColor("#e23636"));
        endButton.setTextColor(Color.parseColor("#FCFBFB"));
        Typeface face = ResourcesCompat.getFont(getContext(), R.font.starguard);
        endButton.setTypeface(face);
        endButton.setPadding(10, 10, 10, 10);
        endButton.setTextSize(20);

        //Création d'un layout pour intégrer le bouton
        final LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(10,10,10,10);

        //Action du bouton
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on lance la requête
                new readerHeroAsync(adaptateur, offset, progress, layout, nbResults).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
                //on incrémente offset pour les résultats suivant
                offset += 100;
                if (offset > nbResults){
                    layout.setVisibility(View.GONE);
                }

            }
        });
        endButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(endButton);
        if (nbResults > 100){
            Log.i("coucou", "coucou : layoutfooter");
            lv.addFooterView(layout);
        }
        new readerHeroAsync(adaptateur, offset, progress, layout, nbResults).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
        offset += 100;*/
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        LinearLayout linearLayout = root.findViewById(R.id.fragment_news_rootview);
        SharedPreferences prefs = getActivity().getSharedPreferences("colorstyle", getContext().MODE_PRIVATE );
        Log.i("colorr", prefs.getBoolean("nightMode", false) + " colooooooooooooooor");
        if (prefs.getBoolean("nightMode", false)) {
            linearLayout.setBackgroundColor(getActivity().getColor(R.color.colorBackGroundNigth));
        } else {
            linearLayout.setBackgroundColor(getActivity().getColor(R.color.colorBackGroundLigth));
        }
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
