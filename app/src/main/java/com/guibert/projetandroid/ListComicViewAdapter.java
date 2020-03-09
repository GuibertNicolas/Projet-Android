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

class ListComicViewAdapter extends ArrayAdapter<Comic> {
    private Context c;
    public ListComicViewAdapter(Context context, ArrayList<Comic> resource) {
        super(context, 0, resource);
        this.c = context;
        Picasso.get().setLoggingEnabled(true);
    }

    public int getCount() {
        return super.getCount();
    }
    public Comic getComic(int position){
        return this.getItem(position);
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        Comic comic = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comicitem, parent, false);
        }
        TextView heroviewlistName = convertView.findViewById(R.id.comiclistname);
        ImageView comicImg = convertView.findViewById(R.id.heroviewlistimg);
        heroviewlistName.setText(comic.getName()); //comic.getName()
        if (comic.getImg().contains("image_not_available")) {
            Picasso.get().load(R.drawable.marvelcomcisnotavailable).placeholder(R.drawable.marvelcomcisnotavailable).fit().into(comicImg);
        } else {
            Picasso.get().load(comic.getImg()).placeholder(R.drawable.marvelcomcisnotavailable).fit().into(comicImg);
        }

        return convertView;
    }
}
