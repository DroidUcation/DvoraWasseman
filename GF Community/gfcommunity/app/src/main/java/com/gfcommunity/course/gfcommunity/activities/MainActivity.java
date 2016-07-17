package com.gfcommunity.course.gfcommunity.activities;

import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.SearchView;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.products.AddProductActivity;
import com.gfcommunity.course.gfcommunity.activities.recipes.AddRecipeActivity;
import com.gfcommunity.course.gfcommunity.data.products.ProductsContentProvider;
import com.gfcommunity.course.gfcommunity.data.recipes.RecipesContentProvider;
import com.gfcommunity.course.gfcommunity.fragments.BlankFragment;
import com.gfcommunity.course.gfcommunity.fragments.ProductsFragment;
import com.gfcommunity.course.gfcommunity.fragments.RecipesFragment;
import com.gfcommunity.course.gfcommunity.loaders.DeleteLoader;
import com.gfcommunity.course.gfcommunity.recyclerView.SelectableAdapter;
import com.gfcommunity.course.gfcommunity.recyclerView.products.ProductsAdapter;
import com.gfcommunity.course.gfcommunity.recyclerView.recipes.RecipesAdapter;
import com.gfcommunity.course.gfcommunity.utils.FragmentEnum;
import com.gfcommunity.course.gfcommunity.utils.NetworkConnectedUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProductsAdapter.ViewHolder.ClickListener,RecipesAdapter.ViewHolder.ClickListener, AdapterView.OnItemClickListener , LoaderManager.LoaderCallbacks<Integer>
{
    private SelectableAdapter mAdapter;
    private int fragmentPosition;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private int loaderID = 1;//Delete loader ID
    private ViewPager viewPager;
    private int selectedItemId = 0;
    private int selectedRecipeId = 0;
    private boolean selectionMode = false;
    private Uri uri;
    FragmentEnum currentFragmentName;
    static Context context;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] tabIcons = {
            R.drawable.new_24,
            R.drawable.product_24,
            R.drawable.recipes_24
    };
    private Menu mMenu;
    private ResetLoaderFragment resetLoaderFragment;
    private static int lastSelectedPos = -1;
    private ShareActionProvider mShareActionProvider;
    private int lastFragmentPos =-1;

    public interface ResetLoaderFragment
    {
        void resetNow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);  // clear all scroll flags
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //Adding fab
        FloatingActionButton addFab = (FloatingActionButton)findViewById(R.id.add_fab);
        addFab.setOnClickListener(this);
        setupTabIcons();

    }

    private void handleOnBackPress() {
        if (mAdapter!= null && mAdapter.isSelected(lastSelectedPos)) {
            toggleSelection(lastSelectedPos);
        }
    }
    private void setFragmenPosition() {
        fragmentPosition = getIntent().getIntExtra("fragmentPosition", 1);
        viewPager.setCurrentItem(fragmentPosition); //select specific fragment
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFragmenPosition();
        lastSelectedPos = lastFragmentPos = -1;
        selectionMode = false;
        invalidateOptionsMenu();//Declare that the options menu has changed, so should be recreated. The onCreateOptionsMenu(Menu) method will be called the next time it needs to be displayed
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mMenu == null) {
            mMenu = menu;
        }
        getMenuInflater().inflate(R.menu.toolbar, mMenu); // Inflate the menu; this adds items to the action bar if it is present.
        setToolbar();
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_edit:
                item.setChecked(true);
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                    if(currentFragmentName == FragmentEnum.ProductsFragment) {
                        intent = new Intent(this, AddProductActivity.class);
                        intent.putExtra("selectedProductId", selectedItemId);//send product id to init
                    }
                    else if(currentFragmentName == FragmentEnum.RecipesFragment) {
                        intent = new Intent(this, AddRecipeActivity.class);
                        intent.putExtra("selectedRecipeId", selectedItemId);//send recipe id to init

                    }
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
                    if(currentFragmentName == FragmentEnum.ProductsFragment){
                        builder.setTitle(getResources().getString(R.string.delete_product));
                        builder.setMessage(getResources().getString(R.string.dialog_msg_delete_prod));
                        uri = ProductsContentProvider.PRODUCTS_CONTENT_URI;
                    }
                    else if(currentFragmentName == FragmentEnum.RecipesFragment){
                        builder.setTitle(getResources().getString(R.string.delete_recipe));
                        builder.setMessage(getResources().getString(R.string.dialog_msg_delete_recipe));
                        uri = RecipesContentProvider.RECIPES_CONTENT_URI;
                    }


                    builder.setPositiveButton(getResources().getString(R.string.confirm_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            //clicked on ok button
                            deleteItem();// perform delete item action
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.cancel_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            //clicked on cancel button
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.show();
                    // Must call show() prior to fetching text view
                    TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
                    messageView.setGravity(Gravity.CENTER);

                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_fab: //Start Add new product or recipe activity
                //Check internet connection
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                    if (viewPager.getCurrentItem() == 1) { //Add product
                        Intent intent = new Intent(this, AddProductActivity.class);
                        startActivity(intent);
                    } else if (viewPager.getCurrentItem() == 2) { //Add recipe
                        Intent intent = new Intent(this, AddRecipeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setToolbar(){
        if(selectionMode){
            toolbar.setTitle("");
        }
        else{
            toolbar.setTitle(getString(R.string.app_name));
        }
        mMenu.findItem(R.id.action_search).setVisible(!selectionMode);
        MenuItem shareItem = mMenu.findItem(R.id.action_share);
        shareItem.setVisible(selectionMode);
//        mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        mMenu.findItem(R.id.action_favorites).setVisible(selectionMode);
        mMenu.findItem(R.id.action_edit).setVisible(selectionMode);
        mMenu.findItem(R.id.action_delete).setVisible(selectionMode);
        mMenu.findItem(R.id.action_navigate).setVisible(selectionMode && currentFragmentName == FragmentEnum.ProductsFragment);
}

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                lastFragmentPos = -1;
                if(lastSelectedPos >= 0){
                    handleOnBackPress();
                    lastSelectedPos = -1;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new BlankFragment(), getResources().getString(R.string.news_fragment_name));
        viewPagerAdapter.addFrag(ProductsFragment.getInstance(), getResources().getString(R.string.products_fragment_name));
        viewPagerAdapter.addFrag(new RecipesFragment().getInstance(), getResources().getString(R.string.recipes_fragment_name));
        viewPager.setAdapter(viewPagerAdapter);
    }
    public void setAdapter(int position){
        Fragment fragment = viewPagerAdapter.getItem(position);
        if(fragment instanceof ProductsFragment) {
            mAdapter = ProductsFragment.productsAdapter;
            currentFragmentName = FragmentEnum.ProductsFragment;
        }
        else if(fragment instanceof RecipesFragment){
            mAdapter = RecipesFragment.recipesAdapter;
            currentFragmentName = FragmentEnum.RecipesFragment;
        }
        resetLoaderFragment = (MainActivity.ResetLoaderFragment)viewPagerAdapter.getItem(position);
        lastFragmentPos = position;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: change toolbar items dynamically
    }




    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    /**
     * perform delete item action
     */
    private void deleteItem() {
        Bundle b = new Bundle();
        b.putCharSequence("itemIdToDelete", selectedItemId +"");
        getSupportLoaderManager().restartLoader(loaderID, b, this).forceLoad();//Initializes delete Loader

    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position,int itemID) {
        int currentFragmentIndex = viewPager.getCurrentItem();
        if(lastFragmentPos != currentFragmentIndex ){//if scrolled to other fragment
            setAdapter(currentFragmentIndex);
        }
        if(mAdapter != null){
            if(lastSelectedPos >= 0 && lastSelectedPos != position) {
                if (mAdapter.isSelected(lastSelectedPos)) {
                    toggleSelection(lastSelectedPos);
                }
            }
            toggleSelection(position);
            lastSelectedPos = position;
            selectedItemId = itemID;
        }
        return true;
    }

    /**
     * Toggle the selection state of an item.
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            selectionMode = false;
        } else {
            selectionMode = true;
        }
        setToolbar();
    }

    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        String itemIdToDelete = args != null ? args.getString("itemIdToDelete") :null;
        uri = ContentUris.withAppendedId(uri, Integer.valueOf(itemIdToDelete));
        if(itemIdToDelete!=null){
            return new DeleteLoader(this, uri);
        }
       return null;
    }
    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        mAdapter.toggleSelection(selectedItemId);
        resetLoaderFragment.resetNow();
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

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
