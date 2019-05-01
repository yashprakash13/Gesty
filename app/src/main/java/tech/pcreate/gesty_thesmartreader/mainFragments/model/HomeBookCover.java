/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.mainFragments.model;

import android.graphics.Bitmap;

public class HomeBookCover {

    private Bitmap bitmap;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public HomeBookCover(Bitmap bitmap, String location) {
        this.bitmap = bitmap;
        this.location = location;
    }
}
