package com.gfcommunity.course.gfcommunity.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider;

/**
 * Update product by asyncTaskLoader
 */
public class UpdateProductLoader extends AsyncTaskLoader<Integer> {
    ContentValues values;
    Context context;
    String whereCondition;
    public UpdateProductLoader(Context context) {
        super(context);
    }

    public UpdateProductLoader(Context context, ContentValues values, String whereCondition) {
        super(context);
        this.values = values;
        this.context = context;
        this.whereCondition=whereCondition;
    }

    @Override
    public Integer loadInBackground() {
        return context.getContentResolver().update(ProductsContentProvider.PRODUCTS_CONTENT_URI,values,whereCondition,null);
    }
}
