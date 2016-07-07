package com.gfcommunity.course.gfcommunity.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider;

/**
 * Delete product by asyncTaskLoader
 */
public class DeleteProductLoader extends AsyncTaskLoader<Integer> {
    String whereCondition;
    Context context;
    public DeleteProductLoader(Context context) {
        super(context);
    }

    public DeleteProductLoader(Context context, String whereCondition) {
        super(context);
        this.whereCondition = whereCondition;
        this.context = context;
    }

    @Override
    public Integer loadInBackground() {
        return context.getContentResolver().delete(ProductsContentProvider.PRODUCTS_CONTENT_URI, whereCondition,null);
    }
}
