package com.gfcommunity.course.gfcommunity.recyclerView.recipes;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gfcommunity.course.gfcommunity.R;


/**
 * Provide views to RecyclerView to show ingredients or instructions.
 */
public class InstructionsAndIngredientsAdapter extends RecyclerView.Adapter<InstructionsAndIngredientsAdapter.ViewHolder>{
    static Context context;
    private String [] texts;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtView;

        public ViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.row_text_view);
        }
    }

    public InstructionsAndIngredientsAdapter(Context context, String [] texts) {
        this.context = context;
        this.texts = texts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = texts[position];
        holder.txtView.setText(text);
        if(position % 2 == 0) {
            holder.txtView.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGray));
        } else {
            holder.txtView.setBackgroundColor(ContextCompat.getColor(context, R.color.windowBackground));
        }

    }

    @Override
    public int getItemCount() {
        return texts.length;
    }
}
