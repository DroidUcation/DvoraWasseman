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
                    + SharingInfoContract.RecipesEntry.CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + SharingInfoContract.RecipesEntry.USER_ID + " TEXT"+");";

    public SharingInfoDatabaseHelper(Context context){
        super(context,"SharingInfoDB",null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_PRODUCTS);

        ContentValues values = new ContentValues();

        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Store name");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Product name");
        values.put(SharingInfoContract.ProductsEntry.USER_ID, "User ID");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 25);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Yigal Alon");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Bnei Brak");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME,null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
