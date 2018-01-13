package com.kree.keehoo.mdpro.model;

import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;

import java.util.List;

/**
 * Created by krzysztof on 13.01.2018.
 */

public interface MvpModelInterface {

    ElementOfTheTappticList getPreviousObject();

    int getPreviouslyClickedObjectId();

    int getPreviouslyFocusedObjectId();

    void saveCurrentClickedObjectName(String name);

    void saveCurrentClickedObjectImageUrl(String imageUrl);

    List<ElementOfTheTappticList> parseReceivedData(String data);;
}
