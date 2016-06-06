package com.finalproject.course.finalproject.data;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.text.format.DateFormat;

public class InnerTest {
    //Insert
    public void testInsertProducts(Context cnx) {
        ContentValues values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "PRODUCT_NAME");
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "STORE_NAME");
        values.put(SharingInfoContract.ProductsEntry.CREATED_AT, DateFormat.format("dd-MM-yyyy hh:mm:ss",new java.util.Date()).toString());
        values.put(SharingInfoContract.ProductsEntry.PHONE, "1234567");
        values.put(SharingInfoContract.ProductsEntry.CITY, "CITY");
        values.put(SharingInfoContract.ProductsEntry.STREET, "STREET");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, "HOUSE_NO");
        values.put(SharingInfoContract.ProductsEntry.COMMENT, "COMMENT");
        cnx.getContentResolver().insert(ProductsContentProvider.PRODUCTS_CONTENT_URI, values);
    }

    public int testUpdateProduct(Context cnx, long rowID) {
        ContentValues values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.CITY, "Updated_city");
        return cnx.getContentResolver().update(
                ContentUris.withAppendedId(ProductsContentProvider.PRODUCTS_CONTENT_URI, rowID),
                values,
                null,
                null);
    }

    public int testDeleteProduct(Context cnx, long rowID) {
        return cnx.getContentResolver().delete(
                ContentUris.withAppendedId(ProductsContentProvider.PRODUCTS_CONTENT_URI, rowID),
                null,
                null);
    }

}
