package com.example.proteinclassification.helper;

import android.graphics.Bitmap;

public class Bitmaphelper {
    private Bitmap bitmap=null;
    private static final Bitmaphelper instance=new Bitmaphelper();

    public Bitmaphelper(){

    }

    public static Bitmaphelper getInstance(){

        return instance;
    }
    public Bitmap getBitmap(){

        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){

        this.bitmap=bitmap;
    }


}
