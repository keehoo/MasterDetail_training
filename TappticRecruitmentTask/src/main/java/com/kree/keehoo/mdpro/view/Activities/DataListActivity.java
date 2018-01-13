package com.kree.keehoo.mdpro.view.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.model.KeysAndConstants.Keys;
import com.kree.keehoo.mdpro.model.KeysAndConstants.PersistentValues;
import com.kree.keehoo.mdpro.model.Loaders.TappticDataLoader;
import com.kree.keehoo.mdpro.presenter.MainPresenter;
import com.kree.keehoo.mdpro.view.Activities.interfaces.MainActivityInterface;
import com.kree.keehoo.mdpro.view.ConnectivityChecker;
import com.kree.keehoo.mdpro.view.Fragments.DataDetailFragment;
import com.kree.keehoo.mdpro.view.RVAdapters.ListAdapter;

import java.util.List;


public class DataListActivity extends AppCompatActivity implements MainActivityInterface {

    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        initViews();
        handleTwoPaneMode();
        mainPresenter = new MainPresenter(this, new PersistentValues(this));
        afterConfigurationChange(savedInstanceState);
        if (noConnectivity()) {
            showRetryScreen();
        } else {
            handleLoaderCallbacks();
        }
    }

    private void handleLoaderCallbacks() {
        getSupportLoaderManager().initLoader(R.id.string_loader_id, null,
                new LoaderManager.LoaderCallbacks<String>() {
                    @Override
                    public Loader<String> onCreateLoader(int id, Bundle args) {
                        return new TappticDataLoader(DataListActivity.this);
                    }

                    @Override
                    public void onLoadFinished(Loader<String> loader, String data) {
                        notifyPresenterDataIsLoaded(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<String> loader) {
                        // TODO: handle reset action
                    }
                }

        );
    }

    private void notifyPresenterDataIsLoaded(String data) {
        mainPresenter.dataIsLoaded(data);
    }

    private void showRetryScreen() {
        Intent intent = new Intent(this, WaitForConnectivityActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean noConnectivity() {
        return new ConnectivityChecker().isNetworkAvailable(this);
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.data_list);
    }

    private void handleTwoPaneMode() {
        setUpTwoPaneMode();
        doNotShowDetailOnLandscapeInTablet();
    }

    private void setUpTwoPaneMode() {
        if (findViewById(R.id.data_detail_container) != null) {  // data_detail_container jest w layoucie activity_data_detail i jest to kontrolka NestedSctollView
            mTwoPane = true;
        }
    }

    private void doNotShowDetailOnLandscapeInTablet() {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mTwoPane = false;
            View v = findViewById(R.id.data_detail_container);
            if (null != v) {
                v.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean isTwoPane() {
        return mTwoPane;
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public LoaderManager getLoadManager() {
        return this.getSupportLoaderManager();
    }

    @Override
    public void showTappticList(ListAdapter adapter) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void postShowDetailScreen(boolean twoPane, ElementOfTheTappticList obj) {

        if (twoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(Keys.KEY, obj.getName());
            arguments.putString(Keys.IMAGE_KEY, obj.getImageUrl());

            DataDetailFragment fragment = new DataDetailFragment();
            fragment.setArguments(arguments);
            FragmentManager supportFragmentManager = getSupportFragmentManager();

            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            supportFragmentManager.beginTransaction()
                    .replace(R.id.data_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DataDetailActivity.class);
            intent.putExtra(Keys.KEY, obj.getName());
            intent.putExtra(Keys.IMAGE_KEY, obj.getImageUrl());
            intent.putExtra(DataDetailActivity.TWO_PANE, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startProperActivity(intent);
            finishActivity();
        }
    }

    @Override
    public void prepareRecyclerView(List<ElementOfTheTappticList> values) {
        final ListAdapter adapter = new ListAdapter(this, values);
        adapter.setListener(new ListAdapter.OnElementClickListener() {
            @Override
            public void onClick(ElementOfTheTappticList currentObject, int currentPosition) {
                mainPresenter.showDetailScreen(isTwoPane(), currentObject);
            }
        });

        adapter.setFocusListener(new ListAdapter.OnElementFocusListener() {
            @Override
            public void onFocus(View v, boolean hasFocus, int position) {
                if (hasFocus) {
                    // no need to handle focus change as per received requirements.
                }
            }
        });
        showTappticList(adapter);
    }

    @Override
    public void startProperActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(mainPresenter.saveCurrentScreenState());
    }

    protected void afterConfigurationChange(Bundle savedInstanceState) {
        mainPresenter.afterConfigurationChange(savedInstanceState);
    }
}
