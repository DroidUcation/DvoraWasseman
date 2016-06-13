package com.gfcommunity.course.gfcommunity.products;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.gfcommunity.course.gfcommunity.data.ProductsContentProvider;

/**
 * Insert product by asyncTaskLoader
 */
public class InsertProductLoader extends AsyncTaskLoader<Uri> {
    ContentValues values;
    Context context;
    public InsertProductLoader(Context context) {
        super(context);
    }

    public InsertProductLoader(Context context, ContentValues values) {
        super(context);
        this.values = values;
        this.context = context;
    }

    @Override
    public Uri loadInBackground() {
        return context.getContentResolver().insert(ProductsContentProvider.PRODUCTS_CONTENT_URI, values);
    }
}
