package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.CellarPageAdapter;
import com.romain.cellarv1.outils.CellarTabsTransition;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.MyAdapterCellarRecyclerView;
import com.romain.cellarv1.outils.MyButtonClickListener;
import com.romain.cellarv1.outils.MySwipeHelper;
import com.romain.cellarv1.outils.Tools;

import java.util.ArrayList;
import java.util.List;


public class CellarActivity extends AppCompatActivity {

    // Initialisation accesLocal
    private AccesLocal accesLocal;

    // Initialisation des Tabs
    private CellarPageAdapter cellarPageAdapter;
    private TabLayout cellarTabLayout;
    private ViewPager viewPager;
    private CellarListFragment cellarListFragment;
    private CellarStatsFragment cellarStatsFragment;

    // Initialisation du menu bis
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private FrameLayout sortMenu;
    private ImageButton sortMap;
    private ImageButton sortColor;
    private ImageButton sortYear;
    private ImageButton sortApogee;
    private ImageView sortRecover;


    // Initialisation du RecyclerView du Tab1
    //private RecyclerView cellarRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellar);
        init();
        //cellarRecyclerView = (RecyclerView) findViewById(R.id.cellarRecyclerView);



    }

    private void init() {

        //txtViewBDD.setMovementMethod(new ScrollingMovementMethod()); // Méthode qui rend la textView scrollable

        initCurvedNavigationView();

        TabLayout cellarTabLayout = (TabLayout) findViewById(R.id.cellarTabLayout);
        cellarTabLayout.setTranslationY(-200f);
        cellarTabLayout.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        FrameLayout sortMenu = (FrameLayout) findViewById(R.id.sortMenu);
        sortMenu.setTranslationY(200f);
        sortMenu.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        initTabs();

        menuBisSelectedItems();

    }

    private void menuBisSelectedItems() {

        final ImageButton sortMap = (ImageButton) findViewById(R.id.sortMap);
        final ImageButton sortColor = (ImageButton) findViewById(R.id.sortColor);
        final ImageButton sortYear = (ImageButton) findViewById(R.id.sortYear);
        final ImageButton sortApogee = (ImageButton) findViewById(R.id.sortApogee);
        final ImageView sortRecover = (ImageView) findViewById(R.id.sortRecover);


        sortMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortMapWineBottleInListView();
                sortMap.setColorFilter(getApplicationContext().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortColorWineBottleInListView();
                sortMap.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(getApplicationContext().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecoverWineBottleInListView();
                sortMap.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(getApplicationContext().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortYearWineBottleInListView();
                sortMap.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(getApplicationContext().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortApogee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortApogeeWineBottleInListView();
                sortMap.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(getApplicationContext().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    private void loadSortMapWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(getApplicationContext());
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortMapWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(getApplicationContext(), wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortColorWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(getApplicationContext());
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortColorWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(getApplicationContext(), wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadRecoverWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(getApplicationContext());
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(getApplicationContext(), wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortYearWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(getApplicationContext());
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortYearWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(getApplicationContext(), wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    ///////////////////////////////////////////////////////////////////////TODO
    private void loadSortApogeeWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(getApplicationContext());
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortMapWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(getApplicationContext(), wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void initTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.cellarViewPager);
        final TabLayout cellarTabLayout = (TabLayout) findViewById(R.id.cellarTabLayout);

        cellarListFragment = new CellarListFragment();
        cellarStatsFragment = new CellarStatsFragment();


        cellarTabLayout.setupWithViewPager(viewPager);
        CellarPageAdapter cellarPageAdapter = new CellarPageAdapter(getSupportFragmentManager(), 0);

        viewPager.setPageTransformer(true, new CellarTabsTransition());

        cellarPageAdapter.addFragment(cellarListFragment, "List");
        cellarPageAdapter.addFragment(cellarStatsFragment, "Stats");

        viewPager.setAdapter(cellarPageAdapter);

        cellarTabLayout.getTabAt(0).setIcon(R.drawable.icone_menu_tabs_list);
        cellarTabLayout.getTabAt(1).setIcon(R.drawable.icone_menu_tabs_stats);

        cellarTabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
        cellarTabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);


        cellarTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#67828f"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#4F656F"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void initCurvedNavigationView() {
        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        curvedBottomNavigationView.setSelectedItemId(R.id.cellar);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new CurvedBottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.user:
                        //Toast.makeText(UserActivity.this, "USER", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.scan:
                        //Toast.makeText(UserActivity.this, "SCAN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.like:
                        //Toast.makeText(UserActivity.this, "LIKE", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.search:
                        //Toast.makeText(UserActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        //overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

}