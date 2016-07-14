package com.gfcommunity.course.gfcommunity.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider;

/**
 * Update product/recipe by asyncTaskLoader
 */
public class UpdateLoader extends AsyncTaskLoader<Integer> {
    private ContentValues values;
    private Context context;
    private Uri uri;

    public UpdateLoader(Context context, ContentValues values, Uri uri) {
        super(context);
        this.values = values;
        this.context = context;
        this.uri=uri;
    }

    @Override
    public Integer loadInBackground() {
        return context.getContentResolver().update(uri, values, null ,null);
    }

}
