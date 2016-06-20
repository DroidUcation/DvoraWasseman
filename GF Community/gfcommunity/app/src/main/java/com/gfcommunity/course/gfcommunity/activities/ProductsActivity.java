package com.gfcommunity.course.gfcommunity.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.data.ProductsContentProvider;
import com.gfcommunity.course.gfcommunity.recyclerView.DividerItemDecoration;
import com.gfcommunity.course.gfcommunity.recyclerView.ProductsAdapter;




public class ProductsActivity extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener , AdapterView.OnItemSelectedListener{
    private int loaderID = 0; // Identifies a particular Loader being used in this component
    private ProductsAdapter productsAdapter;
    private RecyclerView recyclerView;
    private ImageView noRecordsImg;
    private ProgressBar progressBar;
    private String selectedCity;
    private Spinner citiesSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_products, container, false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(loaderID, null, this); //Initializes the CursorLoader

        //RecyclerView
        recyclerView = (RecyclerView) getView().findViewById(R.id.products_recycler_view);
        if(recyclerView != null) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        }
        noRecordsImg = (ImageView) getView().findViewById(R.id.no_results_img);
        progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);

        //Adding fab
        FloatingActionButton addFab = (FloatingActionButton) getView().findViewById(R.id.add_fab);
        addFab.setOnClickListener(this);
        //Cities spinner
        citiesSpinner = (Spinner) getView().findViewById(R.id.citiesSpinner);
        citiesSpinner.setPrompt(getResources().getString(R.string.city));
        citiesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ProductsContentProvider.PRODUCTS_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //Set
        recyclerView.setVisibility(View.VISIBLE);
        citiesSpinner.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        if (cursor != null && cursor.moveToFirst()) {
            productsAdapter = new ProductsAdapter(this, cursor);
            recyclerView.setAdapter(productsAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            citiesSpinner.setVisibility(View.VISIBLE);
            noRecordsImg.setVisibility(View.GONE);
        //Set empty state for RecyclerView if no products found
        } else {
            recyclerView.setVisibility(View.GONE);
            citiesSpinner.setVisibility(View.GONE);
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
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCity = parent.getItemAtPosition(position).toString();
        productsAdapter.getFilter().filter(selectedCity);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
