package com.joo.dayo.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joo.dayo.R;
import com.joo.dayo.activity.WritePostActivity;
import com.joo.dayo.data.PostData;
import com.joo.dayo.data.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewPostAdapter extends RecyclerView.Adapter<NewPostAdapter.NewPostViewHolder>{

    ArrayList<PostData> newPostList;
    ArrayList<PostData> folderPostList;
    FirebaseFirestore firestore;
    DatabaseReference mDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth auth;
    int folderSize, folderNum;
    int favoriteNum;

    public class NewPostViewHolder extends RecyclerView.ViewHolder {

        ImageView contentPhotoView, favoriteIv;
        TextView userNameTxt, contentTxt,favoriteCnt ;


        public NewPostViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTxt = (TextView) itemView.findViewById(R.id.userNameTxt);
            contentTxt = (TextView) itemView.findViewById(R.id.contentTxt);
            contentPhotoView = (ImageView) itemView.findViewById(R.id.contentPhotoView);
            favoriteIv = (ImageView) itemView.findViewById(R.id.favoriteIv);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            auth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();
            favoriteCnt = (TextView) itemView.findViewById(R.id.favoriteCnt);
            //onStarClicked(mDatabase);

        }
    }

    public NewPostAdapter(ArrayList<PostData> newPostList) {
        this.newPostList = newPostList;
    }

    @NonNull
    @Override
    public NewPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_content_item, parent, false);

        return new NewPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewPostViewHolder holder, final int position) {

        holder.userNameTxt.setText(newPostList.get(position).userId);
        holder.favoriteIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        holder.contentTxt.setText(newPostList.get(position).getExplain());
        favoriteNum = newPostList.get(position).favorite;

        //이미지 불러와서 넣기

            StorageReference photoRef = storage.getReference("images").child(newPostList.get(position).getPhotoName());

            photoRef.getBytes(1024 * 1024 * 5)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Bitmap resize = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                            holder.contentPhotoView.setImageBitmap(resize);
                            Log.d("비트맵", "success!");
                        }
                    });

        //좋아요
        final StorageReference favoriteRef = storage.getReference("images").child(String.valueOf(newPostList.get(position).getFavorites().get(auth.getUid())));
        final boolean[] favChk = {Boolean.parseBoolean(String.valueOf(newPostList.get(position).getFavorites().get(auth.getUid())))};
        if (favChk[0]) {
            holder.favoriteIv.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            holder.favoriteIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        //좋아요 클릭 시
            // Create reference for new rating, for use inside the transaction
        holder.favoriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> childUpdates = new HashMap<>();
                //favorite 개수 db에서 불러오기
                if (favChk[0]) { //하트 있을때 누르면 없어짐.
                    // Unstar the post and remove self from stars
                    favoriteNum -= 1;
                    childUpdates.remove(auth.getCurrentUser().getUid());
                    holder.favoriteIv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    favChk[0] = false;
                } else {
                    // Star the post and add self to stars
                    favoriteNum += 1;
                    childUpdates.put(auth.getUid(), true);
                    holder.favoriteIv.setImageResource(R.drawable.ic_favorite_black_24dp);
                    favChk[0]= true;
                }
                DocumentReference ref = firestore.collection("post").document("favorite");
                ref
                        .update("favorite",  favoriteNum)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });


                // Set value and report transaction success
                mDatabase.updateChildren(childUpdates);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (newPostList != null ? newPostList.size() : null );
    }






}
