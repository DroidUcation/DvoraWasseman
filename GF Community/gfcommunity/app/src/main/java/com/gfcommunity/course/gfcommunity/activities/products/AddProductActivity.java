package com.gfcommunity.course.gfcommunity.activities.products;

import android.content.ContentUris;
import android.content.Context;
import android.support.v7.app.AlertDialog;;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider;
import com.gfcommunity.course.gfcommunity.loaders.InsertProductLoader;
import com.gfcommunity.course.gfcommunity.firebase.storage.UploadFile;
import com.gfcommunity.course.gfcommunity.loaders.UpdateProductLoader;
import com.gfcommunity.course.gfcommunity.model.Product;
import com.gfcommunity.course.gfcommunity.recyclerView.products.ProductsAdapter;
import com.gfcommunity.course.gfcommunity.utils.DateFormatUtil;
import com.gfcommunity.course.gfcommunity.utils.SpinnerAdapter;

import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, UploadFile.OnuploadCompletedListener {
    private Button addProductBtn;
    private EditText productNameEditTxt;
    private EditText storeNameEditTxt;
    private EditText storeUrlEditTxt;
    private EditText storePhoneEditTxt;
    private EditText commentEditTxt;
    private EditText storeStreetEditTxt;
    private EditText storeHouseNoEditTxt;
    private Spinner citiesSpinner;
    private String[] cityList;
    private int loaderID = 1;//Insert products loader ID
    private int updateLoaderID = 2;//Update products loader ID
    private String logTag = AddProductActivity.class.getName();
    private String selectedCity;
    private ImageView productImg;
    private FloatingActionButton addProductImgBtn;
    private Uri selectedImage;
    private String productName;
    private String storeName;
    private String selectedProductId;
    private boolean updateActivity = false;
    private Context context;

    private LoaderManager.LoaderCallbacks<Uri> insertProductLoaderListener = new LoaderManager.LoaderCallbacks<Uri>() {
        @Override
        public Loader<Uri> onCreateLoader(int id, Bundle args) {
            return new InsertProductLoader(context, setProductValues(args));
        }

        @Override
        public void onLoadFinished(Loader<Uri> loader, Uri data) {
            Log.i(logTag, "Insert product succeed: " + productName);
            Toast.makeText(context, String.format(getString(R.string.product_added_msg), productName), Toast.LENGTH_SHORT).show();//TODO: Show inserted successfully popup
            //TODO: check if user city is same as product city
            sendNotification(data); //Send notification
            finish(); //Close this activity and go back to Main Activity
        }

        @Override
        public void onLoaderReset(Loader<Uri> loader) {

        }
    };


    private LoaderManager.LoaderCallbacks<Integer> updateProductLoaderListener = new LoaderManager.LoaderCallbacks<Integer>() {

        @Override
        public Loader<Integer> onCreateLoader(int id, Bundle args) {
            Uri uri = ContentUris.withAppendedId(ProductsContentProvider.PRODUCTS_CONTENT_URI, Integer.valueOf(selectedProductId));
            return new UpdateProductLoader(context, setProductValues(args), uri);
        }

        @Override
        public void onLoadFinished(Loader<Integer> loader, Integer data) {
            Log.i(logTag, "Update product succeed: " + productName);
            Toast.makeText(context, String.format(getString(R.string.product_saved_msg), productName), Toast.LENGTH_SHORT).show();//TODO: Show inserted successfully popup
            finish(); //Close this activity and go back to Main Activity
        }

        @Override
        public void onLoaderReset(Loader<Integer> loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        context = getBaseContext();
        initializeViews();//Define views and bind them to events

        Intent intent = getIntent();
        selectedProductId = intent.getStringExtra("selectedProductId");
        if (selectedProductId != null) {
            initialEditedProduct();
        }
    }

    private ContentValues setProductValues(Bundle args) {
        String downloadUrlPath = args != null ? args.getString("downloadUrlPath") : "";
        ContentValues values = new ContentValues();
        productName = productNameEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, !TextUtils.isEmpty(productName) ? productName : "");
        storeName = storeNameEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, !TextUtils.isEmpty(storeName) ? storeName : "");
        String storeUrl = storeUrlEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.STORE_URL, !TextUtils.isEmpty(storeUrl) ? storeUrl : "");
        values.put(SharingInfoContract.ProductsEntry.CREATED_AT, DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        String storePhone = storePhoneEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.PHONE, !TextUtils.isEmpty(storePhone) ? storePhone : "");
        values.put(SharingInfoContract.ProductsEntry.CITY, !(selectedCity.equals(getString(R.string.city_spinner_title))) ? selectedCity : "");
        String storeStreet = storeStreetEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.STREET, !TextUtils.isEmpty(storeStreet) ? storeStreet : "");
        String storeHouseNo = storeHouseNoEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, !TextUtils.isEmpty(storeHouseNo) ? storeHouseNo : "");
        String comment = commentEditTxt.getText().toString();
        values.put(SharingInfoContract.ProductsEntry.COMMENT, !TextUtils.isEmpty(comment) ? comment : "");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI, !TextUtils.isEmpty(downloadUrlPath) ? downloadUrlPath : "");
        return values;
    }

    private void initialEditedProduct() {
        updateActivity = true;
        Uri _uri = ContentUris.withAppendedId(ProductsContentProvider.PRODUCTS_CONTENT_URI, Long.valueOf(selectedProductId));
        Cursor cursor = this.getContentResolver().query(_uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Product product = ProductsAdapter.setProductValues(cursor);

            String city = product.getCity();
            int indexCity = 0;
            for (; indexCity < cityList.length; indexCity++) {
                if (cityList[indexCity] == city)
                    break;
            }
            citiesSpinner.setSelection(indexCity);
            storeStreetEditTxt.setText(product.getStreet());
            storeHouseNoEditTxt.setText(product.getHoseNum());
            commentEditTxt.setText(product.getComment());
            String productName = product.getProductName();
            productNameEditTxt.setText(!TextUtils.isEmpty(productName) ? productName : "");


            String imgUrl = product.getImgUrl();
            if (!TextUtils.isEmpty(imgUrl)) {
                //imgUrl = "https://firebasestorage.googleapis.com/v0/b/gf-community.appspot.com/o/images%2Fproduct_img14676540477212278?alt=media&token=d6a5d69a-b644-410d-89d8-14bfff807833";
                Glide.with(this).load(imgUrl)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.xml.progress) //TODO: put loading icon
                        .error(R.drawable.filter) //TODO: put product icon
                        .into(productImg);
            }


            String storeName = product.getStoreName();
            storeNameEditTxt.setText(!TextUtils.isEmpty(storeName) ? storeName : "");
            storeUrlEditTxt.setText("");
            String storeUrl = product.getStoreUrl();
            if (!TextUtils.isEmpty(storeUrl)) {
                storeUrl = storeUrl.trim();
                String linkedText = String.format("<a href=\"%s\">%s</a>", "http://" + storeUrl, storeUrl);
                storeUrlEditTxt.setText(Html.fromHtml(linkedText));
                Linkify.addLinks(storeUrlEditTxt, Linkify.WEB_URLS);
            }

            String storePhone = product.getPhone();
            storePhoneEditTxt.setText(!TextUtils.isEmpty(storePhone) ? storePhone : "");
            String comment = product.getComment();
            commentEditTxt.setText(!TextUtils.isEmpty(comment) ? comment : "");
            addProductBtn.setText(getString(R.string.save_product));

        }
    }

    /**
     * Define views and bind them to events
     */
    private void initializeViews() {
        //Set autocompleteAddressView adapter
        /*AutoCompleteTextView autocompleteAddressView = (AutoCompleteTextView) findViewById(R.id.address_edit_txt);
        autocompleteAddressView.setAdapter(new PlacesAutoCompleteAdapter(this , R.layout.autocomplete_list_item));*/

        //Define views
        addProductBtn = (Button) findViewById(R.id.add_product_btn);
        addProductBtn.setTransformationMethod(null);  //Ignore automatic upper case
        productNameEditTxt = (EditText) findViewById(R.id.product_name_edit_text);
        storeNameEditTxt = (EditText) findViewById(R.id.store_edit_txt);
        storeUrlEditTxt = (EditText) findViewById(R.id.store_url_edit_txt);
        storePhoneEditTxt = (EditText) findViewById(R.id.store_phone_edit_txt);
        citiesSpinner = (Spinner) findViewById(R.id.citiesSpinner);
        storeStreetEditTxt = (EditText) findViewById(R.id.store_street_edit_txt);
        storeHouseNoEditTxt = (EditText) findViewById(R.id.store_house_no_edit_txt);
        commentEditTxt = (EditText) findViewById(R.id.product_comment_txt);
        addProductImgBtn = (FloatingActionButton) findViewById(R.id.btnSelectProductImg);
        productImg = (ImageView) findViewById(R.id.product_img);

        //Bind views to Listener
        addProductBtn.setOnClickListener(this);
        citiesSpinner.setOnItemSelectedListener(this);
        productNameEditTxt.addTextChangedListener(new ProductTextWatcher(productNameEditTxt));
        storeNameEditTxt.addTextChangedListener(new ProductTextWatcher(storeNameEditTxt));
        addProductImgBtn.setOnClickListener(this);

        //Cities spinner
        String[] cityArray = getResources().getStringArray(R.array.cities_array);
        String[] cityList = new String[(cityArray.length) + 1];
        System.arraycopy(cityArray, 0, cityList, 0, cityArray.length);
        cityList[cityArray.length] = getResources().getString(R.string.city_spinner_title);
        citiesSpinner.setPrompt(getResources().getString(R.string.city_spinner_title));
        SpinnerAdapter dataAdapter = new SpinnerAdapter(this, cityList, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// Drop down layout style - list view with radio button
        citiesSpinner.setAdapter(dataAdapter);
        citiesSpinner.setSelection(dataAdapter.getCount());// show hint
    }


    /*
     * Create pending intent for details activity which be opened from notification
     * @param productUri: relevant product uri to get product details
     * @return PendingIntent
     */
    public PendingIntent createPendingIntent(Uri productUri) {
        //Add notification action
        Intent productDetailsIntent = new Intent(this, ProductDetailsActivity.class);
        Cursor cursor = this.getContentResolver().query(productUri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            productDetailsIntent.putExtra("selected_item", ProductsAdapter.setProductValues(cursor)); //Pass relevant product to ProductDetailsActivity
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntentWithParentStack(productDetailsIntent);
        PendingIntent pi = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    /**
     * Send notification about new product
     */
    public void sendNotification(Uri productUri) {
        Notification n = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.product_notification_title))
                .setContentText(String.format(getString(R.string.product_notification_text), productName, storeName))
                .setSmallIcon(R.mipmap.ic_app)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setContentIntent(createPendingIntent(productUri))
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(1, n);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_product_btn: //Start Add new product activity
                if (isValidatedForm()) { //Validate inputs
                    if (selectedImage != null) {
                        UploadFile.uploadFile(this, selectedImage, this, "product"); //Upload product image to firebase
                    } else {
                        if (updateActivity) {
                            getSupportLoaderManager().initLoader(updateLoaderID, null, insertProductLoaderListener).forceLoad();//Initializes the update Loader
                        } else {
                            getSupportLoaderManager().initLoader(loaderID, null, updateProductLoaderListener).forceLoad();//Initializes the Insert Loader
                        }
                    }
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
        final CharSequence[] options = {getResources().getString(R.string.take_photo_option), getResources().getString(R.string.gallery_option), getResources().getString(R.string.cancel_option)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppAlertDialogStyle);
        builder.setTitle(getString(R.string.add_product_image_title));
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getResources().getString(R.string.take_photo_option))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "product.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals(getResources().getString(R.string.gallery_option))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals(getResources().getString(R.string.cancel_option))) {
                    dialog.dismiss();
                }

            }

        });

        builder.show();

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        selectedImage = null;
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
                selectedImage = Uri.fromFile(f);

            } else if (requestCode == 2) {
                selectedImage = data.getData();
            }

            if (!TextUtils.isEmpty(selectedImage.toString())) {

                Glide.with(this).load(selectedImage)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.products)
                        .error(R.drawable.products)
                        .into(productImg);
            }

        }

    }


    /**
     * Validating form
     */
    private boolean isValidatedForm() {
        boolean isValid = false;
        boolean isProductNameFilled = validateRequiredText(productNameEditTxt); //validate product name
        boolean isStoreNameFilled = validateRequiredText(storeNameEditTxt); //validate store name
        boolean isStroreAddressOrStoreUrlFilled = validateRequiredAddressOrUrl(storeUrlEditTxt, storeStreetEditTxt, storeHouseNoEditTxt); //Validate store address or store url (one of them is required)

        //validate store phone
        Editable storePhoneNumber = storePhoneEditTxt.getText();
        boolean isStorePhoneValid = true;
        if (storePhoneNumber != null && !TextUtils.isEmpty(storePhoneNumber.toString())) {
            isStorePhoneValid = isValidMobile(storePhoneEditTxt, storePhoneNumber.toString().trim());
        }

        //validate store web url
        Editable storeUrl = storeUrlEditTxt.getText();
        boolean isStoreUrlValid = true;
        if (storeUrl != null && !TextUtils.isEmpty(storeUrl.toString())) {
            isStoreUrlValid = isValidWebUrl(storeUrlEditTxt, storeUrl.toString().trim());
        }
        if (isProductNameFilled && isStoreNameFilled && isStroreAddressOrStoreUrlFilled && isStorePhoneValid && isStoreUrlValid) {
            isValid = true;
            cleanErrorMsg(); //Clean all error messages
        }
        return isValid;
    }

    /**
     * Set view error message by view id and set view focus
     *
     * @param view
     */
    private void setErrorMsg(EditText view) {
        String errMessage = getString(R.string.required_err_msg_default);
        switch (view.getId()) {
            case R.id.product_name_edit_text:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg), getString(R.string.product_name)));
                break;
            case R.id.store_edit_txt:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg), getString(R.string.store_name)));
                break;
            case R.id.store_street_edit_txt:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg), getString(R.string.street)));
                break;
            case R.id.store_house_no_edit_txt:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg), getString(R.string.house_no)));
                break;
            case R.id.store_url_edit_txt:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg), getString(R.string.store_url)));
                break;
            case R.id.store_phone_edit_txt:
                errMessage = getResources().getString(R.string.not_valid_number_msg);
                break;
        }
        view.setError(errMessage);
        requestFocus(view);
    }

    /**
     * Validate required text
     *
     * @param editTextView
     * @return isValid
     */
    private boolean validateRequiredText(EditText editTextView) {
        if (TextUtils.isEmpty(editTextView.getText().toString().trim())) {
            setErrorMsg(editTextView);
            return false;
        }
        return true;
    }

    /**
     * Validate phone
     *
     * @param phone
     * @return isValid
     */
    private boolean isValidMobile(EditText editTextView, String phone) {
        boolean isValid = Patterns.PHONE.matcher(phone).matches();
        if (!isValid) {
            setErrorMsg(editTextView);
        }
        return isValid;
    }

    /**
     * Validate web url
     *
     * @param webUrl
     * @return isValid
     */
    private boolean isValidWebUrl(EditText editTextView, String webUrl) {
        boolean isValid = Patterns.WEB_URL.matcher(webUrl).matches();
        if (!isValid) {
            editTextView.setError(getResources().getString(R.string.not_valid_url_msg));
            requestFocus(editTextView);
        }
        return isValid;
    }

    private void cleanErrorMsg() {
        productNameEditTxt.setError(null);
        storeNameEditTxt.setError(null);
        storeUrlEditTxt.setError(null);
        storeStreetEditTxt.setError(null);
        storeHouseNoEditTxt.setError(null);
        storePhoneEditTxt.setError(null);
    }

    /**
     * Check if store url or address is not empty (one of them is required)
     *
     * @param storeUrlEditTxt
     * @param storeStreetEditTxt
     * @param storeHouseNoEditTxt
     * @return isValid-boolean
     */
    private boolean validateRequiredAddressOrUrl(EditText storeUrlEditTxt, EditText storeStreetEditTxt, EditText storeHouseNoEditTxt) {
        boolean isUrlEmpty = TextUtils.isEmpty(storeUrlEditTxt.getText().toString().trim());
        boolean isCityEmpty = TextUtils.isEmpty(selectedCity) || selectedCity.equals(getString(R.string.city_spinner_title));
        boolean isStreetEmpty = TextUtils.isEmpty(storeStreetEditTxt.getText().toString().trim());
        boolean isHouseNoEmpty = TextUtils.isEmpty(storeHouseNoEditTxt.getText().toString().trim());
        boolean isValid = true;
        if (isUrlEmpty && (isCityEmpty || isStreetEmpty || isHouseNoEmpty)) {
            if (isCityEmpty) {
                TextView cityItem = (TextView) citiesSpinner.getSelectedView();
                String errMessage = (String.format(getResources().getString(R.string.required_err_msg), getString(R.string.city)));
                cityItem.setError(errMessage);
                cityItem.setText(errMessage);//changes the selected item error text
                requestFocus(cityItem);
                isValid = false;
            }
            if (isStreetEmpty) {
                setErrorMsg(storeStreetEditTxt);
                isValid = false;
            }
            if (isHouseNoEmpty) {
                setErrorMsg(storeHouseNoEditTxt);
                isValid = false;
            }

            if (isUrlEmpty) {
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
        Bundle bundle = new Bundle();
        bundle.putString("downloadUrlPath", uri.toString());
        getSupportLoaderManager().initLoader(loaderID, bundle, insertProductLoaderListener).forceLoad();//Initializes the Insert Loader
    }

    private class ProductTextWatcher implements TextWatcher {

        private View view;

        private ProductTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.product_name_edit_text:
                    validateRequiredText((EditText) view);
                    break;
                case R.id.store_edit_txt:
                    validateRequiredText((EditText) view);
                    break;
            }
        }
    }
}
