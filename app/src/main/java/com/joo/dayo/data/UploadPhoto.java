package com.joo.dayo.data;

import android.widget.ImageView;

public class UploadPhoto {

    ImageView uploadIv;
    ImageView removeIv;

    public UploadPhoto(ImageView uploadIv){
        this.uploadIv = uploadIv;
    }

    public UploadPhoto(ImageView uploadIv, ImageView removeIv){
        this.uploadIv = uploadIv;
        this.removeIv = removeIv;
    }

    public  ImageView getUploadIv(){
        return uploadIv;
    }

}