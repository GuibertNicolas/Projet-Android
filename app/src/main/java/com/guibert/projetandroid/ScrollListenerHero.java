package com.guibert.projetandroid;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

public class ScrollListenerHero implements AbsListView.OnScrollListener {
    private int result;
    private int offset;
    private ListComicViewAdapter adaptateur;
    private String url;

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public ScrollListenerHero(int r, int o, ListComicViewAdapter a, String u) {
        this.result = r;
        this.offset = o;
        this.adaptateur = a;
        this.url = u;
    }
    @Override
    public void onScrollStateChanged(AbsListView Aview, int scrollState) {
// We take the last son in the scrollview
        if (!Aview.canScrollVertically(1)) {
            // bottom of scroll view
            if ( offset < result ) {
                Log.i("coucou", "coucou Bot offset : " + offset + " result  : " + result);
                //new readerComicsAsync(adaptateur, offset, this).execute(url+"?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
                //new readerHeroAsync(adaptateur, offset).execute("https://gateway.marvel.com/v1/public/characters?limit=100&ts=1581610808&apikey=d6d84ca8600d21e6f695f685bf539c46&hash=d365b42ecc44f34e4e5d7724de96cd60");
            }
        }
        if (!Aview.canScrollVertically(-1)) {
            Log.i("coucou", "coucou Top");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
