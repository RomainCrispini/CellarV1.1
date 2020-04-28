package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.outils.CellarPageAdapter;
import com.romain.cellarv1.outils.CellarTabsTransition;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;


public class LikeActivity extends AppCompatActivity {

    // Initialisation de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private Boolean isFABWineMenuOpen = false;

    // Initialisation accesLocal
    private AccesLocal accesLocal;

    // Initialisation des Tabs
    private CellarPageAdapter cellarPageAdapter;
    private TabLayout cellarTabLayout;
    private ViewPager viewPager;

    private LikeListFragment likeListFragment;
    private LikeWishlistFragment likeWishlistFragment;

    // Initialisation du menu bis
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private FrameLayout sortMenu;
    private ImageButton sortMap;
    private ImageButton sortColor;
    private ImageButton sortYear;
    private ImageButton sortApogee;
    private ImageView sortRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        init();


    }

    private void init() {

        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        initTabs();
        menuBisSelectedItems();

        TabLayout cellarTabLayout = (TabLayout) findViewById(R.id.likeTabLayout);
        cellarTabLayout.setTranslationY(-200f);
        cellarTabLayout.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        FrameLayout sortMenu = (FrameLayout) findViewById(R.id.sortMenu);
        sortMenu.setTranslationY(200f);
        sortMenu.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

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
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("redWine", "redWine");
                startActivity(intent);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROUGE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabRose = (FloatingActionButton) findViewById(R.id.fabRose);
        fabRose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("roseWine", "roseWine");
                startActivity(intent);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROSE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabWhite = (FloatingActionButton) findViewById(R.id.fabWhite);
        fabWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("whiteWine", "whiteWine");
                startActivity(intent);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB BLANC",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabChamp = (FloatingActionButton) findViewById(R.id.fabChamp);
        fabChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("champWine", "champWine");
                startActivity(intent);
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

    private void initTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.likeViewPager);
        final TabLayout cellarTabLayout = (TabLayout) findViewById(R.id.likeTabLayout);

        likeListFragment = new LikeListFragment();
        likeWishlistFragment = new LikeWishlistFragment();

        cellarTabLayout.setupWithViewPager(viewPager);
        CellarPageAdapter cellarPageAdapter = new CellarPageAdapter(getSupportFragmentManager(), 0);

        viewPager.setPageTransformer(true, new CellarTabsTransition());

        cellarPageAdapter.addFragment(likeListFragment, "List");
        cellarPageAdapter.addFragment(likeWishlistFragment, "Stats");

        viewPager.setAdapter(cellarPageAdapter);

        cellarTabLayout.getTabAt(0).setIcon(R.drawable.icone_menu_tabs_like);
        cellarTabLayout.getTabAt(1).setIcon(R.drawable.icone_menu_tabs_wishlist);

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

    private void menuBisSelectedItems() {

        final ImageButton sortMap = (ImageButton) findViewById(R.id.sortMap);
        final ImageButton sortColor = (ImageButton) findViewById(R.id.sortColor);
        final ImageButton sortYear = (ImageButton) findViewById(R.id.sortYear);
        final ImageButton sortApogee = (ImageButton) findViewById(R.id.sortApogee);
        final ImageView sortRecover = (ImageView) findViewById(R.id.sortRecover);


        sortMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadSortMapWineBottleInListView();
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
                //loadSortColorWineBottleInListView();
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
                //loadRecoverWineBottleInListView();
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
                //loadSortYearWineBottleInListView();
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
                //loadSortApogeeWineBottleInListView();
                sortMap.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortColor.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortRecover.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortYear.setColorFilter(getApplicationContext().getColor(R.color.green_middle_light), PorterDuff.Mode.SRC_IN);
                sortApogee.setColorFilter(getApplicationContext().getColor(R.color.green_light), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    private void initCurvedNavigationView() {
        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        curvedBottomNavigationView.setSelectedItemId(R.id.like);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new CurvedBottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.cellar:
                        //Toast.makeText(UserActivity.this, "USER", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        //overridePendingTransition(0, 0);
                        return true;
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