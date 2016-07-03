package com.gfcommunity.course.gfcommunity.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.ProductDetailsActivity;
import com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.model.Product;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Provide views to RecyclerView with data from productList.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements Filterable{
    static Cursor cursor;
    static Context context;
    private List<Product> mProducts;
    private String picassoLogTag = "Picasso productsAdapter";

    @Override
    public Filter getFilter() {
       return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    mProducts = getFilteredList(constraint);
                    if (mProducts != null) {
                        results.values = mProducts;
                        results.count = mProducts.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    private ArrayList<Product> getFilteredList(CharSequence constraint) {

        String query = constraint.toString().toLowerCase();
        String selection = SharingInfoContract.ProductsEntry.PRODUCT_NAME + " LIKE '" + query +"'";

        Cursor cursor = context.getContentResolver().query(ProductsContentProvider.PRODUCTS_CONTENT_URI, null, selection, null, null);

        ArrayList<Product> productsList = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    productsList.add(setProductValues(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return productsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title, subTitle, text;
        private CircularImageView productImg;
        private static SparseArray<Product> productsMap = new SparseArray<Product>();//Products map mapped by product ID

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.row_title);
            subTitle = (TextView) view.findViewById(R.id.row_subtitle);
            text = (TextView) view.findViewById(R.id.row_text);
            productImg = (CircularImageView) view.findViewById(R.id.row_img);
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
                product = setProductValues(cursor);
                productsMap.put(productID, product );
            }

            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("selected_item",product); //Pass selected product to ProductDetailsActivity
            context.startActivity(intent);
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
                product.setAddress(String.format(context.getResources().getString(R.string.address),
                        cursor.getInt(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.HOUSE_NO)),
                        cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.STREET)),
                        cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.CITY))));
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

        //Set product image by picasso
        String productImgPath = cursor.getString(cursor.getColumnIndex(SharingInfoContract.ProductsEntry.IMAGE_URI));
        //String productImgPath = "https://firebasestorage.googleapis.com/v0/b/gf-community.appspot.com/o/images%2Fproduct_img146697445826120160610_141506.jpg?alt=media&token=5130c587-38af-4e03-a401-cfea8afc08dc";
        if(!TextUtils.isEmpty(productImgPath)) {
            Picasso.with(context)
                    .load(productImgPath)
                    .placeholder(R.drawable.common_full_open_on_phone) //TODO: put loading icon
                    .error(R.drawable.filter) //TODO: put product icon
                    .into( holder.productImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(picassoLogTag, "set product image was succeeded");
                        }

                        @Override
                        public void onError() {
                            Log.d(picassoLogTag, "set product image was failed");

                        }
                    });
        }

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}
