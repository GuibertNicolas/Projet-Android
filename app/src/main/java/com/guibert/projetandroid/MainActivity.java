package com.guibert.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<Hero> vHero = new ArrayList<Hero>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListHeroViewAdapter adaptateur = new ListHeroViewAdapter(this, vHero);
        ListView lv = (ListView) findViewById(R.id.listhero);
        lv.setAdapter(adaptateur);
        int offset = 0;
        //lv.setOnScrollListener(new ScrollListenerHero(300,offset, adaptateur));
        //System.out.println(new readerHeroAsync(adaptateur, offset).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60"));
        //while (offset < 300) {
            new readerHeroAsync(adaptateur, offset).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
            //offset += 100;
        //}
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Hero hero = adaptateur.getHero(position);
                Intent intent = new Intent(getBaseContext(), HeroActivity.class);
                intent.putExtra("hero", hero);
                startActivity(intent);
            }
        });


    }
}


