package com.course.cokefacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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
        List<Fragment> fragmentsList = new ArrayList<Fragment>();
        setContentView(R.layout.activity_facts);
         // Retrieve facts records
        String fact;
        String URL = "content://com.course.cokefacts.FactsContentProvider";
        Uri facts = Uri.parse(URL);
        insertFacts(); //insert facts texts to DB by contentProvider
        Cursor c = getContentResolver().query(facts, null, null, null, FactsContract.FactsEntry.COLUMN_FACT);
        if (c.moveToFirst()) {
            do{
                fact = c.getString(c.getColumnIndex(FactsContract.FactsEntry.COLUMN_FACT));
                fragmentsList.add(PageFragment.newInstance(fact));
            }
            while (c.moveToNext());
        }

        //Set page viewer
        myViewPager = (ViewPager)findViewById(R.id.coke_facts_pager);
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


    /**
     * insert facts texts to DB by contentProvider
     *
     */
    public void insertFacts(){
        // Add a new fact record
        ContentValues values = new ContentValues();
        //Set content values
        values.put(FactsContract.FactsEntry.COLUMN_FACT, getResources().getString(R.string.fact1));
        getContentResolver().insert( FactsContentProvider.CONTENT_URI, values);
        values.put(FactsContract.FactsEntry.COLUMN_FACT, getResources().getString(R.string.fact2));
        getContentResolver().insert( FactsContentProvider.CONTENT_URI, values);
        values.put(FactsContract.FactsEntry.COLUMN_FACT, getResources().getString(R.string.fact3));
        getContentResolver().insert( FactsContentProvider.CONTENT_URI, values);
        values.put(FactsContract.FactsEntry.COLUMN_FACT, getResources().getString(R.string.fact4));
        getContentResolver().insert( FactsContentProvider.CONTENT_URI, values);
        values.put(FactsContract.FactsEntry.COLUMN_FACT, getResources().getString(R.string.fact5));
        getContentResolver().insert( FactsContentProvider.CONTENT_URI, values);
    }
}

