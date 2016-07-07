package com.gfcommunity.course.gfcommunity.loaders;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;


import com.gfcommunity.course.gfcommunity.data.recipes.RecipesContentProvider;

/**
 * Insert recipe by asyncTaskLoader
 */
public class InsertRecipeLoader extends AsyncTaskLoader<Uri> {
    ContentValues values;
    Context context;
    public InsertRecipeLoader(Context context) {
        super(context);
    }

    public InsertRecipeLoader(Context context, ContentValues values) {
        super(context);
        this.values = values;
        this.context = context;
    }

    @Override
    public Uri loadInBackground() {
        return context.getContentResolver().insert(RecipesContentProvider.RECIPES_CONTENT_URI, values);
    }
}