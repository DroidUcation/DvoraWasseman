package com.gfcommunity.course.gfcommunity.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.products.AddProductActivity;
import com.gfcommunity.course.gfcommunity.activities.products.EditProductActivity;
import com.gfcommunity.course.gfcommunity.fragments.BlankFragment;
import com.gfcommunity.course.gfcommunity.fragments.ProductsFragment;
import com.gfcommunity.course.gfcommunity.fragments.RecipesFragment;
import com.gfcommunity.course.gfcommunity.utils.NetworkConnectedUtil;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {
    private int fragmentPosition;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BlankFragment(), getResources().getString(R.string.news_fragment_name));
        adapter.addFrag(ProductsFragment.getInstance(), getResources().getString(R.string.products_fragment_name));
        adapter.addFrag(new RecipesFragment(), getResources().getString(R.string.recipes_fragment_name));
        viewPager.setAdapter(adapter);
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
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
            return true;

            case R.id.action_delete:
                item.setChecked(true);
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                  //TODO: delete selected item
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
