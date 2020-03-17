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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.guibert.projetandroid.Adapters.ListHeroViewAdapter;
import com.guibert.projetandroid.AsyncTasks.nbHeroAsync;
import com.guibert.projetandroid.AsyncTasks.readerHeroAsync;
import com.guibert.projetandroid.Data.Hero;
import com.guibert.projetandroid.R;
import com.guibert.projetandroid.Data.ShakeDetector;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;


/*
Liste des héros
 */
public class ListHerosFragment extends Fragment {
    private ListHeroViewAdapter adaptateur;
    private ListView lv;
    private int nbResults;
    private int offset;
    private LinearLayout linearLayout;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private View root;
    private ProgressBar progress;

    public ListHerosFragment() {}

    public static ListHerosFragment newInstance() {
        return (new ListHerosFragment());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.fragment_heros, container, false);
        // Inflate the layout for this fragment
        ArrayList<Hero> vHero = new ArrayList<>();
        adaptateur = new ListHeroViewAdapter(getContext(), vHero);
        lv = root.findViewById(R.id.listhero);
        lv.setAdapter(adaptateur);
        //barre de progression
        progress = root.findViewById(R.id.simpleProgressBarHerofragment);
        progress.setVisibility(View.GONE);
        offset = 0;
        linearLayout = root.findViewById(R.id.fragment_news_rootview);
        //on récupère le nombre de résultats total car on ne peut récuperer les héros que 100 par 100
        try {
            nbResults = new nbHeroAsync(0).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60").get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        //on redirige vers Héro Activity quand on click sur un item de la liste
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Hero hero = adaptateur.getHero(position);
            //on passe le héro choisi en parametre
            Intent intent = new Intent(getContext(), HeroActivity.class);
            intent.putExtra("hero", hero);
            startActivity(intent);
        });

        /*
            création d'un footer pour la liste avec un bouton pour afficher + de résultats
        */
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
                //on incrémente offset pour avoir les résultats suivant
                offset += 100;
                //cache le bouton si on dépasse le nombre de résulats
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
        //on récupère les 100 premiers résulats
        new readerHeroAsync(adaptateur, offset, progress, layout, nbResults).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
        offset += 100;

        //montre un héro au hasard si on secoue le téléphone
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(count -> {
            //on prend un héro au hasard et on lance l'activité Héro avec ses informations
            Random r = new Random();
            int i = r.nextInt(lv.getAdapter().getCount());
            Hero hero = adaptateur.getHero(i);
            Intent intent = new Intent(getContext(), HeroActivity.class);
            intent.putExtra("hero", hero);
            startActivity(intent);
        });

        //changement de couleurs
        SharedPreferences prefs = getActivity().getSharedPreferences("colorstyle", getContext().MODE_PRIVATE );
        changeThemeColor(prefs.getBoolean("nightMode", false));
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        linearLayout = root.findViewById(R.id.fragment_news_rootview);
        SharedPreferences prefs = getActivity().getSharedPreferences("colorstyle", getContext().MODE_PRIVATE );
        changeThemeColor(prefs.getBoolean("nightMode", false));
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changeThemeColor(boolean nightmode){
        if (nightmode) {
            linearLayout.setBackgroundColor(getActivity().getColor(R.color.colorBackGroundNigth));
        } else {
            linearLayout.setBackgroundColor(getActivity().getColor(R.color.colorBackGroundLigth));
        }
    }
}
