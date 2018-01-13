package com.kree.keehoo.mdpro.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.model.KeysAndConstants.Consts;
import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.model.KeysAndConstants.Keys;
import com.kree.keehoo.mdpro.presenter.Loaders.StringLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import view.Activities.DataDetailActivity;
import view.Activities.interfaces.MainActivityInterface;
import view.Fragments.DataDetailFragment;
import view.RVAdapters.SimpleViewAdapter;

/**
 * Created by krzysztof on 13.01.2018.
 */


public class MainPresenter {
    private Context context;
    private Consts consts;
    private Integer click;
    private MainActivityInterface view;

    public MainPresenter(MainActivityInterface view) {
        this.view = view;
        context = view.getActivityContext();
        consts = new Consts(context);
        //  MvpModel model = new MvpModel(this);
        showPreviousScreen();
    }

    public void showPreviousScreen() {
        if (consts.getLastSelectionId() != -2) {
            showDetailScreen(view.isTwoPane(), consts.getLastClickedObj());
        }
    }

    public void afterConfigurationChange(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            click = savedInstanceState.getInt("CLICK");
            if (click != -2) {
                showDetailScreen(view.isTwoPane(), consts.getLastClickedObj());
            }
        }
    }

    public void showDetailScreen(boolean twoPane, ElementOfTheTappticList obj) {

        // model.getValues;

        if (twoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(Keys.KEY, obj.getName());
            consts.saveCurrentClickedObjectName(obj.getName());
            consts.saveCurrentClickedObjectImageUrl(obj.getImageUrl());
            arguments.putString(Keys.KLUCZ_IMAGE, obj.getImageUrl());   // tutaj musze przeslac Id
            DataDetailFragment fragment = new DataDetailFragment();
            fragment.setArguments(arguments);

            FragmentManager supportFragmentManager = view.getSupportFragmentManager();

            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            supportFragmentManager.beginTransaction()
                    .replace(R.id.data_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(view.getActivityContext(), DataDetailActivity.class);
            intent.putExtra(Keys.KEY, obj.getName());
            intent.putExtra(Keys.KLUCZ_IMAGE, obj.getImageUrl());
            intent.putExtra(DataDetailActivity.TWO_PANE, true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            view.startProperActivity(intent);
            view.finishActivity();
        }
    }

    public Bundle saveCurrentScreenState() {
        Bundle outState = new Bundle();
        outState.putInt("CLICK", consts.getLastSelectionId());
        outState.putInt("FOCUS", consts.getCurrentFocusedItemId());
        return outState;
    }

    private LoaderManager.LoaderCallbacks<String> listLoaderCallbacks = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return new StringLoader(view.getActivityContext());
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            Log.d("onLoadFinished", "data = " + data);

            //view.setResult(data);
            List<ElementOfTheTappticList> elementOfTheTappticLists = parseReceivedData(data);
            SimpleViewAdapter adapter = prepareRecyclerViewAdapter(elementOfTheTappticLists);
            view.showTappticList(adapter);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
//TODO: do something!!
        }
    };


    private SimpleViewAdapter prepareRecyclerViewAdapter(List<ElementOfTheTappticList> values) {
        final SimpleViewAdapter adapter = new SimpleViewAdapter(view.getActivityContext(), values, view.isTwoPane());
        adapter.setListener(new SimpleViewAdapter.OnElementClickListener() {
            @Override
            public void onClick(ElementOfTheTappticList currentObject, int currentPosition) {
                showDetailScreen(view.isTwoPane(), currentObject);
            }
        });

        adapter.setFocusListener(new SimpleViewAdapter.OnElementFocusListener() {
            @Override
            public void onFocus(View v, boolean hasFocus, int position) {
                if (hasFocus) {
                }
            }
        });
        return adapter;
    }

    public void downloadTappticValues() {
        ((FragmentActivity) context).getSupportLoaderManager().initLoader(R.id.string_loader_id, null, listLoaderCallbacks);
    }

    public List<ElementOfTheTappticList> parseReceivedData(String data) {
        //TODO: move to seperate parser class
        List<ElementOfTheTappticList> values = new ArrayList<>();

        try {
            JSONArray ja = new JSONArray(data);
            values.clear();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                values.add(new ElementOfTheTappticList(jo.getString("name"), jo.getString("image")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return values;
    }
}
