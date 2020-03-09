package com.guibert.projetandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ListHeroInComicAdapter extends ArrayAdapter<Hero> {
    private Context c;
    public ListHeroInComicAdapter(Context context, ArrayList<Hero> resource) {
        super(context, 0, resource);
        this.c = context;
        Picasso.get().setLoggingEnabled(true);
    }

    public int getCount() {
        return super.getCount();
    }
    public Hero getHero(int position){
        return this.getItem(position);
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Hero hero = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.heroitemincomicview, parent, false);
        }// Lookup view for data population
        TextView heroName = (TextView) convertView.findViewById(R.id.heroInComicListName);
        ImageView heroImg = (ImageView) convertView.findViewById(R.id.heroInComicListImg);
        // Populate the data into the template view using the data object
        heroName.setText(hero.getName());
        //System.out.println(hero.getImg());
        if (hero.getImg().contains("image_not_available")){
            Picasso.get().load(R.drawable.stanleeheronotfound).placeholder(R.drawable.stanleeheronotfound).into(heroImg);
        } else {
            Picasso.get().load(hero.getImg()).placeholder(R.drawable.stanleeheronotfound).into(heroImg);
        }
        return convertView;
    }


}

