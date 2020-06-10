package com.joo.dayo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class WritePostActivity extends Activity {

    ArrayList<UploadPhoto> uploadPhotos;
    UploadPhotoAdapter uploadPhotoAdapter;
    RecyclerView uploadPhotoView;

    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        //저장소 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        init();

        //이미지 뷰 클릭시

    }

    private void init() {
        uploadPhotos = new ArrayList<>();
        uploadPhotoView = (RecyclerView) findViewById(R.id.uploadPhotoView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        uploadPhotoView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //첫 이미지 뷰 추가하기!!!
        //ImageView imageView = new ImageView(this);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_add_to_photos_black_24dp);
        UploadPhoto uploadPhoto = new UploadPhoto(imageView);
        uploadPhotos.add(uploadPhoto);
        uploadPhotos.add(uploadPhoto);
        uploadPhotos.add(uploadPhoto);
        uploadPhotos.add(uploadPhoto);
        uploadPhotos.add(uploadPhoto);

        uploadPhotoAdapter = new UploadPhotoAdapter(uploadPhotos);
        uploadPhotoView.setAdapter(uploadPhotoAdapter);
    }

}
