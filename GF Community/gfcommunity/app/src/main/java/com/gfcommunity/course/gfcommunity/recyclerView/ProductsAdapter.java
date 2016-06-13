package com.gfcommunity.course.gfcommunity.recyclerView;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;


/**
* Provide views to RecyclerView with data from productList.
*/
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    Cursor cursor;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subTitle, text;
        private ImageView productImg;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.row_title);
            subTitle = (TextView) view.findViewById(R.id.row_subtitle);
            text = (TextView) view.findViewById(R.id.row_text);
            productImg = (ImageView) view.findViewById(R.id.row_img);
        }
    }


    public ProductsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.title.setText(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.PRODUCT_NAME)));
        holder.subTitle.setText(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STORE_NAME)));

        //Build and set address
        String address = String.format(context.getResources().getString(R.string.address),
                cursor.getInt(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.HOUSE_NO)),
                cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STREET)),
                cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.CITY)));
        holder.text.setText(address);
        holder.productImg.setImageResource(R.mipmap.ic_launcher); //TODO: Get product image
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
