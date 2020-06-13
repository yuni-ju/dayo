package com.joo.dayo.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joo.dayo.R;
import com.joo.dayo.activity.SettingPageActivity;
import com.joo.dayo.activity.WritePostActivity;
import com.joo.dayo.data.PostData;
import com.joo.dayo.data.UserData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.net.ssl.SSLEngineResult;

public class MyPageFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    FirebaseFirestore firestore;
    FirebaseStorage storage;

    View view;
    ImageButton settingBtn;
    TextView profileNameTxt, profileMailTxt, profileStatusTxt;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_page, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        updateData();

        //setting 버튼 누르기
        settingBtn = (ImageButton) view.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingPageActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //설정 후 돌아왔을 때 상태메세지 업데이트 되도록
        updateData();
    }

    void updateData(){

        profileNameTxt = (TextView) view.findViewById(R.id.profileNameTxt);
        profileMailTxt = (TextView) view.findViewById(R.id.profileMailTxt);
        profileStatusTxt = (TextView) view.findViewById(R.id.profileStatusTxt);

        //회원정보 불러오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            String uid = user.getUid();

            Toast.makeText(this.getContext(), name + "님 안녕하세요!", Toast.LENGTH_SHORT).show();
        }

        //내 정보 띄우기
        profileNameTxt.setText(user.getDisplayName());
        profileMailTxt.setText(user.getEmail());

        //user db에서 불러오기
        DocumentReference docRef = firestore.collection("user").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserData userData = documentSnapshot.toObject(UserData.class);
                profileStatusTxt.setText(userData.getStatus());
                //Toast.makeText(getContext(), userData.getStatus() + " : 상태 ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
