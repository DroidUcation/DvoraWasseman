package com.course.cokefacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Facts DatabaseHelper
 */
public class FactsDataBaseHelper extends SQLiteOpenHelper {
    String sqlCreation="CREATE TABLE "+FactsContract.FactsEntry.TABLE_NAME+
            "(" +
            "_id INTEGER PRIMARY KEY, "+
            FactsContract.FactsEntry.COLUMN_FACT+ " TEXT NOT NULL"+
            ");";
   public FactsDataBaseHelper(Context context){
        super(context,"factsDB",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
