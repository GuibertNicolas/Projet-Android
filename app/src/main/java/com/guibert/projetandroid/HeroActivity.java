package com.guibert.projetandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HeroActivity extends Activity {
    private ArrayList<Comic> vComic = new ArrayList<Comic>();
    private int offset = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListComicViewAdapter adaptateur = new ListComicViewAdapter(this, vComic);
        setContentView(R.layout.activity_hero);
        TextView heroName = (TextView) findViewById(R.id.heroViewName);
        TextView heroDes = (TextView) findViewById(R.id.heroViewDSescription);
        ImageView heroImg = (ImageView) findViewById(R.id.heroViewImg);
        ListView lv = (ListView) findViewById(R.id.listcomic);
        lv.setAdapter(adaptateur);

        //Send Comic datas to comic activity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Comic comic = adaptateur.getComic(position);
                Intent intent = new Intent(getBaseContext(), ComicActivity.class);
                intent.putExtra("comic", comic);
                startActivity(intent);
            }
        });

        //Receive datas from Main Activity
        Intent intent = getIntent();
        if (intent != null){
            Bundle extras = getIntent().getExtras();
            Hero hero = (Hero) extras.getSerializable("hero");
            if (hero != null){
                heroName.setText(hero.getName());
                heroDes.setText(hero.getDescription());
                if (hero.getImg().contains("image_not_available")){
                    Picasso.get().load(R.drawable.stanleeheronotfound).placeholder(R.drawable.stanleeheronotfound).into(heroImg);
                } else {
                    Picasso.get().load(hero.getImg()).placeholder(R.drawable.stanleeheronotfound).into(heroImg);
                }
                new readerComicsAsync(adaptateur, offset).execute(hero.getComicsUrl()+"?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
            }
        }
    }
}
