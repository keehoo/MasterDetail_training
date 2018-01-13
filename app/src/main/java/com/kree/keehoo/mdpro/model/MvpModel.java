package com.kree.keehoo.mdpro.model;

import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;
import com.kree.keehoo.mdpro.model.KeysAndConstants.PersistentValues;

import java.util.List;

/**
 * Created by krzysztof on 13.01.2018.
 */

public class MvpModel implements MvpModelInterface {

    private PersistentValues persistentValues;
    public String data;

    public MvpModel(PersistentValues persistentValues) {
        this.persistentValues = persistentValues;
    }

    @Override
    public ElementOfTheTappticList getPreviousObject() {
        return persistentValues.getLastClickedObj();
    }

    @Override
    public int getPreviouslyClickedObjectId() {
        return persistentValues.getLastSelectionId();
    }

    @Override
    public int getPreviouslyFocusedObjectId() {
        return persistentValues.getCurrentFocusedItemId();
    }

    @Override
    public void saveCurrentClickedObjectName(String name) {
        persistentValues.saveCurrentClickedObjectName(name);
    }

    @Override
    public void saveCurrentClickedObjectImageUrl(String imageUrl) {
        persistentValues.saveCurrentClickedObjectImageUrl(imageUrl);
    }

    public List<ElementOfTheTappticList> parseReceivedData(String data) {
        return new TappticDataMapper().parseReceivedData(data);
    }
}
