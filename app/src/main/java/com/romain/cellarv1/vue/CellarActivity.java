package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    // Initialisation de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private Boolean isFABWineMenuOpen = false;

    // Initialisation accesLocal
    private AccesLocal accesLocal;

    private RecyclerView.Adapter mAdapter;

    // Initialisation des Tabs
    private CellarPageAdapter cellarPageAdapter;
    private TabLayout cellarTabLayout;
    private ViewPager viewPager;
    private CellarListFragment cellarListFragment;
    private CellarStatsFragment cellarStatsFragment;

    // Initialisation du menu bis
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private FrameLayout sortMenuList;
    private FrameLayout sortMenuStats;
    private ImageButton sortMap, getSortColor, getSortYear, getSortApogee;
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

        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        initTabs();
        menuBisListSelectedItems();

        // Animation des TabLayout
        TabLayout cellarTabLayout = (TabLayout) findViewById(R.id.cellarTabLayout);
        cellarTabLayout.setTranslationY(-200f);
        cellarTabLayout.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        // A l'ouverture, animation du menuBisList
        FrameLayout sortMenuBisList = (FrameLayout) findViewById(R.id.sortMenuList);
        sortMenuBisList.setTranslationY(200f);
        sortMenuBisList.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        // A l'ouverture, on laisse caché le menuBisStats
        FrameLayout sortMenuBisStats = (FrameLayout) findViewById(R.id.sortMenuStats);
        sortMenuBisStats.setTranslationY(200f);


    }

    private void initFabWineMenu() {
        fabWineMenu = findViewById(R.id.fabWineMenu);
        fabRed = findViewById(R.id.fabRed);
        fabRose = findViewById(R.id.fabRose);
        fabWhite = findViewById(R.id.fabWhite);
        fabChamp = findViewById(R.id.fabChamp);

        fabWineMenu.setAlpha(1f);
        fabRed.setAlpha(0f);
        fabRose.setAlpha(0f);
        fabWhite.setAlpha(0f);
        fabChamp.setAlpha(0f);

        fabRed.setTranslationX(0f);
        fabRed.setTranslationY(0f);
        fabRose.setTranslationX(0f);
        fabRose.setTranslationY(0f);
        fabWhite.setTranslationX(0f);
        fabWhite.setTranslationY(0f);
        fabChamp.setTranslationX(0f);
        fabChamp.setTranslationY(0f);
    }

    private void getFabWineMenuValue() {
        FloatingActionButton fabWineMenu = (FloatingActionButton) findViewById(R.id.fabWineMenu);
        fabWineMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFABWineMenuOpen) {
                    closeFabWineMenu();
                } else {
                    openFabWineMenu();
                }
                //Toast.makeText(getApplicationContext(), "FAB WINE MENU",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabRed = (FloatingActionButton) findViewById(R.id.fabRed);
        fabRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CellarActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("redWine", "redWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROUGE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabRose = (FloatingActionButton) findViewById(R.id.fabRose);
        fabRose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CellarActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("roseWine", "roseWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROSE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabWhite = (FloatingActionButton) findViewById(R.id.fabWhite);
        fabWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CellarActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("whiteWine", "whiteWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB BLANC",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabChamp = (FloatingActionButton) findViewById(R.id.fabChamp);
        fabChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CellarActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("champWine", "champWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB CHAMP",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFabWineMenu() {
        isFABWineMenuOpen = !isFABWineMenuOpen;

        fabWineMenu.animate().setInterpolator(interpolator).rotation(135f).setDuration(300).start();

        fabRed.animate().translationX(-250f).translationY(-180f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabRose.animate().translationX(-90f).translationY(-240f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabWhite.animate().translationX(90f).translationY(-240f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabChamp.animate().translationX(250f).translationY(-180f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeFabWineMenu() {
        isFABWineMenuOpen = !isFABWineMenuOpen;

        fabWineMenu.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabRed.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabRose.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabWhite.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabChamp.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void menuBisListSelectedItems() {

        final ImageButton sortMap = (ImageButton) findViewById(R.id.sortMap);
        final ImageButton sortColor = (ImageButton) findViewById(R.id.sortColor);
        final ImageButton sortYear = (ImageButton) findViewById(R.id.sortYear);
        final ImageButton sortApogee = (ImageButton) findViewById(R.id.sortApogee);
        final ImageView sortRecover = (ImageView) findViewById(R.id.sortRecover);

        // On sélectionne par défaut l'item du centre
        sortRecover.setColorFilter(Color.parseColor("#67828f"));

        sortMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortMapWineBottleInListView();
                sortMap.setColorFilter(CellarActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortColorWineBottleInListView();
                sortMap.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(CellarActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecoverWineBottleInListView();
                sortMap.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(CellarActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortYearWineBottleInListView();
                sortMap.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(CellarActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
            }
        });

        sortApogee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSortApogeeWineBottleInListView();
                sortMap.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(CellarActivity.this.getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(CellarActivity.this.getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    private void loadSortMapWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView) findViewById(R.id.cellarRecyclerView);
        cellarRecyclerView.setLayoutManager(new LinearLayoutManager(cellarRecyclerView.getContext()));
        accesLocal = new AccesLocal(CellarActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortMapWineBottleList();

        cellarRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapterCellarRecyclerView(CellarActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // Fade in du RecyclerView
        cellarRecyclerView.setAlpha(0f);
        cellarRecyclerView.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(1500).start();
    }

    private void loadSortColorWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(CellarActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortColorWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(CellarActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadRecoverWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(CellarActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(CellarActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortYearWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(CellarActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortYearWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(CellarActivity.this, wineBottleList);
        cellarRecyclerView.setAdapter(myAdapterCellarRecyclerView);
        myAdapterCellarRecyclerView.notifyDataSetChanged();
    }

    private void loadSortApogeeWineBottleInListView() {

        RecyclerView cellarRecyclerView = (RecyclerView)findViewById(R.id.cellarRecyclerView);
        accesLocal = new AccesLocal(CellarActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.sortApogeeWineBottleList();

        MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(CellarActivity.this, wineBottleList);
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

                if(cellarTabLayout.getSelectedTabPosition() == 0) {
                    // Disparition du menuBisStats
                    FrameLayout sortMenuBisStats = (FrameLayout) findViewById(R.id.sortMenuStats);
                    sortMenuBisStats.setTranslationY(0f);
                    sortMenuBisStats.animate().translationY(200f).setInterpolator(interpolator).setDuration(1500).start();

                    // Apparition du menuBisList
                    FrameLayout sortMenuBisList = (FrameLayout) findViewById(R.id.sortMenuList);
                    sortMenuBisList.setTranslationY(200f);
                    sortMenuBisList.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();
                } else {
                    // Disparition du menuBisList
                    FrameLayout sortMenuBisList = (FrameLayout) findViewById(R.id.sortMenuList);
                    sortMenuBisList.setTranslationY(0f);
                    sortMenuBisList.animate().translationY(200f).setInterpolator(interpolator).setDuration(1500).start();

                    // Apparition du menuBisStats
                    FrameLayout sortMenuBisStats = (FrameLayout) findViewById(R.id.sortMenuStats);
                    sortMenuBisStats.setTranslationY(200f);
                    sortMenuBisStats.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();
                }


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
                        startActivity(new Intent(CellarActivity.this, UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.scan:
                        //Toast.makeText(UserActivity.this, "SCAN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CellarActivity.this, ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.like:
                        //Toast.makeText(UserActivity.this, "LIKE", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CellarActivity.this, LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.search:
                        //Toast.makeText(UserActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CellarActivity.this, SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

}