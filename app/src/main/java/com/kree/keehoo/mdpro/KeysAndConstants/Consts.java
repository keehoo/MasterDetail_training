package com.kree.keehoo.mdpro.KeysAndConstants;

import android.content.Context;
import android.content.SharedPreferences;

import com.kree.keehoo.mdpro.R;

/**
 * Created by krzysztof on 07.01.2018.
 */

public class Consts {

    private static final String IMAGE = "IMAGE";
    private static final String NAME = "Name";
    private static final String CLICK = "CLICK";
    private static final String FOCUS = "FOCUS";
    private static final String TWO_PANE = "two_panes";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Consts(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.tapptic_shared_preferences_file), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        getPreviousValues();
    }

    private static int currentFocusedItemId = -1;
    private static int lastSelectionId = -2;
    private static String currentClickedObjectName = "";
    private static String currentClickedObjectImageUrl = "";

    public void resetValues() {
        saveCurrentFocusId(-1);
        saveCurrentClickedObjectName("");
        saveCurrentOnClickId(-2);
        saveCurrentClickedObjectImageUrl("");
    }

    public int getCurrentFocusedItemId() {
        return currentFocusedItemId;
    }

    public int getLastSelectionId() {
        return lastSelectionId;
    }


    public void saveCurrentFocusId (int id) {
        currentFocusedItemId = id;
        editor.putInt(FOCUS, id).commit();
    }

    public void saveCurrentOnClickId(int id) {
        lastSelectionId = id;
        editor.putInt(CLICK, id).commit();
    }

    public void saveCurrentClickedObjectName(String s) {
        currentClickedObjectName = s;
        editor.putString(NAME, s).commit();
    }

    public void saveCurrentClickedObjectImageUrl(String s) {
        currentClickedObjectImageUrl = s;
        editor.putString(IMAGE, s).commit();
    }

    public void saveTwoPane(boolean twoPane) {
        editor.putBoolean(TWO_PANE, twoPane).commit();
    }

    public boolean isTwoPane() {
        return sharedPreferences.getBoolean(TWO_PANE, false);
    }

    public ElementOfTheTappticList getLastClickedObj() {
        return new ElementOfTheTappticList(currentClickedObjectName, currentClickedObjectImageUrl);
    }

    private void getPreviousValues() {
        currentClickedObjectImageUrl = sharedPreferences.getString(IMAGE, "" );
        currentClickedObjectName = sharedPreferences.getString(NAME, "");
        lastSelectionId = sharedPreferences.getInt(CLICK, -2);
        currentFocusedItemId = sharedPreferences.getInt(FOCUS, -1);
    }
}





