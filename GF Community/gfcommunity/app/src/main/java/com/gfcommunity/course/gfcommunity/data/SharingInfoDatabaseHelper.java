package com.gfcommunity.course.gfcommunity.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;

/**
 * Sharing Information DatabaseHelper : products, recipes and tips
 */
public class SharingInfoDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; // Database Version
    // Table Create Statements

    //Products table create statement
    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE IF NOT EXISTS "
            + SharingInfoContract.ProductsEntry.TABLE_NAME + "("
            + SharingInfoContract.ProductsEntry._ID + " INTEGER PRIMARY KEY, "
            + SharingInfoContract.ProductsEntry.PRODUCT_NAME + " TEXT, "
            + SharingInfoContract.ProductsEntry.STORE_NAME + " TEXT, "
            + SharingInfoContract.ProductsEntry.STORE_URL + " TEXT, "
            + SharingInfoContract.ProductsEntry.CITY + " TEXT, "
            + SharingInfoContract.ProductsEntry.STREET + " TEXT, "
            + SharingInfoContract.ProductsEntry.HOUSE_NO + " INTEGER, "
            + SharingInfoContract.ProductsEntry.COMMENT + " TEXT, "
            + SharingInfoContract.ProductsEntry.PHONE + " TEXT, "
            + SharingInfoContract.ProductsEntry.CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + SharingInfoContract.ProductsEntry.IMAGE_URI + " TEXT, "
            + SharingInfoContract.ProductsEntry.USER_ID + " TEXT"+");";

    //Recipes table create statement
    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE IF NOT EXISTS "
                    + SharingInfoContract.RecipesEntry.TABLE_NAME + "("
                    + SharingInfoContract.RecipesEntry._ID + " INTEGER PRIMARY KEY, "
                    + SharingInfoContract.RecipesEntry.RECIPE_NAME + " TEXT, "
                    + SharingInfoContract.RecipesEntry.INGREDIENTS + " TEXT, "
                    + SharingInfoContract.RecipesEntry.INSTRUCTIONS + " TEXT, "
                    + SharingInfoContract.RecipesEntry.RECIPE_IMAGE_URl + " TEXT, "
                    + SharingInfoContract.RecipesEntry.PREPARATION_TIME + " TEXT, "
                    + SharingInfoContract.RecipesEntry.DINERS_NUMBER + " INTEGER, "
                    + SharingInfoContract.RecipesEntry.DIFFICULTY_PREPARATION + " TEXT, "
                    + SharingInfoContract.RecipesEntry.RECIPE_STORY + " TEXT, "
                    + SharingInfoContract.RecipesEntry.CATEGORY + " TEXT, "
                    + SharingInfoContract.RecipesEntry.CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + SharingInfoContract.RecipesEntry.USER_ID + " TEXT"+");";

    public SharingInfoDatabaseHelper(Context context){
        super(context,"SharingInfoDB",null,DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_RECIPES);

        insertFakeData(db);
        ContentValues values = new ContentValues();

        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Mazon min hateva");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Rye flour");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 147);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Rabi Akiva");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Tel Aviv");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME,null, values);
    }

    private void insertFakeData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Super Sal Market");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Gluten free cookies");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 25);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Herzel");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Tel Aviv");
        values.put(SharingInfoContract.ProductsEntry.PHONE, "0526984458");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI, "http://www.lance.com/assets/img/temp/snacks/gf_pb_8pk_crtn.png?1431482040");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME,null, values);

        values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Teva Banachala");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Gluten free pasta");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 98);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Nakhalat Yitskhak");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Hadera");
        values.put(SharingInfoContract.ProductsEntry.PHONE, "02-696-7474");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI, "http://makingourlifematter.com/wp-content/uploads/2014/02/gf-family-hp.png");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME,null, values);

        values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Nizat haduvdevan");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Gluten free crispy");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 58);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Ibn Gabirol");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Tel aviv");
        values.put(SharingInfoContract.ProductsEntry.PHONE, "03-696-5174");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI,"http://www.miltonsbaking.com/assets/Uploads/GlutenFreeSeaSaltLARGE.jpg");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME,null, values);

        values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Shkedia");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Gluten free kornfleks");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 56);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Katzenelson");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Ramat gan");
        values.put(SharingInfoContract.ProductsEntry.PHONE, "03-731-9958");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI,"https://nyoobserver.files.wordpress.com/2015/09/screen-shot-2015-09-22-at-11-02-05-am-e1442935619503.png?w=242&h=300");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME,null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
