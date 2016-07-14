package com.gfcommunity.course.gfcommunity.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.recipes.AddRecipeActivity;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.data.recipes.RecipesContentProvider;
import com.gfcommunity.course.gfcommunity.recyclerView.DividerItemDecoration;
import com.gfcommunity.course.gfcommunity.recyclerView.recipes.RecipesAdapter;
import com.gfcommunity.course.gfcommunity.utils.NetworkConnectedUtil;
import com.gfcommunity.course.gfcommunity.utils.SpinnerAdapter;


public class RecipesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener , AdapterView.OnItemSelectedListener {
        private int loaderID = 0; // Identifies a particular Loader being used in this component
        private RecipesAdapter recipesAdapter;
        private RecyclerView recyclerView;
        private ProgressBar progressBar;
        private String selectedCategory;
        private Spinner categoriesSpinner;
        private Context context;
        private ImageView filterCategoryImg;
        private TextView filteredByTxtView;
        private ImageView cancelFilterImageView;
        private int check = 0;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.fragment_recipe, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
                //RecyclerView
                recyclerView = (RecyclerView) view.findViewById(R.id.recipes_recycler_view);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

                progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

                //filter imageView
                filterCategoryImg = (ImageView) view.findViewById(R.id.filter_category);
                filterCategoryImg.setOnClickListener(this);

                //filtered by text view
                filteredByTxtView = (TextView) view.findViewById(R.id.filtered_by_textView);

                cancelFilterImageView = (ImageView) view.findViewById(R.id.cancel_filter_imageView);
                cancelFilterImageView.setOnClickListener(this);

                //Recipes Categories spinner
                categoriesSpinner = (Spinner) view.findViewById(R.id.recipe_categories_spinner);
                String[] categoriesArrayTemp = getResources().getStringArray(R.array.recipe_categories_array);
                String[] categoriesArray = new String[(categoriesArrayTemp.length) + 1];
                System.arraycopy(categoriesArrayTemp, 0, categoriesArray, 0, categoriesArrayTemp.length);
                categoriesArray[categoriesArrayTemp.length] = getResources().getString(R.string.select_recipe_category);
                SpinnerAdapter dataAdapter = new SpinnerAdapter(context, categoriesArray, android.R.layout.simple_spinner_item);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// Drop down layout style - list view with radio button
                categoriesSpinner.setAdapter(dataAdapter);
                categoriesSpinner.setSelection(dataAdapter.getCount());// show hint
                categoriesSpinner.setOnItemSelectedListener(this);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                this.context = getContext();
                getLoaderManager().initLoader(loaderID, null, this); //Initializes the CursorLoader
        }

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
                String strCategory = null;
                if (args != null) {
                        String str = "";
                        str = args.getString("Category");
                        if (str.length() > 0) {
                                str = str.replaceAll("'", "''");
                                strCategory = SharingInfoContract.RecipesEntry.CATEGORY + " LIKE '" + str + "'";

                        }
                }
                return new CursorLoader(getActivity(), RecipesContentProvider.RECIPES_CONTENT_URI, null, strCategory, null, null);
        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                progressBar.setVisibility(View.GONE);
                if (cursor != null && cursor.moveToFirst()) {
                        recipesAdapter = new RecipesAdapter(context, cursor);
                        recyclerView.setAdapter(recipesAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        this.getView().setBackgroundColor(Color.TRANSPARENT);
                        //Set empty state for RecyclerView if no recipes found
                } else {
                        recyclerView.setVisibility(View.GONE);
                        this.getView().setBackgroundResource(R.drawable.no_records);
                }

        }

        @Override
        public void onLoaderReset(Loader loader) {

        }

        @Override
        public void onClick(View view) {
                switch (view.getId()) {
                        case R.id.filter_category:
                                categoriesSpinner.setVisibility(View.VISIBLE);
                                filterCategoryImg.setVisibility(View.GONE);
                                break;

                        case R.id.cancel_filter_imageView:
                                cancelFilterImageView.setVisibility(View.GONE);
                                filteredByTxtView.setVisibility(View.GONE);
                                filterCategoryImg.setVisibility(View.VISIBLE);
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
                selectedCategory = parent.getItemAtPosition(position).toString();
                if (check++ > 0) {
                        filteredByTxtView.setVisibility(View.VISIBLE);
                        filteredByTxtView.setText(getString(R.string.filtered_by) + " " + selectedCategory);
                        categoriesSpinner.setVisibility(View.GONE);
                        cancelFilterImageView.setVisibility(View.VISIBLE);
                        Bundle b = new Bundle();
                        b.putCharSequence("Category", selectedCategory);
                        getLoaderManager().restartLoader(loaderID, b, this);
                }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public static Fragment getInstance() {
                return new RecipesFragment();
        }
}



