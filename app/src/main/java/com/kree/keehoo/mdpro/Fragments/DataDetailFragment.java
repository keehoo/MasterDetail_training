package com.kree.keehoo.mdpro.Fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kree.keehoo.mdpro.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.KeysAndConstants.Keys;
import com.kree.keehoo.mdpro.R;


public class DataDetailFragment extends Fragment {

    private ElementOfTheTappticList mItem;

    public DataDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Keys.KLUCZ)) {
            Log.d("Fragment", "contains key, key value = "+Keys.KLUCZ);

            mItem = new ElementOfTheTappticList(getArguments().getString(Keys.KLUCZ), getArguments().getString(Keys.KLUCZ_IMAGE));
            Log.d("Fragment mItem = ", mItem.getName() + " - " + mItem.getImageUrl());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
        else {
            Log.d("Nie ma klucza", "ARG_ITEM_ID -->"+Keys.KLUCZ);}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.data_detail)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.data_detail2)).setText(mItem.getImageUrl());
        }

        return rootView;
    }
}
