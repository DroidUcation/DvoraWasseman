package com.gfcommunity.course.gfcommunity.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.recyclerView.DividerItemDecoration;
import com.gfcommunity.course.gfcommunity.recyclerView.ProductsAdapter;
import com.gfcommunity.course.gfcommunity.utils.NetworkConnectedUtil;
import com.gfcommunity.course.gfcommunity.utils.SpinnerAdapter;


public class ProductsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener , AdapterView.OnItemSelectedListener{
    private int loaderID = 0; // Identifies a particular Loader being used in this component
    private ProductsAdapter productsAdapter;
    private RecyclerView recyclerView;
    private ImageView noRecordsImg;
    private ProgressBar progressBar;
    private String selectedCity;
    private Spinner citiesSpinner;
    private Context context;
    private  ImageView filterCity;
    private TextView filtered_by_textView;
    private ImageView cancelFilterImageView;
    private SpinnerAdapter dataAdapter;
    private int check=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.products_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        noRecordsImg = (ImageView) view.findViewById(R.id.no_results_img);
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);

        //Adding fab
        FloatingActionButton addFab = (FloatingActionButton)view.findViewById(R.id.add_fab);
        addFab.setOnClickListener(this);

        //filter imageView
        filterCity = (ImageView)view.findViewById(R.id.filter_city);
        filterCity.setOnClickListener(this);

        //filtered by text view
        filtered_by_textView =  (TextView)view.findViewById(R.id.filtered_by_textView);

        cancelFilterImageView= (ImageView)view.findViewById(R.id.cancel_filter_imageView);
        cancelFilterImageView.setOnClickListener(this);

        //Cities spinner
        citiesSpinner = (Spinner) view.findViewById(R.id.citiesSpinner);
        String[] cityArray = getResources().getStringArray(R.array.cities_array);
        String[] cityList = new String[(cityArray.length)+1];
        System.arraycopy(cityArray, 0, cityList, 0, cityArray.length);
        cityList[cityArray.length] = getResources().getString(R.string.city_spinner_title);
        citiesSpinner.setPrompt(getResources().getString(R.string.city_spinner_title));
        dataAdapter = new SpinnerAdapter(context, cityList, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// Drop down layout style - list view with radio button
        citiesSpinner.setAdapter(dataAdapter);
        citiesSpinner.setSelection(dataAdapter.getCount());// show hint
        citiesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        getLoaderManager().initLoader(loaderID, null, this); //Initializes the CursorLoader
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String strCity = null;
        if(args != null) {
            String str = "";
            str = args.getString("City");
            if(str.length() > 0) {
                str = str.replaceAll("'","''");
                strCity = SharingInfoContract.ProductsEntry.CITY + " LIKE '" + str +"'";

            }
        }
        return new CursorLoader(getActivity(), ProductsContentProvider.PRODUCTS_CONTENT_URI, null, strCity, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        progressBar.setVisibility(View.GONE);
        if (cursor != null && cursor.moveToFirst()) {
            productsAdapter = new ProductsAdapter(context, cursor);
            recyclerView.setAdapter(productsAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            noRecordsImg.setVisibility(View.GONE);
        //Set empty state for RecyclerView if no products found
        } else {
            recyclerView.setVisibility(View.GONE);
            noRecordsImg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_fab: //Start Add new product activity
                //Check internet connection
                if(NetworkConnectedUtil.isNetworkAvailable(context)) {
                    Intent intent = new Intent(getActivity(), AddProductActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context,getString(R.string.no_internet_connection_msg),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.filter_city:
                citiesSpinner.setVisibility(View.VISIBLE);
                check=0;
                citiesSpinner.setSelection(dataAdapter.getCount());// show hint
                filterCity.setVisibility(View.GONE);
                break;

            case R.id.cancel_filter_imageView:
                cancelFilterImageView.setVisibility(View.GONE);
                filtered_by_textView.setVisibility(View.GONE);
                filterCity.setVisibility(View.VISIBLE);
                getLoaderManager().restartLoader(loaderID, null, this);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(loaderID, null, this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCity = parent.getItemAtPosition(position).toString();
       if(check++>0){
           filtered_by_textView.setVisibility(View.VISIBLE);
           filtered_by_textView.setText("Filtered By "+selectedCity);
           citiesSpinner.setVisibility(View.GONE);
           cancelFilterImageView.setVisibility(View.VISIBLE);
           Bundle b = new Bundle();
           b.putCharSequence("City", selectedCity);
           getLoaderManager().restartLoader(loaderID, b, this);
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static Fragment getInstance() {
        return new ProductsFragment();
    }

}
