package com.course.cokefacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Page fragment.
 */
public class PageFragment extends Fragment {

    TextView tv;

    public static PageFragment newInstance(String text) {
        PageFragment pageFragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fact", text);
        pageFragment.setArguments(bundle);

        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)   {
        View view = inflater.inflate(R.layout.facts_fragment_layout, container, false);
        View textView = view.findViewById(R.id.fact_txt);
        if(textView != null){
            tv = (TextView) textView;
            Bundle bundle = getArguments();
            if(tv != null && bundle != null) {
                tv.setText(bundle.getString("fact"));
            }
        } else {
            Log.v("PageFragment","Not excepted to get null for TextView");
        }

        return view;
    }
}
