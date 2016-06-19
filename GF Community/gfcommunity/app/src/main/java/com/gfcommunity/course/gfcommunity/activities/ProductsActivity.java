package com.gfcommunity.course.gfcommunity.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.data.ProductsContentProvider;
import com.gfcommunity.course.gfcommunity.recyclerView.DividerItemDecoration;
import com.gfcommunity.course.gfcommunity.recyclerView.ProductsAdapter;




public class ProductsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener , AdapterView.OnItemSelectedListener{
    private int loaderID = 0; // Identifies a particular Loader being used in this component
    private ProductsAdapter productsAdapter;
    private RecyclerView recyclerView;
    private ImageView noRecordsImg;
    private ProgressBar progressBar;
    private String selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        getSupportLoaderManager().initLoader(loaderID, null, this); //Initializes the CursorLoader

        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.products_recycler_view);
        if(recyclerView != null) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        }
        noRecordsImg = (ImageView) findViewById(R.id.no_results_img);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //Adding fab
        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.add_fab);
        addFab.setOnClickListener(this);
        Spinner citiesSpinner = (Spinner) findViewById(R.id.citiesSpinner);
        //Cities spinner
        citiesSpinner.setPrompt(getResources().getString(R.string.city));
        citiesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ProductsContentProvider.PRODUCTS_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //Set
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        if (cursor != null && cursor.moveToFirst()) {
            productsAdapter = new ProductsAdapter(this, cursor);
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
                Intent intent = new Intent(this, AddProductActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCity = parent.getItemAtPosition(position).toString();
        productsAdapter.getFilter().filter(selectedCity,new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
