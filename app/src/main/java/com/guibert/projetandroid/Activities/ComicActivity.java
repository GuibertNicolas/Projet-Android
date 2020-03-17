package com.guibert.projetandroid.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.guibert.projetandroid.Adapters.ListHeroInComicAdapter;
import com.guibert.projetandroid.AsyncTasks.readerHeroAsync;
import com.guibert.projetandroid.Data.Comic;
import com.guibert.projetandroid.Data.MyDatabase;
import com.guibert.projetandroid.Hero;
import com.guibert.projetandroid.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComicActivity extends AppCompatActivity {
    private ArrayList<Hero> vHero = new ArrayList<>();
    private TextView comicFormat;
    private TextView comicPages;
    private LinearLayoutCompat linearLayout;

    private int offset = 0;
    private MyDatabase mydb;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);
        mydb = new MyDatabase(this);

        //Initialize components
        ImageView comicImg = findViewById(R.id.comicviewimg);
        comicFormat = findViewById(R.id.comicviewFormat);
        comicPages = findViewById(R.id.comicviewpages);
        TextView comicTitle = findViewById(R.id.comicviewtitle);
        TextView comicDes = findViewById(R.id.comicViewDescription);
        Button buyButton = findViewById(R.id.comiclistBuyButton);
        CheckBox isFavorite = findViewById(R.id.item_card_check_favorite);
        linearLayout = findViewById(R.id.comicActLayout);
        SharedPreferences prefs = getSharedPreferences("colorstyle", MODE_PRIVATE);
        changeThemeColor(prefs.getBoolean("nightMode", false));


        final ListHeroInComicAdapter adpt =  new ListHeroInComicAdapter(this, vHero);
        ListView lv = findViewById(R.id.comicViewListCharacters);
        lv.setAdapter(adpt);

        //redirect on Hero activity
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Hero hero = adpt.getHero(position);
            Intent intent = new Intent(getBaseContext(), HeroActivity.class);
            intent.putExtra("hero", hero);
            startActivity(intent);
        });


        //Receive datas from Main Activity
        Intent intent = getIntent();
        if (intent != null){
            Bundle extras = getIntent().getExtras();
            //INformation for actual comic
            final Comic comic = (Comic) extras.getSerializable("comic");
            if (comic != null){
                //title
                comicTitle.setText(comic.getName());
                //description
                if (comic.getDescription().equals("null") || comic.getDescription().equals("")) {
                    comicDes.setText(R.string.no_description);
                } else {
                    comicDes.setText(comic.getDescription());
                }
                //format
                comicFormat.setText(comic.getFormat());
                //page
                comicPages.setText((comic.getNbPage() + " page(s)"));
                //image
                if (comic.getImg().contains("image_not_available")){
                    Picasso.get().load(R.drawable.marvelcomcisnotavailable).placeholder(R.drawable.marvelcomcisnotavailable).into(comicImg);
                } else {
                    Picasso.get().load(comic.getImg()).placeholder(R.drawable.marvelcomcisnotavailable).into(comicImg);
                }
                //Hero list
                while (offset < comic.getNbCharacters()){
                    new readerHeroAsync(adpt, offset).execute(comic.getCharactersUrl()+"?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
                    offset += 100;
                }
                //redirect to buy page on click
                buyButton.setOnClickListener(v -> {
                    Uri buyUrl = Uri.parse(comic.getPurchaseUrl());
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, buyUrl);
                    startActivity(launchBrowser);
                });
                //look in DB is comic is fav
                isFavorite.setChecked(mydb.isFav(comic.getId()));
                isFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    //on check : add to DB
                    if (isChecked) {
                        Toast.makeText(getApplicationContext(), "Added to favorite", Toast.LENGTH_SHORT).show();
                        mydb.insertData(comic);
                    } else { //remove from DB
                        Toast.makeText(getApplicationContext(), "Removed from favorite", Toast.LENGTH_SHORT).show();
                        mydb.deleteComic(comic.getId());
                    }
                });
            }
        }

    }

    //change Color for night/light mode
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changeThemeColor(Boolean nightMode){
        if (nightMode) {
            linearLayout.setBackgroundColor(getColor(R.color.colorBackGroundNigth));
            comicFormat.setTextColor(getColor(R.color.colorBackGroundLigth));
            comicPages.setTextColor(getColor(R.color.colorBackGroundLigth));
        } else {
            linearLayout.setBackgroundColor(getColor(R.color.colorBackGroundLigth));
            comicFormat.setTextColor(getColor(R.color.colorBackGroundNigth));
            comicPages.setTextColor(getColor(R.color.colorBackGroundNigth));
        }
    }
}
