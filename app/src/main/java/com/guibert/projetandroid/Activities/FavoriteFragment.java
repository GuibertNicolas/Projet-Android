package com.guibert.projetandroid.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.guibert.projetandroid.Adapters.ListFavComicAdapter;
import com.guibert.projetandroid.Data.Comic;
import com.guibert.projetandroid.Data.MyDatabase;
import com.guibert.projetandroid.R;

import java.util.ArrayList;

/*
    fragment pour afficher les favoris
 */
public class FavoriteFragment extends Fragment {
    private ArrayList<Comic> vComic = new ArrayList<>();
    private ListFavComicAdapter adpt;
    private MyDatabase mydb;
    private View view;
    private ListView lv;
    private LinearLayout linearLayout;
    public FavoriteFragment() {}

    public static FavoriteFragment newInstance() {
        return (new FavoriteFragment());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_favorite, container,false);
            mydb = new MyDatabase(getContext());
            linearLayout = view.findViewById(R.id.fragment_news_rootview);
            //get favorite comic from DB
            vComic = mydb.readData();
            adpt = new ListFavComicAdapter(getContext(), vComic);
            adpt.notifyDataSetChanged();
            lv = view.findViewById(R.id.listfavcomic);
            lv.setAdapter(adpt);
            //redirect on selected Comic Activity
            lv.setOnItemClickListener((parent, view, position, id) -> {
                Comic comic = adpt.getComic(position);
                Intent intent = new Intent(getContext(), ComicActivity.class);
                intent.putExtra("comic", comic);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        SharedPreferences prefs = getActivity().getSharedPreferences("colorstyle", getContext().MODE_PRIVATE );
        changeThemeColor(prefs.getBoolean("nightMode", false));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        vComic = mydb.readData();
        adpt = new ListFavComicAdapter(getContext(), vComic);
        adpt.notifyDataSetChanged();
        lv.setAdapter(adpt);

        linearLayout = view.findViewById(R.id.fragment_news_rootview);
        SharedPreferences prefs = getActivity().getSharedPreferences("colorstyle", getContext().MODE_PRIVATE );
        changeThemeColor(prefs.getBoolean("nightMode", false));

    }

    //change Color for night/light mode
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changeThemeColor(Boolean nightMode){
        if (nightMode) {
            linearLayout.setBackgroundColor(getActivity().getColor(R.color.colorBackGroundNigth));
        } else {
            linearLayout.setBackgroundColor(getActivity().getColor(R.color.colorBackGroundLigth));
        }
    }
}
