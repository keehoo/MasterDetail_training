package com.kree.keehoo.mdpro.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.kree.keehoo.mdpro.Fragments.DataDetailFragment;
import com.kree.keehoo.mdpro.KeysAndConstants.Consts;
import com.kree.keehoo.mdpro.KeysAndConstants.Keys;
import com.kree.keehoo.mdpro.R;

/**
 * An activity representing a single Data detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link DataListActivity}.
 */
public class DataDetailActivity extends AppCompatActivity {

    public static final String TWO_PANE = "TWO_PANE";
    private DataDetailFragment fragment;
    private Consts consts;

    @Override
    public void onBackPressed() {
        consts.resetValues();
        returnToMainActivity();
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(this, DataListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);

        consts = new Consts(this);

        boolean two_pane = consts.isTwoPane();

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/componłents/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(Keys.KLUCZ, getIntent().getStringExtra(Keys.KLUCZ));
            arguments.putString(Keys.KLUCZ_IMAGE,
                    getIntent().getStringExtra(Keys.KLUCZ_IMAGE));
            fragment = new DataDetailFragment();
            fragment.setArguments(arguments);

            FragmentManager commit = getSupportFragmentManager();


            commit.beginTransaction()
                    .add(R.id.data_detail_container, fragment)
                    .commit();
            commit.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE) ;
        } else {
         //   if (two_pane) {
                returnToMainActivity();
           /* } else {
                Bundle arguments = new Bundle();
                arguments.putString(Keys.KLUCZ, getIntent().getStringExtra(Keys.KLUCZ));
                arguments.putString(Keys.KLUCZ_IMAGE,
                        getIntent().getStringExtra(Keys.KLUCZ_IMAGE));
                fragment = new DataDetailFragment();
                fragment.setArguments(arguments);

                FragmentManager commit = getSupportFragmentManager();

                commit.popBackStack();

                commit.beginTransaction()
                        .replace(R.id.data_detail_container, fragment)
                        .commit();
            }*/

            }

   /*     if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }*/
      //  }
    }
}
