package com.kree.keehoo.mdpro.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kree.keehoo.mdpro.KeysAndConstants.ElementOfTheTappticList;
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
    private List<ElementOfTheTappticList> values;
    private String result;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        initViews();
        setUpTwoPaneMode();
        doNotShowDetailOnLandscapeInTablet();
        getSupportLoaderManager().initLoader(R.id.string_loader_id, null, listLoaderCallbacks);  // inicjacja loadera
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.data_list);
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

    public void setupRecyclerView(List<ElementOfTheTappticList> values, boolean mTwoPane) {
        SimpleViewAdapter adapter = new SimpleViewAdapter(this, values, mTwoPane);
        adapter.setListener(new SimpleViewAdapter.OnElementClickListener() {
            @Override
            public void onClick(ElementOfTheTappticList currentObject, int currentPosition) {
                showSelectedDetailScreen(currentObject, currentPosition);
            }
        });
        adapter.setTouchListener(new SimpleViewAdapter.OnElementTouchListener() {
            @Override
            public void onTouch(ElementOfTheTappticList currentObject, int currentPosition) {
                Toast.makeText(DataListActivity.this, "on Touch !!!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void showSelectedDetailScreen(ElementOfTheTappticList currentObject, int currentPosition) {
        Toast.makeText(this, "Clicked position " + currentPosition, Toast.LENGTH_SHORT).show();
    }

    public void start() {
        /**
         * Po pobraniu danych, rezulatat powinien znajodwac sie w prywatnym polu result,
         * wykorzystujemy to pole (String), zeby otrzymać listę elementów
         * */
        values = new ArrayList<>();

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
            start();
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
