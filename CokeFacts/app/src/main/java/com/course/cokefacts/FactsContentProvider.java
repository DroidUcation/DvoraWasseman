package com.course.cokefacts;

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
import android.support.annotation.Nullable;

/**
 * Facts Content Provider
 */
public class FactsContentProvider extends ContentProvider{
    static  final String PROVIDER_NAME = "com.course.cokefacts.FactsContentProvider";
    static final String URL = "content://" + PROVIDER_NAME;
    static final Uri CONTENT_URI = Uri.parse(URL);
    /* static final int FACTS = 1;
     static final int FACTS_ID = 2;
     static final UriMatcher uriMatcher;
     static{
         uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
         uriMatcher.addURI(PROVIDER_NAME, "facts", FACTS);
         uriMatcher.addURI(PROVIDER_NAME, "facts/#", FACTS_ID);
     }*/
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        FactsDataBaseHelper dbHelper= new FactsDataBaseHelper(context);
        /** * Create a write able database which will trigger its * creation if it doesn't already exist. */
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

   @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
       qb.setTables(FactsContract.FactsEntry.TABLE_NAME);
       /*switch (uriMatcher.match(uri)) {
           case FACTS:
               break;
           case FACTS_ID:
               qb.appendWhere(FactsContract.FactsEntry._ID + "=" + uri.getPathSegments().get(1));
               break;
           default:
               throw new IllegalArgumentException("Unknown URI " + uri);
       }*/

       Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
       return c;
   }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /** * Add a new fact record */
        long rowID = db.insert(FactsContract.FactsEntry.TABLE_NAME, "", values);
        /** * If record is added successfully */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            //getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
