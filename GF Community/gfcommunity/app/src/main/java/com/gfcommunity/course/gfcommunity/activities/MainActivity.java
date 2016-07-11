package com.gfcommunity.course.gfcommunity.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.SearchView;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.products.EditProductActivity;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.fragments.BlankFragment;
import com.gfcommunity.course.gfcommunity.fragments.ProductsFragment;
import com.gfcommunity.course.gfcommunity.fragments.RecipesFragment;
import com.gfcommunity.course.gfcommunity.loaders.DeleteProductLoader;
import com.gfcommunity.course.gfcommunity.recyclerView.SelectableAdapter;
import com.gfcommunity.course.gfcommunity.recyclerView.products.ProductsAdapter;
import com.gfcommunity.course.gfcommunity.utils.NetworkConnectedUtil;

public class MainActivity extends AppCompatActivity implements ProductsAdapter.ViewHolder.ClickListener, AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener , LoaderManager.LoaderCallbacks<Integer> {
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private SelectableAdapter adapter;
    private int fragmentPosition;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private int loaderID = 1;//Insert products loader ID
    private ViewPager viewPager;
    private int selectedProductId=0;
    private boolean selectionMode=false;
    static Context context;
    private int[] tabIcons = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);

        fragmentPosition = getIntent().getIntExtra("fragmentPosition", 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("GF Community");
        toolbar.inflateMenu(R.menu.toolbar);
        toolbar.setOnMenuItemClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(fragmentPosition); //select specific fragment

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setToolbar(menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private void setToolbar(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar, menu);
        menu.findItem(R.id.action_search).setVisible(!selectionMode);
        menu.findItem(R.id.action_share).setVisible(selectionMode);
        menu.findItem(R.id.action_favorites).setVisible(selectionMode);
        menu.findItem(R.id.action_edit).setVisible(selectionMode);
        menu.findItem(R.id.action_delete).setVisible(selectionMode);
}
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new BlankFragment(), getResources().getString(R.string.news_fragment_name));
        viewPagerAdapter.addFrag(ProductsFragment.getInstance(), getResources().getString(R.string.products_fragment_name));
        viewPagerAdapter.addFrag(new RecipesFragment(), getResources().getString(R.string.recipes_fragment_name));
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: change toolbar items dynamically
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                item.setChecked(true);
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                    //TODO: find selected item and pass it to EditProductActivity
                    Intent intent = new Intent(this, EditProductActivity.class);
                    intent.putExtra("selectedProductId",selectedProductId);//send product id to init
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_delete:
                item.setChecked(true);
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()){
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Delete Product")
                                        .setMessage("Are you sure you want to delete product?")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //clicked on ok button
                                                //TODO: delete selected item
                                               deleteItem();
                                            }
                                        }).create().show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteItem() {
        Bundle b = new Bundle();
        b.putCharSequence("itemIdToDelete", selectedProductId+"");
        getSupportLoaderManager().initLoader(loaderID, b, this).forceLoad();//Initializes delete Loader
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position,int productID) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
        selectedProductId = productID;

        return true;
    }

    /**
     * Toggle the selection state of an item.
     * <p/>
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        ProductsFragment.productsAdapter.toggleSelection(position);
        int count = ProductsFragment.productsAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        String itemIdToDelete=args != null ? args.getString("itemIdToDelete") :null;
        if(itemIdToDelete!=null){
            return new DeleteProductLoader(this, SharingInfoContract.ProductsEntry._ID + " = " + itemIdToDelete);
        }
       return null;
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {

    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.toolbar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    item.setChecked(true);
                    if (NetworkConnectedUtil.isNetworkAvailable(context)) {
                        //TODO: find selected item and pass it to EditProductActivity
                        Intent intent = new Intent(context, EditProductActivity.class);
                        intent.putExtra("selectedProductId",selectedProductId);//send product id to init
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case R.id.action_delete:
                    item.setChecked(true);
                    if (NetworkConnectedUtil.isNetworkAvailable(context)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!isFinishing()){
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Delete Product")
                                            .setMessage("Are you sure you want to delete product?")
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //clicked on ok button
                                                    //TODO: delete selected item
                                                    deleteItem();
                                                }
                                            }).create().show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(context, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ProductsFragment.productsAdapter.clearSelection();
            actionMode = null;
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

