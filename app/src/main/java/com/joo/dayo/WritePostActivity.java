package com.joo.dayo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WritePostActivity extends Activity {

    ArrayList<UploadPhoto> uploadPhotos;
    UploadPhotoAdapter uploadPhotoAdapter;
    RecyclerView uploadPhotoView;
    ImageView cancelIv;
    TextView uploadPostIv;
    EditText explainEdt;
    String imgFileName;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private StorageReference storageRef;
    int PICK_IMAGE_FROM_ALBUM = 0;
    private Uri uri;
    private Uri[] uris = new Uri[5];
    private Bitmap bitmap;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        mAuth=FirebaseAuth.getInstance();

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
        photoPickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(photoPickIntent, PICK_IMAGE_FROM_ALBUM);

        //리스트뷰에 사진 등록
        init();

        //글 내용 연결
        explainEdt = (EditText)findViewById(R.id.explainEdt);

        //공유 버튼 클릭 (firebase에 글과 사진 업로드)
        uploadPostIv = (TextView) findViewById(R.id.uploadPostIv);
        uploadPostIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //사진 업로드
                int i=0;
                String imgTemp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                while(uris[i]!=null){
                    String imgFileName = "IMAGE_" + imgTemp + i + ".png";
                    storageRef = storage.getReference("images").child(imgFileName);
                    UploadTask uploadTask = storageRef.putFile(uris[i]);
                    Toast.makeText(getApplicationContext(),i + "개 사진 업로드 성공",Toast.LENGTH_SHORT).show();
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(),"사진 업로드 성공",Toast.LENGTH_SHORT).show();
                            // ...
                        }
                    });
                    i++;
                }

                //정보 연동 및 글 업로드
                /*
                storage = FirebaseStorage.getInstance();
                firestore = FirebaseFirestore.getInstance();
                PostData postData = new PostData();
                i=0;
                while(uris[i]!=null) {
                    postData.uris[i] = uris[i].toString();
                }
                postData.uid = mAuth.getCurrentUser().getUid();
                postData.userId = mAuth.getCurrentUser().getEmail();
                postData.explain = explainEdt.getText().toString();
                postData.timeStamp = System.currentTimeMillis();
                firestore.collection("images").document().set(postData);
                */
                finish();
            }
        });

        //x 버튼 클릭
        cancelIv = (ImageView) findViewById(R.id.cancelIv);
        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                if(data.getClipData() != null){
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    if(count>5){
                        Toast.makeText(this, "사진은 한번에 최대 5개까지 업로드 가능", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    for(int i = 0; i < count; i++) {
                        uris[i] = data.getClipData().getItemAt(i).getUri();
                    }
                    showIv(count);
                }
                else if (data.getData() != null) {
                    try {
                        //uri = data.getData();
                        //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

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
        uploadPhotoAdapter = new UploadPhotoAdapter(uploadPhotos);
        uploadPhotoView.setAdapter(uploadPhotoAdapter);

        //첫 이미지 뷰 추가하기!!!
        /*
        ImageView uploadIv = new ImageView(this);
        uploadIv.setImageResource(R.drawable.ic_add_to_photos_black_24dp);
        UploadPhoto uploadPhoto = new UploadPhoto(uploadIv);
        uploadPhotos.add(uploadPhoto);
        */
    }

    public void showIv(int count){

        for(int i=0;i<count;i++) {
            ImageView uploadIv = new ImageView(this);
            uploadIv.setImageURI(uris[i]);
            UploadPhoto uploadPhoto = new UploadPhoto(uploadIv);
            uploadPhotos.add(uploadPhoto);
        }
        uploadPhotoAdapter.notifyDataSetChanged();
    }
}