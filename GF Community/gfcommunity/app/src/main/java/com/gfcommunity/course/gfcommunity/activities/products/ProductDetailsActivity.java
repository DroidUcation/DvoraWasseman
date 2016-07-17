package com.gfcommunity.course.gfcommunity.activities.products;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.util.Linkify;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gfcommunity.course.gfcommunity.activities.MainActivity;
import com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider;
import com.gfcommunity.course.gfcommunity.fragments.ProductsFragment;
import com.gfcommunity.course.gfcommunity.loaders.DeleteLoader;
import com.gfcommunity.course.gfcommunity.model.Product;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.utils.DateFormatUtil;
import com.gfcommunity.course.gfcommunity.utils.NetworkConnectedUtil;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Integer> {
    private Product product;
    private String storePhone;
    private ImageView storePhoneImg;
    private Menu mMenu;
    private Toolbar toolbar;
    private int selectedProductId;
    private Resources resources;
    private int loaderID = 1;//Delete products loader ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
        resources = getResources();
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("selected_item"); //get current product passed from ProductsAdapter
        selectedProductId = intent.getIntExtra("selectedItemId", -1);
        storePhoneImg = (ImageView) findViewById(R.id.store_phone_img);
        storePhoneImg.setOnClickListener(this);
        setProductValues(); //Set product details in the textViews
    }
    @Override
    public void onBackPressed() {
        handleOnBackPress();
    }

    private void handleOnBackPress() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragmentPosition", 1);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMenu == null) {
            mMenu = menu;
        }
        getMenuInflater().inflate(R.menu.toolbar, mMenu); // Inflate the menu; this adds items to the action bar if it is present.
        setToolbar();
        return true;
    }

    private void setToolbar() {
        toolbar.setTitle(getString(R.string.product_details));
        mMenu.findItem(R.id.action_search).setVisible(false);
        mMenu.findItem(R.id.action_share).setVisible(true);
        mMenu.findItem(R.id.action_favorites).setVisible(true);
        mMenu.findItem(R.id.action_edit).setVisible(true);
        mMenu.findItem(R.id.action_delete).setVisible(true);
        mMenu.findItem(R.id.action_navigate).setVisible(true);
    }

    /**
     * Set product details in the textViews
     */
    private void setProductValues() {
        TextView productNameTxt = (TextView) findViewById(R.id.product_name_txt);
        String productName = product.getProductName();
        productNameTxt.setText(!TextUtils.isEmpty(productName) ? productName : "");

        ImageView productImg = (ImageView) findViewById(R.id.product_img);
        String imgUrl = product.getImgUrl();
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(this).load(imgUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.products)
                    .error(R.drawable.products)
                    .into(productImg);
        }

        TextView storeNameTxt = (TextView) findViewById(R.id.store_name_txt);
        String storeName = product.getStoreName();
        storeNameTxt.setText(!TextUtils.isEmpty(storeName) ? storeName : "");


        TextView storeAddressTxt = (TextView) findViewById(R.id.address_txt);
        String storeAddress = String.format(this.resources.getString(R.string.address),
                product.getHoseNum(),
                product.getStreet(),
                product.getCity());

        if (!TextUtils.isEmpty(storeAddress)) {
            storeAddressTxt.setText(storeAddress);
        } else {
            LinearLayout addressLayout = (LinearLayout) findViewById(R.id.store_address_layout);
            addressLayout.setVisibility(View.GONE);
        }


        TextView storeUrlTxt = (TextView) findViewById(R.id.store_url_txt);
        storeUrlTxt.setText("");
        String storeUrl = product.getStoreUrl();
        if (!TextUtils.isEmpty(storeUrl)) {
            storeUrl = storeUrl.trim();
            String linkedText = String.format("<a href=\"%s\">%s</a>", "http://" + storeUrl, storeUrl);
            storeUrlTxt.setText(Html.fromHtml(linkedText));
            Linkify.addLinks(storeUrlTxt, Linkify.WEB_URLS);
        } else {
            storeUrlTxt.setVisibility(View.GONE);
        }

        TextView store_phone_txt = (TextView) findViewById(R.id.store_phone_txt);
        storePhone = product.getPhone();
        if (!TextUtils.isEmpty(storePhone)) {
            store_phone_txt.setText(storePhone);
        } else {
            LinearLayout phoneLayout = (LinearLayout) findViewById(R.id.store_phone_layout);
            phoneLayout.setVisibility(View.GONE);
        }

        TextView productCommentTxt = (TextView) findViewById(R.id.product_comment_txt);
        String comment = product.getComment();
        if (!TextUtils.isEmpty(comment)) {
            productCommentTxt.setText(comment);
        } else {
            productCommentTxt.setVisibility(View.GONE);
        }


        TextView productUserUploadedTxt = (TextView) findViewById(R.id.product_user_uploaded_txt);
        String productUserUploadedDate = DateFormatUtil.DATE_FORMAT_DDMMYYYY.format(product.getCreatedAt()).toString();
        //TODO: set user name
        String productUserUploaded = String.format(resources.getString(R.string.user_uploaded_with_date_text),
                !TextUtils.isEmpty(product.getUserID()) ? "user name" : getString(R.string.app_name),
                productUserUploadedDate);
        productUserUploadedTxt.setText(!TextUtils.isEmpty(productUserUploaded) ? productUserUploaded : "");
    }

    @Override
    public void onClick(View v) {
        Intent dialIntent;
        switch (v.getId()) {
            case R.id.store_phone_img: //Start ACTION_DIAL
                dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + storePhone));
                startActivity(dialIntent);
                finish();
                break;
            case R.id.store_phone_txt:
                dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + storePhone));
                startActivity(dialIntent);
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                item.setChecked(true);
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                    Intent intent = new Intent(this, AddProductActivity.class);
                    intent.putExtra("selectedProductId", selectedProductId);//send product id to init
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_delete:
                item.setChecked(true);
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppAlertDialogStyle);
                    builder.setTitle(resources.getString(R.string.delete_product));
                    builder.setMessage(resources.getString(R.string.dialog_msg_delete_prod));

                    builder.setPositiveButton(resources.getString(R.string.confirm_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            //clicked on ok button
                            deleteItem();// perform delete item action
                        }
                    });

                    builder.setNegativeButton(resources.getString(R.string.cancel_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            //clicked on cancel button
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.show();
                    // Must call show() prior to fetching text view
                    TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
                    messageView.setGravity(Gravity.CENTER);

                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * perform delete item action
     */
    private void deleteItem() {
        getSupportLoaderManager().restartLoader(loaderID, null, this).forceLoad();//Initializes delete Loader

    }

    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        Uri uri = ContentUris.withAppendedId(ProductsContentProvider.PRODUCTS_CONTENT_URI, selectedProductId);
        return new DeleteLoader(this, uri);

    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        ProductsFragment.productsAdapter.toggleSelection(selectedProductId);
        finish(); //Close this activity and go back to Main Activity
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }
}
