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

    public class UploadPhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView uploadPhotoIv;

        public UploadPhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            uploadPhotoIv = (ImageView) itemView.findViewById(R.id.uploadPhotoIv);

            //필요 시 여기서 클릭이벤트 추가

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
