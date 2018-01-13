package com.kree.keehoo.mdpro.presenter;

import android.os.Bundle;

import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.model.KeysAndConstants.PersistentValues;
import com.kree.keehoo.mdpro.model.MvpModel;
import com.kree.keehoo.mdpro.model.MvpModelInterface;
import com.kree.keehoo.mdpro.view.Activities.interfaces.MainActivityInterface;

import java.util.List;

import static com.kree.keehoo.mdpro.model.KeysAndConstants.PersistentValues.DEFAULT_LAST_CLICKED_ID_VALUE;

/**
 * Created by krzysztof on 13.01.2018.
 */

public class MainPresenter {
    private static final String FOCUS = "FOCUS";
    public static final String CLICK = "CLICK";
    private PersistentValues persistentValues;
    private MainActivityInterface view;
    private final MvpModelInterface model;

    public MainPresenter(MainActivityInterface view, PersistentValues persistentValues) {
        // persistenValues are given here in the constructor to prevent Presenter from accessing Context.
        this.view = view;
        this.persistentValues = persistentValues;
        model = new MvpModel(persistentValues);
        showPreviousScreen();
    }

    private void showPreviousScreen() {
        if (persistentValues.getLastSelectionId() != DEFAULT_LAST_CLICKED_ID_VALUE) {
            view.postShowDetailScreen(view.isTwoPane(), model.getPreviousObject());
        }
    }

    public void afterConfigurationChange(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Integer click = savedInstanceState.getInt(CLICK);
            if (click != DEFAULT_LAST_CLICKED_ID_VALUE) {
                view.postShowDetailScreen(view.isTwoPane(), model.getPreviousObject());
            }
        }
    }

    public void showDetailScreen(boolean twoPane, ElementOfTheTappticList obj) {
        model.saveCurrentClickedObjectName(obj.getName());
        model.saveCurrentClickedObjectImageUrl(obj.getImageUrl());
        view.postShowDetailScreen(twoPane, obj);
    }

    public Bundle saveCurrentScreenState() {
        Bundle outState = new Bundle();
        outState.putInt(CLICK, model.getPreviouslyClickedObjectId());
        outState.putInt(FOCUS, model.getPreviouslyFocusedObjectId());
        return outState;
    }


    private void prepareRecyclerViewAdapter(List<ElementOfTheTappticList> values) {
        view.prepareRecyclerView(values);
    }


    public void dataIsLoaded(String data) {
        prepareRecyclerViewAdapter(model.parseReceivedData(data));
    }
}
