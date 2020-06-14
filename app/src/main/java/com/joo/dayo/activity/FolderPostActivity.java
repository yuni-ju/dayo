package com.joo.dayo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.joo.dayo.R;
import com.joo.dayo.adapter.FolderAdapter;
import com.joo.dayo.adapter.NewPostAdapter;
import com.joo.dayo.data.PostData;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FolderPostActivity extends Activity {

    ArrayList<PostData> folderPostList;
    FirebaseFirestore firestore;
    NewPostAdapter newPostAdapter;
    RecyclerView newPostView;
    FirebaseUser firebaseUser;
    TextView emptyTxt;
    int folderNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_post);

        folderNum =  getIntent().getIntExtra("folderNum",0);
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        init();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
        finish();
    }

    void init(){

        folderPostList = new ArrayList<>();

        //db에서 가져오기
        firestore.collection("post")
                .whereEqualTo("uid", firebaseUser.getUid())
                .whereEqualTo("folderNum",folderNum)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                folderPostList.add(new PostData(document.getData().get("explain").toString(),
                                        document.getData().get("photoName").toString(),
                                        document.getData().get("uid").toString(),
                                        document.getData().get("userId").toString(),
                                        document.getData().get("timeStamp").toString(),
                                        Integer.parseInt(document.getData().get("favorite").toString()),
                                        Integer.parseInt(document.getData().get("folderNum").toString())
                                ));
                            }

                            if(folderPostList.isEmpty()){
                                emptyTxt = (TextView) findViewById(R.id.emptyTxt);
                                emptyTxt.setVisibility(View.VISIBLE);
                            }

                            //연결
                            newPostView = (RecyclerView) findViewById(R.id.newPostView);
                            newPostView.setHasFixedSize(true);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FolderPostActivity.this);
                            newPostView.setLayoutManager(linearLayoutManager);
                            newPostAdapter = new NewPostAdapter( folderPostList);
                            newPostView.setAdapter(newPostAdapter);

                            newPostAdapter.notifyDataSetChanged();
                        } else {
                            // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
