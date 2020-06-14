package com.joo.dayo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.joo.dayo.R;
import com.joo.dayo.activity.FolderPostActivity;
import com.joo.dayo.activity.WritePostActivity;
import com.joo.dayo.data.UploadPhoto;

import java.util.ArrayList;
import java.util.List;


public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder>{

    private List<String> folderNameList;

    public class FolderViewHolder extends RecyclerView.ViewHolder {

        ImageView folderIv;
        TextView folderNameTxt;
        LinearLayout folderLayout;

        public FolderViewHolder(@NonNull final View itemView) {
            super(itemView);

            folderLayout = (LinearLayout) itemView.findViewById(R.id.folderLayout );
            folderIv = (ImageView) itemView.findViewById(R.id.folderIv);
            folderNameTxt = (TextView) itemView.findViewById(R.id.folderNameTxt);;

            //폴더 클릭 시
            folderLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        //해당 폴더에 해당하는 글 보여줌
                        Intent intent = new Intent(v.getContext(), FolderPostActivity.class);
                        intent.putExtra("folderNum", position);
                        ((Activity) v.getContext()).startActivity(intent);
                    }
                }
            });

        }

    }

    public FolderAdapter(List<String> folderNameList) {
        this.folderNameList = folderNameList;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.folder_item, parent, false);
        FolderViewHolder viewHolder = new FolderViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {

        String folderName = folderNameList.get(position);
        holder.folderNameTxt.setText(folderName);
    }

    @Override
    public int getItemCount() {
        return (folderNameList != null ? folderNameList.size() : null );
    }

}
