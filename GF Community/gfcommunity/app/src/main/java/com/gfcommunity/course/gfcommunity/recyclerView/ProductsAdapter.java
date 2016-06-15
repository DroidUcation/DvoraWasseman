package com.gfcommunity.course.gfcommunity.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.gfcommunity.course.gfcommunity.Product;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.ProductDetailsActivity;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;

import java.sql.Timestamp;


/**
* Provide views to RecyclerView with data from productList.
*/
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    static Cursor cursor;
    static Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title, subTitle, text;
        private ImageView productImg;
        private static SparseArray<Product> productsMap = new SparseArray<Product>();//Products map mapped by product ID

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.row_title);
            subTitle = (TextView) view.findViewById(R.id.row_subtitle);
            text = (TextView) view.findViewById(R.id.row_text);
            productImg = (ImageView) view.findViewById(R.id.row_img);
            view.setOnClickListener(this);
        }

        /**
         * Click on RecyclerView item start product details activity:
         * Creating products map mapped by product ID
         * @param v
         */
        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            cursor.moveToPosition(position);
            int productID = cursor.getInt(cursor.getColumnIndex(SharingInfoContract.ProductsEntry._ID));
            Product product = productsMap.get(productID);
            //Set the product only if it's the first clicking (the product is not initialized to map)
            if(product == null){
                product = new Product();
                product.setProductName(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.PRODUCT_NAME)));
                //TODO: set image
                product.setStoreName(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STORE_NAME)));
                //TODO: set address
                product.setStoreUrl(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STORE_URL)));
                product.setPhone(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.PHONE)));
                product.setComment(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.COMMENT)));
                product.setUserID(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.USER_ID)));
                product.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.CREATED_AT))));
                productsMap.put(productID, product);
            }

            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("selected_item",product);
            context.startActivity(intent);
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
