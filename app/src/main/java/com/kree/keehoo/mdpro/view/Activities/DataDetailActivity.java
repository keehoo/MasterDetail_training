package com.kree.keehoo.mdpro.view.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.model.KeysAndConstants.Keys;
import com.kree.keehoo.mdpro.model.KeysAndConstants.PersistentValues;
import com.kree.keehoo.mdpro.view.Fragments.DataDetailFragment;

public class DataDetailActivity extends AppCompatActivity {

    public static final String TWO_PANE = "TWO_PANE";
    private PersistentValues persistentValues;

    @Override
    public void onBackPressed() {
        persistentValues.resetValues();
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
        persistentValues = new PersistentValues(this);
        handleFragmentInfation(savedInstanceState);
    }

    private void handleFragmentInfation(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(Keys.KEY, getIntent().getStringExtra(Keys.KEY));
            arguments.putString(Keys.IMAGE_KEY,
                    getIntent().getStringExtra(Keys.IMAGE_KEY));
            DataDetailFragment fragment = new DataDetailFragment();
            fragment.setArguments(arguments);
            FragmentManager commit = getSupportFragmentManager();
            commit.beginTransaction()
                    .add(R.id.data_detail_container, fragment)
                    .commit();
            commit.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            returnToMainActivity();
        }
    }
}
