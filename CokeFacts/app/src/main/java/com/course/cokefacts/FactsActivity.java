package com.course.cokefacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

public class FactsActivity extends FragmentActivity implements View.OnClickListener {
    ViewPager myViewPager;
    ImageButton leftNav;
    ImageButton rightNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        //Set page viewer
        myViewPager = (ViewPager)findViewById(R.id.coke_facts_pager);
        List<Fragment> fragmentsList = new ArrayList<Fragment>();
        fragmentsList.add(PageFragment.newInstance(getResources().getString(R.string.fact1)));
        fragmentsList.add(PageFragment.newInstance(getResources().getString(R.string.fact2)));
        fragmentsList.add(PageFragment.newInstance(getResources().getString(R.string.fact3)));
        fragmentsList.add(PageFragment.newInstance(getResources().getString(R.string.fact4)));
        fragmentsList.add(PageFragment.newInstance(getResources().getString(R.string.fact5)));
        FactsPageAdapter myFragmentPagerAdapter = new FactsPageAdapter(getSupportFragmentManager(),fragmentsList);
        myViewPager.setAdapter(myFragmentPagerAdapter);
        // Images left navigation
        leftNav = (ImageButton)findViewById(R.id.left_nav);
        // Images right navigation
        rightNav = (ImageButton)findViewById(R.id.right_nav);
        leftNav.setOnClickListener(this);
        rightNav.setOnClickListener(this);

     }

    /**
     * manage the clicks
     * @param view
     */
    @Override
    public void onClick(View view){
        int tab=0;
        switch(view.getId()) {
            case R.id.left_nav:
                 tab = myViewPager.getCurrentItem();
                    if (tab > 0) {
                        tab--;
                        myViewPager.setCurrentItem(tab);
                    } else if (tab == 0) {
                        myViewPager.setCurrentItem(tab);
                    }
                break;
            // Images right navigatin
            case R.id.right_nav:
                tab = myViewPager.getCurrentItem();
                tab++;
                myViewPager.setCurrentItem(tab);
                break;
        }
    }
}

