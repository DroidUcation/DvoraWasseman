package com.gfcommunity.course.gfcommunity.recyclerView.products;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.products.ProductDetailsActivity;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.model.Product;
import com.gfcommunity.course.gfcommunity.recyclerView.SelectableAdapter;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.sql.Timestamp;
import java.util.List;


/**
 * Provide views to RecyclerView with data from productList.
 */
public class ProductsAdapter extends SelectableAdapter<ProductsAdapter.ViewHolder> {
    static Cursor cursor;
    static Context context;
    private ViewHolder.ClickListener clickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener {
        private TextView title, subTitle, text;
        private RelativeLayout relativeLayout;
        private CircularImageView productImg;
        private static SparseArray<Product> productsMap = new SparseArray<Product>();//Products map mapped by product ID
        private ClickListener listener;

        public ViewHolder(View view, ClickListener listener) {
            super(view);
            title = (TextView) view.findViewById(R.id.row_title);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.listRowCard);
            subTitle = (TextView) view.findViewById(R.id.row_subtitle);
            text = (TextView) view.findViewById(R.id.row_text);
            productImg = (CircularImageView) view.findViewById(R.id.row_img);
            this.listener = listener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
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
                product = setProductValues(cursor);
                productsMap.put(productID, product );
            }

            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("selected_item",product); //Pass selected product to ProductDetailsActivity
            intent.putExtra("selectedProductId", productID);//send product id to ProductDetailsActivity
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {

                int position = this.getAdapterPosition();
                cursor.moveToPosition(position);
                int productID = cursor.getInt(cursor.getColumnIndex(SharingInfoContract.ProductsEntry._ID));
                //v.findViewById(R.id.listRowCard).setBackgroundColor(Color.BLUE);
                return listener.onItemLongClicked(position,productID);
            }

            return false;
        }

        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position, int productID);
        }

    }

    public static Product setProductValues(Cursor cursor){
        Product product = new Product();
        if(cursor != null) {
            product.setProductName(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.PRODUCT_NAME)));
            product.setImgUrl(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.IMAGE_URI)));
            product.setStoreName(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STORE_NAME)));

            int houseNo = cursor.getInt(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.HOUSE_NO));
            String street = cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STREET));
            String city = cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.CITY));
            if (houseNo > 0 && !TextUtils.isEmpty(street) && !TextUtils.isEmpty(city)) {
                //Build and set address
                product.setHoseNum(houseNo);
                product.setStreet(street);
                product.setCity(city);
            }

            product.setStoreUrl(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STORE_URL)));
            product.setPhone(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.PHONE)));
            product.setComment(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.COMMENT)));
            product.setUserID(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.USER_ID)));
            product.setCreatedAt(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.CREATED_AT))));
        } else {
            Log.e(ProductsAdapter.class.getName(), "Failed to set product details");
        }
        return product;
    }
    public ProductsAdapter(Context context, Cursor cursor, ViewHolder.ClickListener clickListener) {
        this.context = context;
        this.cursor = cursor;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.title.setText(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.PRODUCT_NAME)));
        holder.subTitle.setText(cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STORE_NAME)));

        String text = ""; //Set store address or url according to user input
        String storeUrl = cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STORE_URL));
        int houseNo = cursor.getInt(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.HOUSE_NO));
        String street = cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STREET));
        String city = cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.CITY));
        if(houseNo > 0 && !TextUtils.isEmpty(street) && !TextUtils.isEmpty(city)) {
            //Build address string
            text = String.format(context.getResources().getString(R.string.address),
                    cursor.getInt(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.HOUSE_NO)),
                    cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STREET)),
                    cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.CITY)));
        } else if(!TextUtils.isEmpty(storeUrl)){
            text = storeUrl;
        }
        holder.text.setText(text);

        //Set product image by Glide
        String productImgPath = cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.IMAGE_URI));
        if(!TextUtils.isEmpty(productImgPath)) {
            Glide.with(context).load(productImgPath)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.product_circle_img)
                    .error(R.drawable.product_circle_img)
                    .into(holder.productImg);
        } else {
            holder.productImg.setImageResource(R.drawable.product_circle_img);
        }
        if(isSelected(position)){
            holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.greenAppColor));
        }
        else{
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}
