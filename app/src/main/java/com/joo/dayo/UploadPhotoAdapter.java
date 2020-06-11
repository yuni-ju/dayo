package com.joo.dayo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class UploadPhotoAdapter extends RecyclerView.Adapter<UploadPhotoAdapter.UploadPhotoViewHolder>{

    private ArrayList<UploadPhoto> uploadPhotos;

    private FirebaseStorage storage;
    int PICK_IMAGE_FROM_ALBUM = 0;
    private Uri uri;
    private Bitmap bitmap;

    public class UploadPhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView uploadPhotoIv, removeIv;


        public UploadPhotoViewHolder(@NonNull final View itemView) {
            super(itemView);
            uploadPhotoIv = (ImageView) itemView.findViewById(R.id.uploadPhotoIv);
            removeIv = (ImageView) itemView.findViewById(R.id.removeIv);;

            //필요 시 여기서 클릭이벤트 추가
            uploadPhotoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position == getItemCount() -1 && position != RecyclerView.NO_POSITION) {
                        /*
                        //사진 추가하기 버튼 처리
                        ImageView imageView = new ImageView(context);
                        imageView.setImageResource(R.drawable.ic_add_to_photos_black_24dp);
                        removeIv.setVisibility(View.VISIBLE);
                        UploadPhoto uploadPhoto = new UploadPhoto(imageView,removeIv);
                        uploadPhotos.add(uploadPhoto);
                        notifyDataSetChanged();
                         */
                    }
                    else{
                        //올린 사진 클릭 시 사진 업로드 하도록.

                    }
                }
            });

            removeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(uploadPhotos.size()==1 ){
                        Toast.makeText(view.getContext(), "사진 전부 삭제", Toast.LENGTH_SHORT).show();
                        ((Activity)view.getContext()).finish();
                    }
                    else if (position != RecyclerView.NO_POSITION) {
                        uploadPhotos.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });

        }

    }

    public UploadPhotoAdapter(ArrayList<UploadPhoto> uploadPhotos) {
        this.uploadPhotos = uploadPhotos;
    }

    @NonNull
    @Override
    public UploadPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.upload_photo_item, parent, false);
        UploadPhotoViewHolder viewHolder = new UploadPhotoViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadPhotoViewHolder holder, int position) {
        Drawable drawable = uploadPhotos.get(position).getUploadIv().getDrawable();
        holder.uploadPhotoIv.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return (uploadPhotos != null ? uploadPhotos.size() : null );
    }

}
