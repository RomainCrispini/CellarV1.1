package com.romain.cellarv1.outils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.romain.cellarv1.vue.CellarActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyAdapterCellarRecyclerView extends RecyclerView.Adapter<MyAdapterCellarRecyclerView.CellarViewHolder> {

    ArrayList<WineBottle> wineBottleArrayList;

    Context mContext;

    // Constructeur
    public MyAdapterCellarRecyclerView(Context mContext, ArrayList<WineBottle> arrayList) {
        wineBottleArrayList = arrayList;
        this.mContext = mContext;
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

        public final ImageButton favorite;
        public final ImageButton delete;


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

            favorite = itemView.findViewById(R.id.favorite);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    @NonNull
    @Override
    public CellarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_cellar_custom_list_view, viewGroup, false);
        CellarViewHolder cellarViewHolder = new CellarViewHolder(v);

        //return new CellarViewHolder(v);
        return cellarViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CellarViewHolder holder, int position) {

        final WineBottle wineBottle = wineBottleArrayList.get(position);



        holder.cardView.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), wineBottle.getFavorite() + " " + wineBottle.getRandom(), Toast.LENGTH_SHORT).show();
            }
        });




        holder.favorite.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pour set 1 dans la propriété favorite d'une bottle si elle n'est pas déjà set
                String valueRandom = wineBottle.getRandom();
                AccesLocal accesLocal = new AccesLocal(mContext);

                if(wineBottle.getFavorite().matches("0")) {
                    accesLocal.addLikeToABottle(valueRandom);

                } else if(wineBottle.getFavorite().matches("1")) {
                    accesLocal.removeLikeToABottle(valueRandom);
                }

                //notifyDataSetChanged();
                //Toast.makeText(v.getContext(), wineBottle.getFavorite(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar snackbar = Snackbar.make(v, "Etes vous sûr de vouloir supprimer cette bouteille de votre cave ?", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Oui", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO REMOVE THE LAST BOTTLE
                        // Pour Supprimer une bouteille de la bdd
                        String valueRandom = wineBottle.getRandom();
                        AccesLocal accesLocal = new AccesLocal(mContext);
                        accesLocal.takeOutBottle(valueRandom);
                        //Toast.makeText(v.getContext(), valueRandom, Toast.LENGTH_SHORT).show();
                    }
                });
                        //.setActionTextColor(getResources().getColor(R.color.orange));
                        //.setActionTextColor(Color.parseColor("#FFF"));

                // On change la couleur du texte de la Snackbar
                View snackbarView = snackbar.getView();
                TextView tv = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                tv.setTextColor(Color.parseColor("#67828f"));
                // On change la couleur du background de la Snackbar
                snackbarView.setBackgroundColor(Color.parseColor("#2F3B40"));
                snackbar.setDuration(3000).show();



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

        // On set la CardView d'un coeur coloré si la bouteille est favorite = 1, noir si favorite = 0
        switch(currentItem.getFavorite()) {
            case "0" :
                holder.favorite.setImageResource(R.drawable.icon_like_cardview);
                //holder.favorite.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                break;
            case "1" :
                holder.favorite.setImageResource(R.drawable.icon_like_cardview);
                //holder.favorite.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                holder.favorite.setColorFilter(Color.parseColor("#D57400"));
                break;
        }


    }

    @Override
    public int getItemCount() {
        return this.wineBottleArrayList.size();
    }

}
