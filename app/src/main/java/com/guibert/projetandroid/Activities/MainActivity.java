package com.guibert.projetandroid.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.guibert.projetandroid.R;

/*
    Activité principale avec le side menu, toolbar
    on peut altérner entre les 2 fragments : LisHero et Favorite
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment fragmentHero;
    private Fragment fragmentFav;
    private CheckBox colorModeCb;
    private SharedPreferences.Editor mEditor;

    private static final int FRAGMENT_HERO = 0;
    private static final int FRAGMENT_FAV = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checkbox pour choisir les couleurs light / night
        colorModeCb = findViewById(R.id.colorMde);
        SharedPreferences prefs = getSharedPreferences("colorstyle", MODE_PRIVATE );
        mEditor = prefs.edit();
        //on récupère le mode de couleur dans les préférences
        colorModeCb.setChecked(prefs.getBoolean("nightMode", false));
        if (colorModeCb.isChecked()){
            colorModeCb.setText(R.string.night);
        } else {
            colorModeCb.setText(R.string.light);
        }

        colorModeCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //enregistre le mode de couleur dans les prefs
            mEditor.putBoolean("nightMode", isChecked).apply();
            if (isChecked){
                colorModeCb.setText(R.string.night);
            } else {
                colorModeCb.setText(R.string.night);
            }
        });


        // 6 - Configure all views

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();
        this.showFirstFragment();
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 4 - Handle Navigation Item Click
        int id = item.getItemId();
        switch (id) {
            case R.id.activity_main_drawer_news:
                this.showFragment(FRAGMENT_HERO);
                break;
            case R.id.activity_main_drawer_profile:
                this.showFragment(FRAGMENT_FAV);
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null){
            // 1.1 - Show News Fragment
            this.showFragment(FRAGMENT_HERO);
            // 1.2 - Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    public void showFragment(int fragment_id){
        switch (fragment_id) {
            case 0:
                this.showHeroFragment();
                break;
            case  1:
                this.showFavFragment();
                break;
        }
    }

    private void showHeroFragment() {
        if (this.fragmentHero == null) this.fragmentHero = ListHerosFragment.newInstance();
        this.startTransactionFragment(this.fragmentHero);

    }

    private void showFavFragment() {
        if (this.fragmentFav == null) this.fragmentFav = FavoriteFragment.newInstance();
        this.startTransactionFragment(this.fragmentFav);
    }

    // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar(){
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout(){
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView(){
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
