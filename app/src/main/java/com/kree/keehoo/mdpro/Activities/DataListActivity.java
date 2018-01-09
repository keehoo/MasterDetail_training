package com.kree.keehoo.mdpro.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Toast;

import com.kree.keehoo.mdpro.Fragments.DataDetailFragment;
import com.kree.keehoo.mdpro.KeysAndConstants.Consts;
import com.kree.keehoo.mdpro.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.KeysAndConstants.Keys;
import com.kree.keehoo.mdpro.Loaders.StringLoader;
import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.RVAdapters.SimpleViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DataListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private String result;
    private RecyclerView recyclerView;
    private Consts consts;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("TEST", "ROTATION TEST");
        outState.putInt("CLICK", consts.getLastSelectionId());
        outState.putInt("FOCUS", consts.getCurrentFocusedItemId());
        super.onSaveInstanceState(outState);
    }


    protected void afterConfigurationChange(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String test = savedInstanceState.getString("TEST");
            Integer click = savedInstanceState.getInt("CLICK");
            Integer focus = savedInstanceState.getInt("FOCUS");

            if (click != -2) {
                // Item has been clicked before
                //  Toast.makeText(this, "Item has been clicked before: " + click, Toast.LENGTH_SHORT).show();
                showDetailScreen(mTwoPane, consts.getLastClickedObj(), click);
            }

            if (focus != -1) {
                // Toast.makeText(this, "Item has been focused on before: " + focus, Toast.LENGTH_SHORT).show();
                // Item has been focused on before
            }

            Toast.makeText(this, test + getResources().getConfiguration().orientation, Toast.LENGTH_SHORT).show();
            Log.d("ORIENTATION", "" + getResources().getConfiguration().orientation);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "On back pressed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        initViews();
        consts = new Consts(this);
        setUpTwoPaneMode();
        afterConfigurationChange(savedInstanceState);
        doNotShowDetailOnLandscapeInTablet();
        showLastOpenedDetailScreen();

        if (noConnectivity()) {
            showRetryScreen();
        } else {
            getSupportLoaderManager().initLoader(R.id.string_loader_id, null, listLoaderCallbacks);  // inicjacja loadera
        }
    }

    private void showRetryScreen() {
        Intent intent = new Intent(this, WaitForConnectivityActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean noConnectivity() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        Toast.makeText(this, "Network available", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void showLastOpenedDetailScreen() {
        if (consts.getLastSelectionId() != -2) {
            Toast.makeText(this, "Attempting to open previous screen", Toast.LENGTH_SHORT).show();
            showDetailScreen(mTwoPane, consts.getLastClickedObj(), consts.getLastSelectionId());
        }
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.data_list);
    }

    private void setUpTwoPaneMode() {
        if (findViewById(R.id.data_detail_container) != null) {  // data_detail_container jest w layoucie activity_data_detail i jest to kontrolka NestedSctollView
            mTwoPane = true;
        }
        consts.saveTwoPane(mTwoPane);
    }

    private void doNotShowDetailOnLandscapeInTablet() {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mTwoPane = false;
            View v = findViewById(R.id.data_detail_container);
            if (null != v) {
                v.setVisibility(View.GONE);
            }
        }
        consts.saveTwoPane(mTwoPane);
    }

    public void setupRecyclerView(List<ElementOfTheTappticList> values, final boolean mTwoPane) {
        final SimpleViewAdapter adapter = new SimpleViewAdapter(this, values, mTwoPane);
        adapter.setListener(new SimpleViewAdapter.OnElementClickListener() {
            @Override
            public void onClick(ElementOfTheTappticList currentObject, int currentPosition) {
                showSelectedDetailScreen(currentObject, currentPosition);
                consts.saveCurrentOnClickId(currentPosition);
                consts.saveCurrentClickedObjectImageUrl(currentObject.getImageUrl());
                consts.saveCurrentClickedObjectName(currentObject.getName());
                showDetailScreen(mTwoPane, currentObject, currentPosition);
            }
        });

        adapter.setFocusListener(new SimpleViewAdapter.OnElementFocusListener() {
            @Override
            public void onFocus(View v, boolean hasFocus, int position) {
                if (hasFocus) {
                    Toast.makeText(DataListActivity.this, "Focus on item " + position, Toast.LENGTH_SHORT).show();
                    consts.saveCurrentFocusId(position);
                }
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void showDetailScreen(boolean mTwoPanes, ElementOfTheTappticList obj, int position) {
        if (mTwoPanes) {
            Bundle arguments = new Bundle();
            arguments.putString(Keys.KLUCZ, obj.getName());
            consts.saveCurrentClickedObjectName(obj.getName());
            consts.saveCurrentClickedObjectImageUrl(obj.getImageUrl());
            arguments.putString(Keys.KLUCZ_IMAGE, obj.getImageUrl());   // tutaj musze przeslac Id
            DataDetailFragment fragment = new DataDetailFragment();
            fragment.setArguments(arguments);

            FragmentManager supportFragmentManager = getSupportFragmentManager();

            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            supportFragmentManager.beginTransaction()
                    .replace(R.id.data_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DataDetailActivity.class);
            intent.putExtra(Keys.KLUCZ, obj.getName());
            intent.putExtra(Keys.KLUCZ_IMAGE, obj.getImageUrl());
            intent.putExtra(DataDetailActivity.TWO_PANE, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void showSelectedDetailScreen(ElementOfTheTappticList currentObject, int currentPosition) {
        Toast.makeText(this, "Clicked position " + currentPosition, Toast.LENGTH_SHORT).show();
    }

    public void parseReceivedData() {
        List<ElementOfTheTappticList> values = new ArrayList<>();

        try {
            JSONArray ja = new JSONArray(getResult());
            values.clear();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                values.add(new ElementOfTheTappticList(jo.getString("name"), jo.getString("image")));
            }
            //tv.setText("dlugosc listy "+lista.size());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONArray", "JSONEXCEPTION");
            Toast.makeText(DataListActivity.this, "JSONArray Exception", Toast.LENGTH_SHORT).show();
        }
        setupRecyclerView(values, mTwoPane);
    }

    private LoaderManager.LoaderCallbacks<String> listLoaderCallbacks = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return new StringLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            Log.d("onLoadFinished", "data = " + data);
            setResult(data);
            parseReceivedData();
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            setupRecyclerView(Collections.<ElementOfTheTappticList>emptyList(), mTwoPane);
        }
    };


    /**
     * Getters & Setters
     */
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
