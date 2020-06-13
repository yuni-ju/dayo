package com.joo.dayo.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joo.dayo.R;
import com.joo.dayo.data.PostData;

import java.io.IOException;
import java.util.ArrayList;

public class NewPostAdapter extends RecyclerView.Adapter<NewPostAdapter.NewPostViewHolder>{

    ArrayList<PostData> newPostList;
    //ArrayList<ContentPhoto> contentPhotos;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public class NewPostViewHolder extends RecyclerView.ViewHolder {

        ImageView contentPhotoView;
        TextView userNameTxt, contentTxt ;

        public NewPostViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTxt = (TextView) itemView.findViewById(R.id.userNameTxt);
            contentTxt = (TextView) itemView.findViewById(R.id.contentTxt);
            contentPhotoView = (ImageView) itemView.findViewById(R.id.contentPhotoView);

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
    public void onBindViewHolder(@NonNull final NewPostViewHolder holder, int position) {


        holder.userNameTxt.setText(newPostList.get(position).userId);
        holder.contentTxt.setText(newPostList.get(position).getExplain());

        //이미지 불러와서 넣기
        StorageReference photoRef  = storage.getReference("images").child(newPostList.get(position).getPhotoName());

        photoRef.getBytes(1024 * 1024 *5)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        Bitmap resize = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                        holder.contentPhotoView.setImageBitmap(resize);
                        Log.d("비트맵", "success!");
                    }
                });
       /*
        photoRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("URI다운", "Download url is: " + uri.toString());
                        holder.contentPhotoView.setImageURI(uri);
                    }
                });
        */
    }

    @Override
    public int getItemCount() {
        return (newPostList != null ? newPostList.size() : null );
    }

    /*
    void favoriteEvent(int position){
        final DocumentReference sfDocRef = firestore.collection("post").document(contentUid[position]);

        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                double newPopulation = snapshot.getDouble("population") + 1;
                transaction.update(sfDocRef, "population", newPopulation);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "Transaction success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Transaction failure.", e);
                    }
                });

    }
*/


}
