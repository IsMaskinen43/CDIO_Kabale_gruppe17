package com.example.cdio_kabale_gruppe17;

import android.graphics.Bitmap;

import java.util.List;


/**
 * Class that will make us transfer the bitmap of the pictures between two activities
 */
public class PictureHelperClass {

    private static PictureHelperClass instance = null;
    private static List<Bitmap> pictureList;

    private PictureHelperClass(){}

    public static PictureHelperClass getInstance(){
        if (instance == null) instance = new PictureHelperClass();
        return instance;
    }

    public void setPictureList(List<Bitmap> pictures){
        this.pictureList = pictures;
    }

    public List<Bitmap> getPictureList(){
        return pictureList;
    }


}
