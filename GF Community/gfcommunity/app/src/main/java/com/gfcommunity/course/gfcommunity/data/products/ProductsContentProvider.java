package com.gfcommunity.course.gfcommunity.data.products;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.data.SharingInfoDatabaseHelper;

/**
 * Products ContentProvider
 */
public class ProductsContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider";
    private static final String URL = "content://" + AUTHORITY;

    //URI for products
    public static final Uri PRODUCTS_CONTENT_URI = Uri.parse(URL + "/" + SharingInfoContract.ProductsEntry.TABLE_NAME);

    //Define URI Matcher
    public static final int PRODUCTS = 1;
    public static final int PRODUCTS_ID = 2;
     static final UriMatcher uriMatcher;
     static{
         uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
         uriMatcher.addURI(AUTHORITY, SharingInfoContract.ProductsEntry.TABLE_NAME, PRODUCTS);
         uriMatcher.addURI(AUTHORITY, SharingInfoContract.ProductsEntry.TABLE_NAME + "/#",PRODUCTS_ID);
     }
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        SharingInfoDatabaseHelper dbHelper= new SharingInfoDatabaseHelper(context);
        //Create a write table database which will trigger its creation if it doesn't already exist.
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    /**
     * Select product/s according to URI
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SharingInfoContract.ProductsEntry.TABLE_NAME);
       switch (uriMatcher.match(uri)) {
           case PRODUCTS:
               break;
           case PRODUCTS_ID:
               qb.appendWhere(SharingInfoContract.ProductsEntry._ID + "=" + uri.getLastPathSegment());
               break;
           default:
               throw new IllegalArgumentException("Unknown URI " + uri);
       }

        if(!TextUtils.isEmpty(sortOrder)){
            sortOrder = SharingInfoContract.ProductsEntry.CREATED_AT + " DESC"; //Sort by modified date as default
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return c;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Add a new product record
     * @return URI of the new inserted row
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME, "", values);
        //If record is added successfully
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(PRODUCTS_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null); // notify all listeners of changes
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    /**
     * Delete a product record according to URI
     * @return count of deleted rows
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case PRODUCTS_ID:
                delCount = db.delete(
                        SharingInfoContract.ProductsEntry.TABLE_NAME,
                        getUriIdCondition(uri, selection), //Get where condition
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // notify all listeners of changes
        if (delCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delCount;
    }

    /**
     * Update a product record according to URI
     * @return count of updated rows
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateCount = 0;
        switch (uriMatcher.match(uri)) {
            case PRODUCTS_ID:
                updateCount = db.update(
                        SharingInfoContract.ProductsEntry.TABLE_NAME,
                        values,
                        getUriIdCondition(uri, selection), //Get where condition
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
        // notify all listeners of changes
        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

    /**
     * Build condition according to URI ID
     * @param uri
     * @param selection
     * @return where condition for specific ID
     */
    public String getUriIdCondition(Uri uri,  String selection){
        String idStr = uri.getLastPathSegment();
        String where = SharingInfoContract.ProductsEntry._ID + " = " + idStr;
        if (!TextUtils.isEmpty(selection)) {
            where += " AND " + selection;
        }
        return where;
    }
}
