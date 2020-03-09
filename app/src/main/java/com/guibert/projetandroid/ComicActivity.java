package com.guibert.projetandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComicActivity extends Activity {
    private ArrayList<Hero> vHero = new ArrayList<Hero>();
    private int offset = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);

        //Initialize components
        ImageView comicImg = findViewById(R.id.comicviewimg);
        TextView comicFormat = findViewById(R.id.comicviewFormat);
        TextView comicPages = findViewById(R.id.comicviewpages);
        TextView comicTitle = findViewById(R.id.comicviewtitle);
        TextView comicDes = findViewById(R.id.comicViewDescription);
        final ListHeroInComicAdapter adpt =  new ListHeroInComicAdapter(this, vHero);
        ListView lv = findViewById(R.id.comicViewListCharacters);
        lv.setAdapter(adpt);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Hero hero = adpt.getHero(position);
                Intent intent = new Intent(getBaseContext(), HeroActivity.class);
                intent.putExtra("hero", hero);
                startActivity(intent);
            }
        });


        //Receive datas from Main Activity
        Intent intent = getIntent();
        if (intent != null){
            Bundle extras = getIntent().getExtras();
            Comic comic = (Comic) extras.getSerializable("comic");
            if (comic != null){
                comicTitle.setText(comic.getName());
                if (comic.getDescription().equals("null")) {
                    comicDes.setText("Pas de description disponible");
                } else {
                    comicDes.setText(comic.getDescription());
                }
                comicFormat.setText(comic.getFormat());
                comicPages.setText(comic.getNbPage() + " page(s)");
                if (comic.getImg().contains("image_not_available")){
                    Picasso.get().load(R.drawable.stanleeheronotfound).placeholder(R.drawable.stanleeheronotfound).into(comicImg);
                } else {
                    Picasso.get().load(comic.getImg()).placeholder(R.drawable.stanleeheronotfound).into(comicImg);
                }
            while (offset < comic.getNbCharacters()){
                new readerHeroAsync(adpt, offset).execute(comic.getCharactersUrl()+"?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
                offset += 100;
            }
            }
        }

    }


}
