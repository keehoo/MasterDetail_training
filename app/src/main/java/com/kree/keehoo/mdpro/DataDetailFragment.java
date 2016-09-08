package com.kree.keehoo.mdpro;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Data detail screen.
 * This fragment is either contained in a {@link DataListActivity}
 * in two-pane mode (on tablets) or a {@link DataDetailActivity}
 * on handsets.
 */
public class DataDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */


    /**
     * The dummy content this fragment is presenting.
     */
    private Obj mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DataDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Keys.KLUCZ)) {
            Log.d("Fragment", "contains key, key value = "+Keys.KLUCZ);
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = new Obj(Keys.KLUCZ, "Jakies nazwisko");
            Log.d("Fragment mItem = ", mItem.getName() + " - " + mItem.getImage());


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
        }

        return rootView;
    }
}
