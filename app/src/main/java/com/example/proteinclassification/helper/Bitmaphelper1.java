package com.example.proteinclassification.helper;

import android.graphics.Bitmap;

public class Bitmaphelper1 {
    private Bitmap bitmap=null;
    String msg="";
    private static final Bitmaphelper1 instance=new Bitmaphelper1();

    public Bitmaphelper1(){

    }

    public static Bitmaphelper1 getInstance(){

        return instance;
    }
    public Bitmap getBitmap(){

        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){

        this.bitmap=bitmap;
    }
    public void savemsg(String s){
        msg=s;

    }
    public String sendmsg (){
        return  msg;

    }


}
