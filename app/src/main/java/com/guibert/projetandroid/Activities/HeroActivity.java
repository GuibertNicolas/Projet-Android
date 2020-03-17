package com.guibert.projetandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.guibert.projetandroid.Adapters.ListComicViewAdapter;
import com.guibert.projetandroid.AsyncTasks.readerComicsAsync;
import com.guibert.projetandroid.Data.Comic;
import com.guibert.projetandroid.Data.Hero;
import com.guibert.projetandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HeroActivity extends Activity {
    private ArrayList<Comic> vComic = new ArrayList<>();
    private LinearLayoutCompat linearLayout;
    private int offset = 0;
    private ProgressBar progress;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListComicViewAdapter adaptateur = new ListComicViewAdapter(this, vComic);
        setContentView(R.layout.activity_hero);

        //Création des éléments
        TextView heroName = findViewById(R.id.heroViewName);
        TextView heroDes = findViewById(R.id.heroViewDSescription);
        ImageView heroImg = findViewById(R.id.heroViewImg);
        ListView lv = findViewById(R.id.listcomic);
        progress = findViewById(R.id.simpleProgressBar);
        progress.setVisibility(View.GONE);
        lv.setAdapter(adaptateur);

        linearLayout = findViewById(R.id.heroActLayout);
        SharedPreferences prefs = getSharedPreferences("colorstyle", MODE_PRIVATE );
        changeThemeColor(prefs.getBoolean("nightMode", false));


        //Send Comic datas to comic activity
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Comic comic = adaptateur.getComic(position);
            Intent intent = new Intent(getBaseContext(), ComicActivity.class);
            intent.putExtra("comic", comic);
            startActivity(intent);
        });

        //Receive datas from Main Activity
        Intent intent = getIntent();
        if (intent != null){
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                final Hero hero = (Hero) extras.getSerializable("hero");
                if (hero != null){
                    heroName.setText(hero.getName());
                    if (hero.getDescription().equals("null") || hero.getDescription().equals("")) {
                        heroDes.setText(R.string.no_description);
                    } else {
                        heroDes.setText(hero.getDescription());
                    }
                    if (hero.getImg().contains("image_not_available")){
                        Picasso.get().load(R.drawable.stanleeheronotfound).placeholder(R.drawable.stanleeheronotfound).into(heroImg);
                    } else {
                        Picasso.get().load(hero.getImg()).placeholder(R.drawable.stanleeheronotfound).into(heroImg);
                    }
                    //création d'un bouton "see more" à la fin de la liste
                    Button endButton = new Button(this);
                    endButton.setText(R.string.seeMore);
                    endButton.setBackgroundColor(Color.parseColor("#e23636"));
                    endButton.setTextColor(Color.parseColor("#FCFBFB"));
                    Typeface face = getResources().getFont(R.font.starguard);
                    endButton.setTypeface(face);
                    endButton.setPadding(10, 10, 10, 10);
                    endButton.setTextSize(20);

                    //Création d'un layout pour intégrer le bouton
                    final LinearLayout layout = new LinearLayout(this);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);
                    layout.setPadding(10,10,10,10);

                    //Action du bouton
                    endButton.setOnClickListener(v -> {
                        //on lance la requête pour avoir les comics
                        new readerComicsAsync(adaptateur, offset, progress, layout, hero.getNbComics()).execute(hero.getComicsUrl()+"?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
                        //on incrémente offset pour avoir les résultats suivant
                        offset += 100;
                        //on cache le bouton une fois la tous les résultats récupérer
                        if (offset > hero.getNbComics()){
                            layout.setVisibility(View.GONE);
                        }
                    });
                    endButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.addView(endButton);
                    layout.setVisibility(View.GONE);
                    //on ajoute le layout uniquement si il y a plus de 100 comics
                    if (hero.getNbComics() > 100){
                        lv.addFooterView(layout);
                    }
                    //on récupère les 100 premiers résultats
                    new readerComicsAsync(adaptateur, offset, progress, layout, hero.getNbComics()).execute(hero.getComicsUrl()+"?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
                    offset += 100;
                }
            }
        }
    }
    //change Color for night/light mode
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changeThemeColor(Boolean nightMode){
        if (nightMode) {
            linearLayout.setBackgroundColor(getColor(R.color.colorBackGroundNigth));
        } else {
            linearLayout.setBackgroundColor(getColor(R.color.colorBackGroundLigth));
        }
    }
}
