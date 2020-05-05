package com.romain.cellarv1.outils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.romain.cellarv1.R;
import com.romain.cellarv1.controleur.Controle;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.vue.BottleActivity;
import com.romain.cellarv1.vue.CellarActivity;
import com.romain.cellarv1.vue.CellarListFragment;
import com.romain.cellarv1.vue.UserActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyAdapterCellarRecyclerView extends RecyclerView.Adapter<MyAdapterCellarRecyclerView.CellarViewHolder> {

    private ArrayList<WineBottle> wineBottleArrayList;
    private Context mContext;
    private Dialog popup;

    // Constructeur
    public MyAdapterCellarRecyclerView(Context mContext, ArrayList<WineBottle> arrayList) {

        wineBottleArrayList = arrayList;
        this.mContext = mContext;
        popup = new Dialog(mContext);
    }

    public static class CellarViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageWineColor;
        public ImageView image;
        public TextView region;
        public TextView appellation;
        public TextView domain;
        public TextView year;

        public CardView cardView;
        public CardView pastilleImageBottle;

        public ImageView imageBottle;

        public final ToggleButton favorite;
        public final ImageButton delete;

        public Dialog popup;


        @SuppressLint("ResourceType")
        public CellarViewHolder(@NonNull View itemView) {
            super(itemView);
            imageWineColor = itemView.findViewById(R.id.imageWineColorListView);
            image = itemView.findViewById(R.id.imageBottleListView);
            region = itemView.findViewById(R.id.regionListView);
            appellation = itemView.findViewById(R.id.appellationListView);
            domain = itemView.findViewById(R.id.domainListView);
            year = itemView.findViewById(R.id.yearListView);

            cardView = itemView.findViewById(R.id.cardView);

            pastilleImageBottle = itemView.findViewById(R.id.pastilleImageBottle);

            imageBottle = itemView.findViewById(R.id.imageBottle);

            favorite = itemView.findViewById(R.id.favorite);
            delete = itemView.findViewById(R.id.delete);

        }
    }

    @NonNull
    @Override
    public CellarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_cellar_custom_list_view, viewGroup, false);
        CellarViewHolder cellarViewHolder = new CellarViewHolder(v);

        popup.setContentView(R.layout.popup_take_out_bottle);

        return new CellarViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final CellarViewHolder holder, final int position) {

        final WineBottle wineBottle = wineBottleArrayList.get(position);
        
        
        holder.cardView.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO PROBLEME DE DATE
                Date date = wineBottle.getDateAddNewBottle();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String strDate = dateFormat.format(date);

                Intent intent = new Intent(mContext, BottleActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                intent.putExtra("wineColor", wineBottle.getWineColor());
                intent.putExtra("imageBottle", wineBottle.getImage());
                intent.putExtra("country", wineBottle.getCountry());
                intent.putExtra("region", wineBottle.getRegion());
                intent.putExtra("domain", wineBottle.getDomain());
                intent.putExtra("appellation", wineBottle.getAppellation());
                intent.putExtra("millesime", wineBottle.getYear().toString());
                intent.putExtra("apogee", wineBottle.getApogee().toString());
                intent.putExtra("estimate", wineBottle.getEstimate().toString());
                intent.putExtra("favorite", wineBottle.getFavorite());

                intent.putExtra("random", wineBottle.getRandom());


                /*
                // Tente de faire une transition visuelle vers BottleActivity (fiche vin)
                ImageView imageBottle = cellarViewHolder.imageBottle;
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(imageBottle, "imageTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, pairs);
                mContext.startActivity(intent, options.toBundle());
                 */

                mContext.startActivity(intent);
            }
        });

        holder.favorite.setText(null);
        holder.favorite.setTextOn(null);
        holder.favorite.setTextOff(null);
        holder.favorite.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pour set 1 dans la propriété favorite d'une bottle si elle n'est pas déjà set
                String valueRandom = wineBottle.getRandom();
                AccesLocal accesLocal = new AccesLocal(mContext);

                if (wineBottle.getFavorite().matches("0")) {
                    accesLocal.addLikeToABottle(valueRandom);

                    //MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(mContext, wineBottleArrayList);
                    //myAdapterCellarRecyclerView.notifyDataSetChanged();

                //} else if(wineBottle.getFavorite().matches("1") && holder.favorite.getDrawable().getColorFilter().equals(Color.parseColor("#97C58D"))) {
                } else if(wineBottle.getFavorite().matches("1")) {
                    accesLocal.removeLikeToABottle(valueRandom);
                }



                /*
                ArrayList<WineBottle> wineBottleArrayList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();
                MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(mContext, wineBottleArrayList);
                myAdapterCellarRecyclerView.notifyDataSetChanged();


                mRecyclerView.setHasFixedSize(true);


                //mLayoutManager = new LinearLayoutManager(getContext());
                //mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new MyAdapterCellarRecyclerView(getContext(), wineBottleArrayList);

                mRecyclerView.setAdapter(mAdapter);

                 */





            }
        });


        holder.delete.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btnAccept = (Button) popup.findViewById(R.id.btnAccept);
                Button btnDenie = (Button) popup.findViewById(R.id.btnDenie);
                ImageView imageWineColor = (ImageView) popup.findViewById(R.id.imageWineColor);
                ImageView imageBottle = (ImageView) popup.findViewById(R.id.imageBottle);
                TextView region = (TextView) popup.findViewById(R.id.region);
                TextView domain = (TextView) popup.findViewById(R.id.domain);
                TextView appellation = (TextView) popup.findViewById(R.id.appellation);
                TextView millesime = (TextView) popup.findViewById(R.id.millesime);
                TextView number = (TextView) popup.findViewById(R.id.number);

                popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Tools tools = new Tools();
                imageBottle.setImageBitmap(tools.stringToBitmap(wineBottle.getImage()));
                switch(wineBottle.getWineColor().trim()) {
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

                // On retire 1 au nombre de bouteilles
                String bottleNumber = String.valueOf(wineBottle.getNumber() - 1);
                number.setText(bottleNumber);

                region.setText(wineBottle.getRegion());
                domain.setText(wineBottle.getDomain());
                appellation.setText(wineBottle.getAppellation());
                millesime.setText((wineBottle.getYear()).toString());

                popup.show();


                btnAccept.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String valueRandom = wineBottle.getRandom();
                        AccesLocal accesLocal = new AccesLocal(mContext);
                        accesLocal.takeOutBottle(valueRandom);
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

        // On applique l'animation sur la pastille de la bouteille
        holder.pastilleImageBottle.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_image_cardview));

        // On applique l'animation sur la CardView
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_animation_cardview));

        // On set les infos dans le cardview layout
        WineBottle currentItem = wineBottleArrayList.get(position);

        Tools tools = new Tools();
        holder.image.setImageBitmap(tools.stringToBitmap(currentItem.getImage()));

        holder.region.setText(currentItem.getRegion());
        holder.appellation.setText(currentItem.getAppellation());
        holder.domain.setText(currentItem.getDomain());
        holder.year.setText(currentItem.getYear().toString());

        // On set la couleur du vin sous la pastille de l'image de l'étiquette
        switch(currentItem.getWineColor().trim()) {
            case "Rouge" :
                holder.imageWineColor.setImageResource(R.drawable.red_wine_listview);
                break;
            case "Rose" :
                holder.imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                break;
            case "Blanc" :
                holder.imageWineColor.setImageResource(R.drawable.white_wine_listview);
                break;
            case "Effervescent" :
                holder.imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                break;
        }




        // On set la CardView d'un coeur coloré si la bouteille est favorite = 1, rien si favorite = 0
        switch(currentItem.getFavorite()) {
            case "0" :
                holder.favorite.setChecked(false);
                break;
            case "1" :
                holder.favorite.setChecked(true);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return this.wineBottleArrayList.size();
    }

}
