package com.course.cokefacts.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.course.cokefacts.R;

/**
 * Facts DatabaseHelper
 */
public class FactsDataBaseHelper extends SQLiteOpenHelper {
    String sqlCreation="CREATE TABLE "+FactsContract.FactsEntry.TABLE_NAME+
            "(" +
            "_id INTEGER PRIMARY KEY, "+
            FactsContract.FactsEntry.COLUMN_FACT+ " TEXT NOT NULL"+
            ");";
    Context cnx;
   public FactsDataBaseHelper(Context context){
        super(context,"factsDB",null,1);
        cnx = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreation);

        ContentValues values = new ContentValues();

        //insert facts texts to DB
        values.put(FactsContract.FactsEntry.COLUMN_FACT, cnx.getResources().getString(R.string.fact1));
        db.insert(FactsContract.FactsEntry.TABLE_NAME,null, values);
        values.put(FactsContract.FactsEntry.COLUMN_FACT,cnx.getResources().getString(R.string.fact2));
        db.insert(FactsContract.FactsEntry.TABLE_NAME, null, values);
        values.put(FactsContract.FactsEntry.COLUMN_FACT,cnx.getResources().getString(R.string.fact3));
        db.insert(FactsContract.FactsEntry.TABLE_NAME, null, values);
        values.put(FactsContract.FactsEntry.COLUMN_FACT,cnx.getResources().getString(R.string.fact4));
        db.insert(FactsContract.FactsEntry.TABLE_NAME,null, values);
        values.put(FactsContract.FactsEntry.COLUMN_FACT,cnx.getResources().getString(R.string.fact5));
        db.insert(FactsContract.FactsEntry.TABLE_NAME,null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
