package com.finalproject.course.finalproject.activities;

import android.content.ContentValues;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.finalproject.course.finalproject.data.SharingInfoContract;
import com.finalproject.course.finalproject.location.PlacesAutoCompleteAdapter;

import com.finalproject.course.finalproject.R;
import com.finalproject.course.finalproject.products.InsertProductLoader;

import android.net.Uri;
import android.widget.EditText;


public class AddProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Uri> {
    private Button addProductBtn;
    private EditText productNameEditTxt;
    private EditText storeNameEditTxt;
    private EditText storeUrlEditTxt;
    private EditText storePhoneEditTxt;
    private EditText commentEditTxt;
    private int loaderID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        //Set autocompleteAddressView adapter
        AutoCompleteTextView autocompleteAddressView = (AutoCompleteTextView) findViewById(R.id.address_edit_txt);
        autocompleteAddressView.setAdapter(new PlacesAutoCompleteAdapter(this , R.layout.autocomplete_list_item));

        //Define views
        addProductBtn  = (Button) findViewById(R.id.add_product_btn);
        addProductBtn.setTransformationMethod(null);  //Ignore automatic upper case for lollipop
        productNameEditTxt  = (EditText) findViewById(R.id.product_name_edit_text);
        storeNameEditTxt  = (EditText) findViewById(R.id.store_edit_txt);
        storeUrlEditTxt  = (EditText) findViewById(R.id.store_url_edit_txt);
        storePhoneEditTxt  = (EditText) findViewById(R.id.store_phone_edit_txt);
        commentEditTxt = (EditText) findViewById(R.id.product_comment_edit_txt);
        getSupportLoaderManager().initLoader(loaderID, null, this);//Initializes the Insert Loader
    }

    @Override
    public Loader<Uri> onCreateLoader(int id, Bundle args) {
        ContentValues values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, productNameEditTxt.getText().toString());
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, storeNameEditTxt.getText().toString());
        values.put(SharingInfoContract.ProductsEntry.STORE_URL, storeUrlEditTxt.getText().toString());
        values.put(SharingInfoContract.ProductsEntry.CREATED_AT, DateFormat.format("dd-MM-yyyy hh:mm:ss",new java.util.Date()).toString());
        values.put(SharingInfoContract.ProductsEntry.PHONE, storePhoneEditTxt.getText().toString());
        values.put(SharingInfoContract.ProductsEntry.CITY, "CITY");
        values.put(SharingInfoContract.ProductsEntry.STREET, "STREET");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, "HOUSE_NO");
        values.put(SharingInfoContract.ProductsEntry.COMMENT, commentEditTxt.getText().toString());
        return new InsertProductLoader(this, values);
    }

    @Override
    public void onLoadFinished(Loader<Uri> loader, Uri data) {

    }

    @Override
    public void onLoaderReset(Loader<Uri> loader) {

    }
}
