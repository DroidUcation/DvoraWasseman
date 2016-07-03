package com.gfcommunity.course.gfcommunity.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gfcommunity.course.gfcommunity.model.Product;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.utils.DateFormatUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Product product;
    private String storePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        product = (Product) getIntent().getSerializableExtra("selected_item"); //get current product passed from ProductsAdapter

        ImageView storePhoneImg = (ImageView) findViewById(R.id.store_phone_img);
        storePhoneImg.setOnClickListener(this);

        setProductValues(); //Set product details in the textViews
    }

    /**
     * Set product details in the textViews
     */
    private void setProductValues() {
        TextView productNameTxt = (TextView) findViewById(R.id.product_name_txt);
        String productName = product.getProductName();
        productNameTxt.setText(!TextUtils.isEmpty(productName) ? productName : "");

        ImageView productImg = (ImageView) findViewById(R.id.product_img);
        String imgUri = product.getImgUrl();
        if(!TextUtils.isEmpty(imgUri)){
            Picasso.with(this)
                    //.load("http://apod.nasa.gov/apod/image/1606/Omega_Crete_200mm_50schedler.jpg")
                    .load(imgUri)
                    .placeholder(R.xml.progress) //TODO: put loading icon
                    .error(R.drawable.filter) //TODO: put product icon
                    .into(productImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso", "success");
                        }

                        @Override
                        public void onError() {
                            Log.d("Picasso", "onError");

                        }
                    });
        }

        TextView storeNameTxt = (TextView) findViewById(R.id.store_name_txt);
        String storeName = product.getStoreName();
        storeNameTxt.setText(!TextUtils.isEmpty(storeName) ? storeName : "");

        TextView storeAddressTxt = (TextView) findViewById(R.id.address_txt);
        String storeAddress = product.getAddress();
        storeAddressTxt.setText(!TextUtils.isEmpty(storeAddress) ? storeAddress : "");

        TextView storeUrlTxt = (TextView) findViewById(R.id.store_url_txt);
        storeUrlTxt.setText("");
        String storeUrl = product.getStoreUrl();
        if(!TextUtils.isEmpty(storeUrl)) {
            storeUrl = storeUrl.trim();
            String linkedText = String.format("<a href=\"%s\">%s</a>", "http://"+storeUrl, storeUrl);
            storeUrlTxt.setText(Html.fromHtml(linkedText));
            Linkify.addLinks(storeUrlTxt, Linkify.WEB_URLS);
        }

        TextView store_phone_txt = (TextView) findViewById(R.id.store_phone_txt);
        storePhone = product.getPhone();
        store_phone_txt.setText(!TextUtils.isEmpty(storePhone) ? storePhone : "");

        TextView productCommentTxt = (TextView) findViewById(R.id.product_comment_txt);
        String comment = product.getComment();
        productCommentTxt.setText(!TextUtils.isEmpty(comment) ? comment : "");

        TextView productUserUploadedTxt = (TextView) findViewById(R.id.product_user_uploaded_txt);
        String productUserUploadedDate = DateFormatUtil.DATE_FORMAT_DDMMYYYY.format(product.getCreatedAt()).toString();
        //TODO: set user name
        String productUserUploaded = String.format(getResources().getString(R.string.user_uploaded_text), "user name", productUserUploadedDate);
        productUserUploadedTxt.setText(!TextUtils.isEmpty(productUserUploaded) ? productUserUploaded : "");
    }

    @Override
    public void onClick(View v) {
        Intent dialIntent;
        switch (v.getId()) {
            case R.id.store_phone_img: //Start ACTION_DIAL
                dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:"+storePhone));
                startActivity(dialIntent);
                break;
            case R.id.store_phone_txt:
                dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:"+storePhone));
                startActivity(dialIntent);
                break;
        }
    }
}
