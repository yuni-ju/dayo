package com.joo.dayo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.joo.dayo.R;
import com.joo.dayo.adapter.NewPostAdapter;
import com.joo.dayo.data.PostData;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class NewPostFragment extends Fragment {

    FirebaseFirestore firestore;
    DatabaseReference db;
    NewPostAdapter newPostAdapter;
    RecyclerView newPostView;
    ArrayList<PostData> newPostList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        firestore = FirebaseFirestore.getInstance();

        return inflater.inflate(R.layout.fragment_new_post,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    void init(){

        newPostList = new ArrayList<>();

        //db에서 가져오기
        firestore.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                newPostList.add(new PostData(document.getData().get("explain").toString(),
                                        document.getData().get("photoName").toString(),
                                        document.getData().get("uid").toString(),
                                        document.getData().get("userId").toString(),
                                        document.getData().get("timeStamp").toString(),
                                        Integer.parseInt(document.getData().get("favorite").toString())
                                ));
                            }

                            //연결
                            newPostView = (RecyclerView) getView().findViewById(R.id.newPostView);
                            newPostView.setHasFixedSize(true);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            newPostView.setLayoutManager(linearLayoutManager);
                            newPostAdapter = new NewPostAdapter(newPostList);
                            newPostView.setAdapter(newPostAdapter);

                            newPostAdapter.notifyDataSetChanged();
                        } else {
                            // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });





    }

}
