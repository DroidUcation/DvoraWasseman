package com.gfcommunity.course.gfcommunity.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.gfcommunity.course.gfcommunity.Product;
import com.gfcommunity.course.gfcommunity.R;

public class ProductDetailsActivity extends AppCompatActivity {
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //get current product passed from ProductsAdapter
        product=(Product)getIntent().getSerializableExtra("selected_item");
        setValues();
    }
    private void setValues(){

        TextView product_name_txt = (TextView) findViewById(R.id.product_name_txt);
        product_name_txt.setText(product.getProductName());
        TextView address = (TextView) findViewById(R.id.address_txt);
        address.setText(product.getAddress());
        TextView store_url_txt = (TextView) findViewById(R.id.store_url_txt);
        store_url_txt.setText(product.getStoreUrl());
        TextView productComment = (TextView) findViewById(R.id.product_comment_txt);
        productComment.setText(product.getComment());
        TextView store_phone_txt = (TextView) findViewById(R.id.store_phone_txt);
        store_phone_txt.setText(product.getPhone());
        TextView product_user_upploaded_txt = (TextView) findViewById(R.id.product_user_upploaded_txt);
        product_user_upploaded_txt.setText(product.getCreatedAt().toString());
    }

}
