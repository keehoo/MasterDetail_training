package com.kree.keehoo.mdpro.model.KeysAndConstants;

/**
 * Created by keehoo on 08.09.2016.
 */
public class ElementOfTheTappticList {

    private String name;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ElementOfTheTappticList(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
