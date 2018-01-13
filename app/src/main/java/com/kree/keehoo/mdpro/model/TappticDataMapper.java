package com.kree.keehoo.mdpro.model;

import com.kree.keehoo.mdpro.model.KeysAndConstants.ElementOfTheTappticList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krzysztof on 13.01.2018.
 */

class TappticDataMapper {

    private static final String NAME = "name";
    private static final String IMAGE = "image";

    List<ElementOfTheTappticList> parseReceivedData(String data) {
        //TODO: move to seperate parser class
        List<ElementOfTheTappticList> values = new ArrayList<>();

        try {
            JSONArray ja = new JSONArray(data);
            values.clear();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                values.add(new ElementOfTheTappticList(jo.getString(NAME), jo.getString(IMAGE)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return values;
    }
}
