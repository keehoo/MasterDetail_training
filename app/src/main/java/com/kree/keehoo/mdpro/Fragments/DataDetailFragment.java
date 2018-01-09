package com.kree.keehoo.mdpro.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
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

        if (getArguments().containsKey(Keys.KEY)) {

            mItem = new ElementOfTheTappticList(getArguments().getString(Keys.KEY), getArguments().getString(Keys.KLUCZ_IMAGE));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        } else {
            // TODO: add exception handling
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.data_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.data_detail)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.data_detail2)).setText(mItem.getImageUrl());
        }

        return rootView;
    }
}
