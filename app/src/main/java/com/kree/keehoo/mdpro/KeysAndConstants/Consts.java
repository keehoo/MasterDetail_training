package com.kree.keehoo.mdpro.KeysAndConstants;

/**
 * Created by krzysztof on 07.01.2018.
 */

public class Consts {


    public Consts() {


    }

    private static int currentFocusedItemId = -1;
    private static int lastSelectionId = -2;
    private static String currentClickedObjectName = "";
    private static String currentClickedObjectImageUrl = "";

    public int getCurrentFocusedItemId() {
        return currentFocusedItemId;
    }

    public int getLastSelectionId() {
        return lastSelectionId;
    }


    public void saveCurrentFocusId (int id) {
        currentFocusedItemId = id;

    }

    public void saveCurrentOnClickId(int id) {
        lastSelectionId = id;
    }


    public void saveCurrentClickedObjectName(String s) {
        currentClickedObjectName = s;
    }

    public void saveCurrentClickedObjectImageUrl(String s) {
        currentClickedObjectImageUrl = s;
    }

    public ElementOfTheTappticList getLastClickedObj() {
        return new ElementOfTheTappticList(currentClickedObjectName, currentClickedObjectImageUrl);
    }
}





