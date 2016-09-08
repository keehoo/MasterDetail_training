package com.kree.keehoo.mdpro;

/**
 * Created by keehoo on 08.09.2016.
 */
public class Obj {

    String name, image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Obj(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
