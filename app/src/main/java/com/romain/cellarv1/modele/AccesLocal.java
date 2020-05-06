package com.romain.cellarv1.modele;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.romain.cellarv1.outils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AccesLocal {

    // Propriétés
    private String nomBase = "cellar.sqlite";
    private Integer versionBase = 1;
    private MySQLiteOpenHelper accesBD; // Accès à la BDD SQLite
    private SQLiteDatabase bd; // Propriété qui permet de créer des canaux pour lire et/ou écrire dans la BDD

    /**
     * Constructeur, quand on instanciera cette classe (il faudra y envoyer le context)
     * @param context
     */
    public AccesLocal(Context context) {
        accesBD = new MySQLiteOpenHelper(context, nomBase, null, versionBase);
    }

    /**
     * Ajout d'une bouteille dans la BDD
     * @param wineBottle
     */
    public void add(WineBottle wineBottle) {
        bd = accesBD.getWritableDatabase();
        String requete = "insert into bottle (dateaddnewbottle, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random) values ";
        requete += "(\""+ wineBottle.getDateAddNewBottle() +"\", \""+wineBottle.getCountry()+"\", \""+wineBottle.getRegion()+"\", \""+wineBottle.getWineColor()+" \", \""+wineBottle.getDomain()+"\", \""+wineBottle.getAppellation()+"\", "+wineBottle.getYear()+", "+wineBottle.getApogee()+", "+wineBottle.getNumber()+", "+wineBottle.getEstimate()+", \""+wineBottle.getImage()+"\", \""+wineBottle.getFavorite()+"\", \""+wineBottle.getWish()+"\", \""+wineBottle.getRandom()+"\")";
        bd.execSQL(requete);
        bd.close();
    }

    public void takeOutBottle(String random) {
        bd = accesBD.getWritableDatabase();
        bd.delete("bottle", "random = ?", new String[] { random });
        bd.close();
    }

    public void addLikeToABottle(String random) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("favorite", "1");
        String where = "random=?";
        String[] whereArgs = new String[] {String.valueOf(random)};
        bd.update("bottle", args, where, whereArgs);

        //bd.execSQL(requete);
        bd.close();
    }

    public void removeLikeToABottle(String random) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("favorite", "0");
        String where = "random=?";
        String[] whereArgs = new String[] {String.valueOf(random)};
        bd.update("bottle", args, where, whereArgs);

        //bd.execSQL(requete);
        bd.close();
    }

    public void updateBottle(String random, String country, String region, String domain, String appellation, int year, int apogee, int estimate, String favorite, String wish) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("country", country);
        args.put("region", region);
        args.put("domain", domain);
        args.put("appellation", appellation);
        args.put("year", year);
        args.put("apogee", apogee);
        args.put("estimate", estimate);
        args.put("favorite", favorite);
        args.put("wish", wish);

        String where = "random=?";
        bd.update("bottle", args, "random=" + random, null);

    }

    /**
     * Récupération de la liste des bouteilles enregistrées dans le cellier
     * @return Liste exhaustive des bouteilles de vin
     */
    public List<WineBottle> recoverWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by dateaddnewbottle asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String image = cursor.getString(10);
            String favorite = cursor.getString(11);
            String wish = cursor.getString(12);
            String random = cursor.getString(13);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    /**
     * Récupération de la liste des bouteilles enregistrées dans le cellier
     * @return Liste exhaustive des bouteilles de vin dont favorite = 1
     */
    public List<WineBottle> recoverLikeWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where favorite = '1'";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String image = cursor.getString(10);
            String favorite = cursor.getString(11);
            String wish = cursor.getString(12);
            String random = cursor.getString(13);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> recoverWineBottleWishlist() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where wish = '1'";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String image = cursor.getString(10);
            String favorite = cursor.getString(11);
            String wish = cursor.getString(12);
            String random = cursor.getString(13);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortMapWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by region asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String image = cursor.getString(10);
            String favorite = cursor.getString(11);
            String wish = cursor.getString(12);
            String random = cursor.getString(13);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortColorWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by winecolor desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String image = cursor.getString(10);
            String favorite = cursor.getString(11);
            String wish = cursor.getString(12);
            String random = cursor.getString(13);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortYearWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by year asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String image = cursor.getString(10);
            String favorite = cursor.getString(11);
            String wish = cursor.getString(12);
            String random = cursor.getString(13);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    /**
     * TODO SORT APOGEE WINEBOTTLELIST
     */
    public List<WineBottle> sortApogeeWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by country desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String image = cursor.getString(10);
            String favorite = cursor.getString(11);
            String wish = cursor.getString(12);
            String random = cursor.getString(13);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    /**
     * Récupération de la dernière bouteille de la BDD
     * @return wineBottle
     */
    public WineBottle getLastWineBottle() {
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle = null;
        String requete = "select * from bottle";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToLast();
        if(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String image = cursor.getString(10);
            String favorite = cursor.getString(11);
            String wish = cursor.getString(12);
            String random = cursor.getString(13);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, image, favorite, wish, random);
        }
        cursor.close();
        bd.close();
        return wineBottle;
    }
}
