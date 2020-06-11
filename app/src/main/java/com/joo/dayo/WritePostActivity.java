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
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

public class WritePostActivity extends Activity {

    ArrayList<UploadPhoto> uploadPhotos;
    UploadPhotoAdapter uploadPhotoAdapter;
    RecyclerView uploadPhotoView;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    int PICK_IMAGE_FROM_ALBUM = 0;
    private Uri uri;
    private Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        //저장소 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        //사진 선택 하기
        storage = FirebaseStorage.getInstance();
        Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
        photoPickIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        photoPickIntent.setType("image/*");
        startActivityForResult(photoPickIntent, PICK_IMAGE_FROM_ALBUM);

        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    try {
                        uri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        Toast.makeText(getApplicationContext(), "사진 선택 후 ", Toast.LENGTH_SHORT).show();
                        showIv();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else {
                //사진 선택 안했을 시 종료
                finish();
            }
        }
    }

    private void init() {
        uploadPhotoView = (RecyclerView) findViewById(R.id.uploadPhotoView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        uploadPhotoView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        uploadPhotos = new ArrayList<>();
        uploadPhotoAdapter = new UploadPhotoAdapter(getApplicationContext(), uploadPhotos);
        uploadPhotoView.setAdapter(uploadPhotoAdapter);

        //첫 이미지 뷰 추가하기!!!
        /*
        ImageView uploadIv = new ImageView(this);
        uploadIv.setImageResource(R.drawable.ic_add_to_photos_black_24dp);
        UploadPhoto uploadPhoto = new UploadPhoto(uploadIv);
        uploadPhotos.add(uploadPhoto);
        */
    }

    public void showIv(){
        ImageView uploadIv = new ImageView(this);
        uploadIv.setImageURI(uri);
        UploadPhoto uploadPhoto = new UploadPhoto(uploadIv);
        uploadPhotos.add(uploadPhoto);
        uploadPhotoAdapter.notifyDataSetChanged();
    }

    public void contentUpload() {
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //DB에 등록하기
                final String cu = mAuth.getUid();
                //1. 사진을 storage에 저장하고 그 url을 알아내야함..
                String filename = cu + "_" + System.currentTimeMillis();
                StorageReference storageRef = storage.getReferenceFromUrl("본인의 Firebase 저장소").child("WriteClassImage/" + filename);
                UploadTask uploadTask;

                Uri file = uri;

                uploadTask = storageRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override

                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.v("알림", "사진 업로드 실패");
                        exception.printStackTrace();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        uri = taskSnapshot.getUploadSessionUri();
                        Log.v("알림", "사진 업로드 성공 " + uri);
                    }
                });
            }
        };
    }
}