package com.kree.keehoo.mdpro.view.Activities.interfaces;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;

import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.view.RVAdapters.ListAdapter;

import java.util.List;

/**
 * Created by krzysztof on 13.01.2018.
 */

public interface MainActivityInterface {

    boolean isTwoPane();

    FragmentManager getSupportFragmentManager();

    void startProperActivity(Intent intent);

    void finishActivity();

    LoaderManager getLoadManager();

    void showTappticList(ListAdapter adapter);

    void postShowDetailScreen(boolean twoPane, ElementOfTheTappticList obj);

    void prepareRecyclerView(List<ElementOfTheTappticList> values);

}
