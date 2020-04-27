package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.romain.cellarv1.R;
import com.romain.cellarv1.controleur.Controle;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.Tools;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddActivity extends AppCompatActivity {

    /**
     * Propriétés
     */

    // Appareil photo et gallery
    private String photoPath = null;
    private static final int GALLERY_READ_REQUEST_PERMISSION = 100;
    private static final int CAMERA_REQUEST_PERMISSION = 101;
    // private static final int GALLERY_WRITE_REQUEST_PERMISSION = 102;
    private static final int CAMERA_REQUEST_CODE = 103;
    private static final int GALLERY_REQUEST_CODE = 104;
    private ImageView scanImageView;
    private FloatingActionButton scan;
    private LinearLayout layapp;

    // Gallery
    private FloatingActionButton btnGallery;

    // Liste pays
    private ArrayList<String> countrylist = new ArrayList<>();

    // ProgessBar
    private ProgressBar progressBar;
    //private Handler handler = new Handler();

    // Snackbar
    private ConstraintLayout mainLayout;

    // Champs texte
    private AutoCompleteTextView txtCountry;
    private EditText txtRegion, txtDomain, txtAppellation;
    private EditText nbYear, nbNumber, nbEstimate;
    private ImageButton btnRed, btnRose, btnWhite, btnChamp;
    private FloatingActionButton btnAdd;

    // Déclaration du contrôleur
    private Controle controle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();

        FloatingActionButton scan = (FloatingActionButton) findViewById(R.id.scan);

        scanImageView = (ImageView) findViewById(R.id.scanImageView);








    }


    /**
     * Méthode qui initialise les liens avec les objets graphiques, et appelle toutes les méthodes
     */
    private void init() {
        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(customBottomNavListener);
        txtCountry = (AutoCompleteTextView) findViewById(R.id.textCountry);
        txtRegion = (EditText) findViewById(R.id.textRegion);
        btnRed = (ImageButton) findViewById(R.id.redWineButton);
        btnRose = (ImageButton) findViewById(R.id.roseWineButton);
        btnWhite = (ImageButton) findViewById(R.id.whiteWineButton);
        btnChamp = (ImageButton) findViewById(R.id.champWineButton);
        txtDomain = (EditText) findViewById(R.id.textDomain);
        txtAppellation = (EditText) findViewById(R.id.textAppellation);
        nbYear = (EditText) findViewById(R.id.nbYear);
        nbNumber = (EditText) findViewById(R.id.nbNumber);
        nbEstimate = (EditText) findViewById(R.id.nbEstimate);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        this.controle = Controle.getInstance(this); // Création d'une instance de type Controle

        FloatingActionButton btnGallery = (FloatingActionButton) findViewById(R.id.btnGallery);
        ImageView scanImageView = (ImageView) findViewById(R.id.scanImageView);

        mainLayout = findViewById(R.id.mainLayout);

        addWineBottle();
        recoverWineBottle();
        recoverFABWineColor();
        recoverJsonCountries();
        //pulsar();
        progressBar();

    }





    public void accesGallery(View view) {
        // Permission pour accès Gallery
        if(ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Accès à la gallery du tel
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        } else {
            galleryRequestPermission();
        }
    }

    private void galleryRequestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(AddActivity.this)
                    .setTitle("Permission")
                    .setMessage("Cellar requiert votre permission pour accéder à votre galerie d'images")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_READ_REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_READ_REQUEST_PERMISSION);
        }
    }

    private void cameraRequestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(AddActivity.this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(AddActivity.this)
                    .setTitle("Permission")
                    .setMessage("Cellar requiert votre permission pour accéder à votre appareil photo")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
        }
    }

    // Au retour de la sélection d'une image (après appel de startactivityforresult)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView scanImageView = (ImageView) findViewById(R.id.scanImageView); // DOIT ETRE DECLAREE ICI !!!!!!!!!!!!!!!!!!!!!!
        // Vérification du bon code de retour et l'état du retour ok
        switch (requestCode) {
            case CAMERA_REQUEST_CODE :
                // Récupération de l'image
                Bitmap imageCamera = BitmapFactory.decodeFile(photoPath);
                // Afficher l'image
                scanImageView.setImageBitmap(imageCamera);
                break;
            case GALLERY_REQUEST_CODE : // Vérifie si une image est récupérée
                // Accès à l'image à partir de data
                Uri selectedImage = data.getData();
                // Mémorisation du path précis
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Curseur d'accès au chemin de l'image
                Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Position sur la première ligne (normalement une seule)
                cursor.moveToFirst();
                // Récupération du chemin précis de l'image
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgPath = cursor.getString(columnIndex);
                cursor.close();
                // Récupération de l'image
                Bitmap imageGallery = BitmapFactory.decodeFile(imgPath);
                // Redimentionner l'image
                imageGallery = changeSizeBitmap(imageGallery, 0.5f);
                // Affichage de l'image
                scanImageView.setImageBitmap(imageGallery);
                break;
        }

    }

    public void takePicture(View view) {
        if(ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Crée un intent pour ouvrir une fenêtre pour prendre la photo
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Test pour contrôler que l'intent peut être géré
            if(intent.resolveActivity(getPackageManager()) != null) {
                // Créer un nom de fichier unique
                String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File photoFile = File.createTempFile("photo" + time, ".jpg", photoDir);
                    // Enregistrer le chemin complet
                    photoPath = photoFile.getAbsolutePath();
                    // Créer l'URI
                    Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
                    // Transfert uri vers l'intent pour enregistrement photo dans fichier temporaire
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    // Ouvrir l'activity par rapport à l'intent
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            cameraRequestPermission();
        }

    }

    private Bitmap changeSizeBitmap(Bitmap bitmap, float proportion) {
        // Métrique qui permet de récupérer les dimensions de l'écran
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // Taille de l'écran (et affecter les proportions)
        float screenHeight = metrics.heightPixels*proportion;
        float screenWidth = metrics.widthPixels*proportion;
        // Taille de l'image
        float bitmapHeight = bitmap.getHeight();
        float bitmapWidth = bitmap.getWidth();
        // Calcul du ratio entre taille image et écran
        float ratioHeight = screenHeight/bitmapHeight;
        float ratioWidth = screenWidth/bitmapWidth;
        // Récupération du plus petit ratio
        float ratio = Math.min(ratioHeight, ratioWidth);
        // Redimentionnement de l'image par rapport au ratio
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmapWidth*ratio), (int) (bitmapHeight*ratio), true);
        // envoie la nouvelle image
        return bitmap;
    }

        /*
    public void askCameraPermissions(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE) {
            if(grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "L'accès à la caméra est requis", Toast.LENGTH_LONG).show();
            }

        }
    }
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView scanImageView = (ImageView) findViewById(R.id.scanImageView); // DOIT ETRE DECLAREE ICI !!!!!!!!!!!!!!!!!!!!!!
        if(requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            scanImageView.setImageBitmap(image);
        }
    }
     */

    /**
     * Ajout d'une nouvelle bouteille
     */
    private void addWineBottle() {
        ((FloatingActionButton) findViewById(R.id.btnAdd)).setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtCountry.getText().toString().equals("")) {
                    //btnAdd.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.green_light));


                    txtRegion.setError("tt");
                    Drawable warning = getResources().getDrawable(R.drawable.ic_add_black_24dp);
                    //add an error icon to yur drawable files
                    warning.setBounds(0, 0, warning.getIntrinsicWidth(), warning.getIntrinsicHeight());

                    txtRegion.setCompoundDrawables(null,null, warning,null);
                    //txtRegion.setErrorColor(Color.BLUE);
                    btnAdd.setBackgroundColor(Color.RED);
                }



                String country = "";
                String region = "";
                String domain = "";
                String appellation = "";
                String wineColor = "";
                int year = 0;
                int number = 0;
                int estimate = 0;
                String image = "";
                int like = 0;

                Tools tool = new Tools();
                // Récupération des données saisies


                try {

                    if(btnRed.getAlpha() == 1f) {
                        wineColor = "Rouge";
                    } else if(btnRose.getAlpha() == 1f) {
                        wineColor = "Rose";
                    } else if(btnWhite.getAlpha() == 1f) {
                        wineColor = "Blanc";
                    } else if(btnChamp.getAlpha() == 1f) {
                        wineColor = "Effervescent";
                    }


                    country = txtCountry.getText().toString();
                    region = txtRegion.getText().toString();
                    domain = txtDomain.getText().toString();
                    appellation = txtAppellation.getText().toString();
                    year = Integer.parseInt(nbYear.getText().toString());
                    number = Integer.parseInt(nbNumber.getText().toString());
                    estimate = Integer.parseInt(nbEstimate.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Bitmap bitmap = ((BitmapDrawable) scanImageView.getDrawable()).getBitmap();
                    image = (tool.bitmapToString(bitmap));
                } catch(Exception e) {
                    e.printStackTrace();
                }

                afficheResult(country, region, wineColor, domain, appellation, year, number, estimate, image, like);




                Snackbar snackbar = Snackbar.make(mainLayout, "Bouteille enregistrée !", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Annuler", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO REMOVE THE LAST BOTTLE
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.orange));

                // On change la couleur du texte de la Snackbar
                View snackbarView = snackbar.getView();
                TextView tv = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                tv.setTextColor(Color.parseColor("#67828f"));
                // On change la couleur du background de la Snackbar
                snackbarView.setBackgroundColor(Color.parseColor("#2F3B40"));
                snackbar.setDuration(3000).show();
            }
        });

    }

    private void afficheResult(String country, String region, String wineColor, String domain, String appellation, Integer year, Integer number, Integer estimate, String image, Integer like) {
        this.controle.createWineBottle(country, region, wineColor, domain, appellation, year, number, estimate, image, like,  this);
    }

    /**
     * Récupération de la wineBottle si elle a été SERIALISEE et si le champ pays n'est pas null
     */
    private void recoverWineBottle() {
        if(controle.getCountry() != null) {
            txtCountry.setText(controle.getCountry());
            txtRegion.setText(controle.getRegion());
            txtDomain.setText(controle.getDomain());
            txtAppellation.setText(controle.getAppellation());
            nbYear.setText(controle.getYear().toString());
            nbNumber.setText(controle.getNumber().toString());
            nbEstimate.setText(controle.getEstimate().toString());

            //rdFemme.setChecked(true);
            //if(controle.getSexe() == 1){
            //    rdHomme.setChecked(true);
            //}
            // Simule le clic sur le bouton calcul
            //((Button) findViewById(R.id.btnAdd)).performClick();
        }
    }

    /**
     * Chargement et récupération des infos du fichier JSon
     */
    public void getJsonCountries() {
        String json;
        try {
            //Load File
            InputStream is = getResources().openRawResource(R.raw.countries);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countrylist.add(jsonObject.getString("name"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(),countrylist.toString(),Toast.LENGTH_LONG).show();
    }

    private void recoverJsonCountries() {
        getJsonCountries();
        AutoCompleteTextView textCountries = (AutoCompleteTextView) findViewById(R.id.textCountry);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countrylist);
        textCountries.setAdapter(adapter);
    }

    private void recoverFABWineColor() {
        ImageButton redWineButton = (ImageButton) findViewById(R.id.redWineButton);
        ImageButton roseWineButton = (ImageButton) findViewById(R.id.roseWineButton);
        ImageButton whiteWineButton = (ImageButton) findViewById(R.id.whiteWineButton);
        ImageButton champWineButton = (ImageButton) findViewById(R.id.champWineButton);
        Intent intent = getIntent();
        if (intent != null) {
            String str = "";
            if (intent.hasExtra("redWine")) {
                redWineButton.setAlpha(1f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(0.3f);
            } else if (intent.hasExtra("roseWine")) {
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(1f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(0.3f);
            } else if (intent.hasExtra("whiteWine")) {
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(1f);
                champWineButton.setAlpha(0.3f);
            } else {
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(1f);
            }

        }
    }

    /*
    private void pulsar() {
        PulsatorLayout pulsatorLayout = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsatorLayout.start();
    }
     */

    public void wineColorSelector(View view) {
        ImageButton redWineButton = (ImageButton) findViewById(R.id.redWineButton);
        ImageButton roseWineButton = (ImageButton) findViewById(R.id.roseWineButton);
        ImageButton whiteWineButton = (ImageButton) findViewById(R.id.whiteWineButton);
        ImageButton champWineButton = (ImageButton) findViewById(R.id.champWineButton);
        int id = view.getId();
        switch (id) {
            case R.id.redWineButton:
                redWineButton.setAlpha(1f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(0.3f);
                break;
            case R.id.roseWineButton:
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(1f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(0.3f);
                break;
            case R.id.whiteWineButton:
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(1f);
                champWineButton.setAlpha(0.3f);
                break;
            case R.id.champWineButton:
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(1f);
                break;
        }
    }

    /**
     * Méthode qui gère la progressBar de AddActivity
     */
    private void progressBar() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //holder.progressBar.isIndeterminate(false);
        progressBar.setMax(7);
        progressBar.setProgress(0);

        txtCountry = (AutoCompleteTextView) findViewById(R.id.textCountry);
        txtCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.incrementProgressBy(1);
                        //progressBar.setProgress(progressBar.getProgress() + 1);
                        check = false;
                    }
                }
                else {
                    progressBar.incrementProgressBy(-1);
                    //progressBar.setProgress(progressBar.getProgress() - 1);
                    //progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#282828"), android.graphics.PorterDuff.Mode.SRC_IN);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtRegion = (EditText) findViewById(R.id.textRegion);
        txtRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.incrementProgressBy(1);
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtDomain = (EditText) findViewById(R.id.textDomain);
        txtDomain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtAppellation = (EditText) findViewById(R.id.textAppellation);
        txtAppellation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nbYear = (EditText) findViewById(R.id.nbYear);
        nbYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nbNumber = (EditText) findViewById(R.id.nbNumber);
        nbNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nbEstimate = (EditText) findViewById(R.id.nbEstimate);
        nbEstimate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });




        //if(!txtCountry.getText().toString().isEmpty() && !txtRegion.getText().toString().isEmpty()) {
        //    progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#159700"), android.graphics.PorterDuff.Mode.SRC_IN);
        //}
        //txtCountry.getText().toString().isEmpty();
        //txtRegion.getText().toString().isEmpty();
        //txtDomain.getText().toString().isEmpty();
        //txtAppellation.getText().toString().isEmpty();
        //nbYear.getText().toString().isEmpty();
        //nbNumber.getText().toString().isEmpty();
        //nbEstimate.getText().toString().isEmpty();
        //if(!txtCountry.getText().toString().isEmpty() && !txtRegion.getText().toString().isEmpty()) {
        //    progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#159700"), android.graphics.PorterDuff.Mode.SRC_IN);
        //}

    }

    private CurvedBottomNavigationView.OnNavigationItemSelectedListener customBottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.user:
                            //Toast.makeText(AddActivity.this, "USER", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.cellar:
                            //Toast.makeText(AddActivity.this, "CELLAR", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.scan:
                            //Toast.makeText(AddActivity.this, "SCAN", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.like:
                            //Toast.makeText(AddActivity.this, "LIKE", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.search:
                            //Toast.makeText(AddActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            };




}

