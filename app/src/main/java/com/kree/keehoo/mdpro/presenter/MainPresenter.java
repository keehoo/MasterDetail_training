package com.kree.keehoo.mdpro.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.model.KeysAndConstants.Consts;
import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.model.KeysAndConstants.Keys;

import view.Activities.DataDetailActivity;
import view.Activities.interfaces.MainActivityInterface;
import view.Fragments.DataDetailFragment;

/**
 * Created by krzysztof on 13.01.2018.
 */


public class MainPresenter {
    private  Context context;
    private Consts consts;
    private Integer click;
    private MainActivityInterface view;

    public MainPresenter(MainActivityInterface view) {
        this.view = view;
        context = view.getActivityContext();
        consts = new Consts(context);
      //  MvpModel model = new MvpModel(this);
    }

    public void afterConfigurationChange(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            click = savedInstanceState.getInt("CLICK");
            Integer focus = savedInstanceState.getInt("FOCUS");

            if (click != -2) {

                showDetailScreen(view.isTwoPane(), consts.getLastClickedObj());
            }

            if (focus != -1) {
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


}
