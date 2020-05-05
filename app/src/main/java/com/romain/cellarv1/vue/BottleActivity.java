package com.romain.cellarv1.vue;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.Tools;

public class BottleActivity extends AppCompatActivity {

    // Initialisation de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private Boolean isFABWineMenuOpen = false;

    // Initialisation des champs texte et des ImageView
    private EditText countryBottle, regionBottle, domainBottle, appellationBottle, millesimeBottle, apogeeBottle, estimateBottle;
    private ImageView favoriteBottle, imageBottle, imageWineColor;

    // Update Button
    private Button btnUpdateBottle;

    // Initialisation du Popup
    public Dialog popup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);
        init();




        btnUpdateBottle.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btnAccept = (Button) popup.findViewById(R.id.btnAccept);
                Button btnDenie = (Button) popup.findViewById(R.id.btnDenie);

                ImageView imageWineColor = (ImageView) popup.findViewById(R.id.imageWineColor);
                ImageView imageBottle = (ImageView) popup.findViewById(R.id.imageBottle);
                TextView country = (TextView) popup.findViewById(R.id.country);
                TextView region = (TextView) popup.findViewById(R.id.region);
                TextView domain = (TextView) popup.findViewById(R.id.domain);
                TextView appellation = (TextView) popup.findViewById(R.id.appellation);

                switch(getIntent().getStringExtra("wineColor").trim()) {
                    case "Rouge" :
                        imageWineColor.setImageResource(R.drawable.red_wine_listview);
                        break;
                    case "Rose" :
                        imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                        break;
                    case "Blanc" :
                        imageWineColor.setImageResource(R.drawable.white_wine_listview);
                        break;
                    case "Effervescent" :
                        imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                        break;
                }

                String image = getIntent().getStringExtra("imageBottle");
                Tools tools = new Tools();
                imageBottle.setImageBitmap(tools.stringToBitmap(image));

                switch(getIntent().getStringExtra("favorite")) {
                    case "0" :
                        favoriteBottle.setVisibility(View.INVISIBLE);
                        break;
                    case "1" :
                        favoriteBottle.setVisibility(View.VISIBLE);
                        break;
                }

                country.setText(countryBottle.getText());
                region.setText(regionBottle.getText());
                domain.setText(domainBottle.getText());
                appellation.setText(appellationBottle.getText());

                popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popup.show();

                btnAccept.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String strRandom = getIntent().getStringExtra("random");
                        String strCountry = countryBottle.getText().toString();
                        String strRegion = regionBottle.getText().toString();
                        String strDomain = domainBottle.getText().toString();
                        String strAppellation = appellationBottle.getText().toString();
                        Integer intMillesime = Integer.parseInt(millesimeBottle.getText().toString());
                        Integer intApogee = Integer.parseInt(apogeeBottle.getText().toString());
                        Integer intEstimate = Integer.parseInt(estimateBottle.getText().toString());
                        AccesLocal accesLocal = new AccesLocal(getApplicationContext());
                        accesLocal.updateBottle(strRandom, strCountry, strRegion, strDomain, strAppellation, intMillesime, intApogee, intEstimate);
                        popup.dismiss();
                    }
                });

                btnDenie.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });
            }
        });


    }

    private void init() {

        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        initWineBottle();
        btnUpdateBottle = (Button) findViewById(R.id.btnUpdateBottle);

        popup = new Dialog(this);
        popup.setContentView(R.layout.popup_update_bottle);




    }

    private void initWineBottle() {
        favoriteBottle = (ImageView) findViewById(R.id.favoriteBottle);
        imageBottle = (ImageView) findViewById(R.id.imageBottle);
        imageWineColor = (ImageView) findViewById(R.id.imageWineColor);

        countryBottle = (EditText) findViewById(R.id.countryBottle);
        regionBottle = (EditText) findViewById(R.id.regionBottle);
        domainBottle = (EditText) findViewById(R.id.domainBottle);
        appellationBottle = (EditText) findViewById(R.id.appellationBottle);
        millesimeBottle = (EditText) findViewById(R.id.millesimeBottle);
        apogeeBottle = (EditText) findViewById(R.id.apogeeBottle);
        estimateBottle = (EditText) findViewById(R.id.estimateBottle);


        switch(getIntent().getStringExtra("wineColor").trim()) {
            case "Rouge" :
                imageWineColor.setImageResource(R.drawable.red_wine_listview);
                break;
            case "Rose" :
                imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                break;
            case "Blanc" :
                imageWineColor.setImageResource(R.drawable.white_wine_listview);
                break;
            case "Effervescent" :
                imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                break;
        }

        String image = getIntent().getStringExtra("imageBottle");
        Tools tools = new Tools();
        imageBottle.setImageBitmap(tools.stringToBitmap(image));

        switch(getIntent().getStringExtra("favorite")) {
            case "0" :
                favoriteBottle.setVisibility(View.INVISIBLE);
                break;
            case "1" :
                favoriteBottle.setVisibility(View.VISIBLE);
                break;
        }

        countryBottle.setText(getIntent().getStringExtra("country"));
        regionBottle.setText(getIntent().getStringExtra("region"));
        domainBottle.setText(getIntent().getStringExtra("domain"));
        appellationBottle.setText(getIntent().getStringExtra("appellation"));
        millesimeBottle.setText(getIntent().getStringExtra("millesime"));
        apogeeBottle.setText(getIntent().getStringExtra("apogee"));
        estimateBottle.setText(getIntent().getStringExtra("estimate"));
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

    private void initCurvedNavigationView() {
        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        curvedBottomNavigationView.setSelectedItemId(R.id.scan);
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
                    case R.id.like:
                        //Toast.makeText(UserActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
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
