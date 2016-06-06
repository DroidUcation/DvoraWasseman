package com.finalproject.course.finalproject;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.finalproject.course.finalproject.data.ProductsContentProvider;
import com.finalproject.course.finalproject.data.SharingInfoContract;
import com.finalproject.course.finalproject.recyclerView.Product;
import com.finalproject.course.finalproject.recyclerView.ProductsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {
    private List<Product> productList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.products_recycler_view);
        productsAdapter = new ProductsAdapter(productList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productsAdapter);

        prepareProductData();
    }

    /**
     * Get product list by products contentProvider
     */
    private void prepareProductData() {
        Cursor c = getContentResolver().query(ProductsContentProvider.PRODUCTS_CONTENT_URI, null, null, null,null);
        if (c != null && c.moveToFirst()) {
            do{
                Product product = new Product();
                product.setDateModified(new Date(c.getLong(c.getColumnIndex(SharingInfoContract.ProductsEntry.CREATED_AT))));
                product.setImage(R.mipmap.ic_launcher);
                product.setProductName(c.getString(c.getColumnIndex(SharingInfoContract.ProductsEntry.PRODUCT_NAME)));
                product.setStoreName(c.getString(c.getColumnIndex(SharingInfoContract.ProductsEntry.STORE_NAME)));
                product.setUserID(c.getString(c.getColumnIndex(SharingInfoContract.ProductsEntry.USER_ID)));
                productList.add(product);
            }
            while (c.moveToNext());
        }

        Product product = new Product("flour", "Native Food", new Date(), "1", R.mipmap.ic_launcher);
        productList.add(product);

        Product product2 = new Product("flour", "Native Food", new Date(), "2", R.mipmap.ic_launcher);
        productList.add(product2);

        Product product3 = new Product("flour", "Native Food", new Date(), "1", R.mipmap.ic_launcher);
        productList.add(product3);

        Product product4 = new Product("flour", "Native Food", new Date(), "2", R.mipmap.ic_launcher);
        productList.add(product4);
        Product product5 = new Product("flour", "Native Food", new Date(), "1", R.mipmap.ic_launcher);
        productList.add(product5);

        Product product6 = new Product("flour", "Native Food", new Date(), "2", R.mipmap.ic_launcher);
        productList.add(product6);
        Product product7 = new Product("flour", "Native Food", new Date(), "1" ,R.mipmap.ic_launcher);
        productList.add(product7);

        Product product8 = new Product("flour", "Native Food", new Date(), "2" ,R.mipmap.ic_launcher);
        productList.add(product8);

        productsAdapter.notifyDataSetChanged();
    }
}
