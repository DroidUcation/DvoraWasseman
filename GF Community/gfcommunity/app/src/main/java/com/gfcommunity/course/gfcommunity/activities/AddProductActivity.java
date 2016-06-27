package com.gfcommunity.course.gfcommunity.activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.products.InsertProductLoader;
import com.gfcommunity.course.gfcommunity.utils.UploadFileUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.OutputStream;


public class AddProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Uri>, View.OnClickListener, AdapterView.OnItemSelectedListener, UploadFileUtil.OnuploadCompletedListener {
    private Button addProductBtn;
    private EditText productNameEditTxt;
    private EditText storeNameEditTxt;
    private EditText storeUrlEditTxt;
    private EditText storePhoneEditTxt;
    private EditText commentEditTxt;
    private EditText storeStreetEditTxt;
    private EditText storeHouseNoEditTxt;
    private int loaderID = 0;
    private String logTag = AddProductActivity.class.getName();
    private String selectedCity;
    private ImageView productImg;
    private Button addProductImgBtn;
    private OutputStream outFile;
    private Bitmap bitmap;
    private String productImgPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initializeViews();//Define views and bind them to events
    }

    /**
     * Define views and bind them to events
     */
    private void initializeViews() {
        //Set autocompleteAddressView adapter
        /*AutoCompleteTextView autocompleteAddressView = (AutoCompleteTextView) findViewById(R.id.address_edit_txt);
        autocompleteAddressView.setAdapter(new PlacesAutoCompleteAdapter(this , R.layout.autocomplete_list_item));*/

        //Define views
        addProductBtn  = (Button) findViewById(R.id.add_product_btn);
        addProductBtn.setTransformationMethod(null);  //Ignore automatic upper case
        productNameEditTxt  = (EditText) findViewById(R.id.product_name_edit_text);
        storeNameEditTxt  = (EditText) findViewById(R.id.store_edit_txt);
        storeUrlEditTxt  = (EditText) findViewById(R.id.store_url_edit_txt);
        storePhoneEditTxt  = (EditText) findViewById(R.id.store_phone_edit_txt);
        Spinner citiesSpinner = (Spinner) findViewById(R.id.citiesSpinner);
        storeStreetEditTxt = (EditText) findViewById(R.id.store_street_edit_txt);
        storeHouseNoEditTxt = (EditText) findViewById(R.id.store_house_no_edit_txt);
        commentEditTxt = (EditText) findViewById(R.id.product_comment_txt);
        addProductImgBtn = (Button)findViewById(R.id.btnSelectProductImg);
        addProductImgBtn.setTransformationMethod(null);  //Ignore automatic upper case
        productImg = (ImageView)findViewById(R.id.viewImage);

        //Bind views to Listener
        addProductBtn.setOnClickListener(this);
        citiesSpinner.setOnItemSelectedListener(this);
        productNameEditTxt.addTextChangedListener(new MyTextWatcher(productNameEditTxt));
        storeNameEditTxt.addTextChangedListener(new MyTextWatcher(storeNameEditTxt));
        addProductImgBtn.setOnClickListener(this);

        //Cities spinner
        citiesSpinner.setPrompt(getResources().getString(R.string.city));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cities_array));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// Drop down layout style - list view with radio button
        citiesSpinner.setAdapter(dataAdapter);
    }

    @Override
    public Loader<Uri> onCreateLoader(int id, Bundle args) {
        // Upload product image to firebase storage



        String downloadUrlPath = args.getString("downloadUrlPath");
        ContentValues values = new ContentValues();
        String productName = productNameEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, !TextUtils.isEmpty(productName) ? productName : "");
        String storeName = storeNameEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, !TextUtils.isEmpty(storeName) ? storeName : "");
        String storeUrl = storeUrlEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.STORE_URL, !TextUtils.isEmpty(storeUrl) ? storeUrl : "");
        values.put(SharingInfoContract.ProductsEntry.CREATED_AT, DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        String storePhone = storePhoneEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.PHONE, !TextUtils.isEmpty(storePhone) ? storePhone : "");
        values.put(SharingInfoContract.ProductsEntry.CITY, selectedCity);
        String storeStreet = storeStreetEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.STREET, !TextUtils.isEmpty(storeStreet) ? storeStreet : "");
        String storeHouseNo = storeHouseNoEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, !TextUtils.isEmpty(storeHouseNo) ? storeHouseNo : "");
        String comment = commentEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.COMMENT, !TextUtils.isEmpty(comment) ? comment : "");

        if(downloadUrlPath != null) {
//            String imgUrl = downloadUrlPath.getPath();
            values.put(SharingInfoContract.ProductsEntry.IMAGE_URL, !TextUtils.isEmpty(downloadUrlPath) ? downloadUrlPath : "");
        }
        return new InsertProductLoader(this, values);
    }

    @Override
    public void onLoadFinished(Loader<Uri> loader, Uri data) {
        Log.i(logTag, "Insert product succssed: "+ productNameEditTxt.getText().toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this,"Product is inserted successfully",Toast.LENGTH_SHORT).show();//TODO: Show inserted successfully popup
    }

    @Override
    public void onLoaderReset(Loader<Uri> loader) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_product_btn: //Start Add new product activity
                if(isValidatedForm()) { //Validate inputs
//                    getSupportLoaderManager().initLoader(loaderID, null, this).forceLoad();//Initializes the Insert Loader
                    UploadFileUtil.uploadFile(this, productImgPath,this);
                }
                break;
            case R.id.btnSelectProductImg:
                selectImage(); //select image from camera or gallery
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCity = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Create alert dialog with options to add image from gallery or camera
     */
    private void selectImage() {
        Uri selectedImage;
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add product image");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "product.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }

                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }

                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });

        builder.show();

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImage = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File product : f.listFiles()) {
                    if (product.getName().equals("product.jpg")) {
                        f = product;
                        break;
                    }
                }
                productImgPath = f.getAbsolutePath();

            } else if (requestCode == 2) {
                 selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                productImgPath = c.getString(columnIndex);
                c.close();
                Log.w(logTag, productImgPath);
            }

            if(!TextUtils.isEmpty(productImgPath)){

                Picasso.with(this)
                        .load(selectedImage)
                        .resize(productImg.getHeight(),productImg.getWidth())
                        .placeholder(R.drawable.filter) //TODO: put product icon
                        .error(R.drawable.filter) //TODO: put product icon
//                        .error(R.drawable.filter) //TODO: put product icon
//                        .fit()
                       .centerCrop()

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

        }

    }



    /**
     * Validating form
     */
    private boolean isValidatedForm() {
        boolean isValid = false;
        boolean isProductNameFilled = validateRequiredText(productNameEditTxt);
        boolean isStoreNameFilled = validateRequiredText(storeNameEditTxt);
        boolean isStroreAddressOrStoreUrlFilled = validateRequiredAddressOrUrl(storeUrlEditTxt, storeStreetEditTxt, storeHouseNoEditTxt); //Validate store address or store url (one of them is required)
        if(isProductNameFilled && isStoreNameFilled && isStroreAddressOrStoreUrlFilled) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Set view error message by view id and set view focus
     * @param view
     */
    private void setErrorMsg(EditText view){
        String errMessage = getString(R.string.required_err_msg_default);
        switch (view.getId()) {
            case R.id.product_name_edit_text:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg),getString(R.string.product_name)));
                break;
            case R.id.store_edit_txt:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg),getString(R.string.store_name)));
                break;
            case R.id.store_street_edit_txt:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg),getString(R.string.street)));
                break;
            case R.id.store_house_no_edit_txt:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg),getString(R.string.house_no)));
                break;
            case R.id.store_url_edit_txt:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg),getString(R.string.store_url)));
                break;
        }
        view.setError(errMessage);
        requestFocus(view);
    }

    private boolean validateRequiredText(EditText editTextView) {
        if (TextUtils.isEmpty(editTextView.getText().toString().trim())) {
            setErrorMsg(editTextView);
            return false;
        }
        return true;
    }

    /**
     * Check if store url or address is not empty (one of them is required)
     * @param storeUrlEditTxt
     * @param storeStreetEditTxt
     * @param storeHouseNoEditTxt
     * @return isValid-boolean
     */
    private boolean validateRequiredAddressOrUrl(EditText storeUrlEditTxt, EditText storeStreetEditTxt, EditText storeHouseNoEditTxt) {
        boolean isUrlEmpty = TextUtils.isEmpty(storeUrlEditTxt.getText().toString().trim());
        boolean isCityEmpty = TextUtils.isEmpty(selectedCity.toString().trim());
        boolean isStreetEmpty = TextUtils.isEmpty(storeStreetEditTxt.getText().toString().trim());
        boolean isHouseNoEmpty = TextUtils.isEmpty(storeHouseNoEditTxt.getText().toString().trim());
        boolean isValid = true;
        if(isUrlEmpty && (isCityEmpty || isStreetEmpty || isHouseNoEmpty)) {
            if(isCityEmpty) {
                //TODO: set spinner error
                isValid = false;
            }
            if(isStreetEmpty) {
                setErrorMsg(storeStreetEditTxt);
                isValid = false;
            }
            if(isHouseNoEmpty) {
                setErrorMsg(storeHouseNoEditTxt);
                isValid = false;
            }

            if(isUrlEmpty) {
                setErrorMsg(storeUrlEditTxt);
                isValid = false;
            }
        }
        return isValid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onUrlReceived(Uri uri) {
        Bundle bundle= new Bundle();
        bundle.putString("downloadUrlPath",uri.toString());
        getSupportLoaderManager().initLoader(loaderID,bundle , this).forceLoad();//Initializes the Insert Loader


    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.product_name_edit_text:
                    validateRequiredText((EditText)view);
                    break;
                case R.id.store_edit_txt:
                    validateRequiredText((EditText)view);
                    break;
            }
        }
    }
}
