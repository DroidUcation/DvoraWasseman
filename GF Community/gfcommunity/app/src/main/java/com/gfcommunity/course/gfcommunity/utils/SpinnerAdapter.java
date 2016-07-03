package com.gfcommunity.course.gfcommunity.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(Context context, String[] objects, int textViewResourceId) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}