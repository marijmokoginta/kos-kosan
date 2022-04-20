package com.sibkelompoke.kost.model;

public class KostArea {
    private int image;
    private String location;

    public KostArea (int image, String location) {
        this.image = image;
        this.location = location;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
