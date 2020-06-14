package com.joo.dayo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.joo.dayo.R;
import com.joo.dayo.activity.SettingPageActivity;
import com.joo.dayo.adapter.FolderAdapter;
import com.joo.dayo.data.UserData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MyPageFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    FirebaseStorage storage;

    RecyclerView folderView;
    FolderAdapter folderAdapter;
    View view;
    LinearLayout addFolderLayout;
    ImageButton settingBtn;
    TextView profileNameTxt, profileMailTxt, profileStatusTxt;

    int folderSize = 0;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_page, container, false);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        profileNameTxt = (TextView) view.findViewById(R.id.profileNameTxt);
        profileMailTxt = (TextView) view.findViewById(R.id.profileMailTxt);
        profileStatusTxt = (TextView) view.findViewById(R.id.profileStatusTxt);
        folderView = (RecyclerView) view.findViewById(R.id.folderView);

        addFolderLayout = (LinearLayout) view.findViewById(R.id.addFolderLayout);

        //회원정보 불러오기
        user = FirebaseAuth.getInstance().getCurrentUser();
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

        //내 정보 연결
        profileNameTxt.setText(user.getDisplayName());
        profileMailTxt.setText(user.getEmail());

        //db에서 불러오는 정보
        updateData();

        //다이어리 추가 버튼 누르기
        addFolderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(folderSize>2){
                  Toast.makeText(v.getContext(), "다이어리는 3개까지만 생성 가능합니다.", Toast.LENGTH_SHORT).show();
              }
              else{
                  //다이어리 추가 후 DB 업데이트.
                  //다이어리명 입력 창 띄우기
                  AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                  alert.setTitle("추가할 다이어리 이름 입력");
                  final EditText folderNameTxt = new EditText(v.getContext());
                  InputFilter[] FilterArray = new InputFilter[1];
                  FilterArray[0] = new InputFilter.LengthFilter(8); //글자수 제한
                  folderNameTxt.setFilters(FilterArray);
                  alert.setView(folderNameTxt);
                  alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) { //확인 버튼을 클릭했을때
                          final String folderName = folderNameTxt.getText().toString();
                          firestore = FirebaseFirestore.getInstance();
                          //배열 db 업데이트
                          DocumentReference folderRef = firestore .collection("user").document(user.getUid());
                          folderRef.update("folder", FieldValue.arrayUnion(folderName));
                          //washingtonRef.update("regions", FieldValue.arrayRemove("east_coast"));
                      }
                  });
                  alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int whichButton) { //취소 버튼을 클릭
                      }
                  });
                  alert.show();
                  updateData();
              }
            }
        });

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
        //user db에서 불러오기
        DocumentReference docRef = firestore.collection("user").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserData userData = documentSnapshot.toObject(UserData.class);

                //상태메세지와 폴더 정보 불러옴
                profileStatusTxt.setText(userData.getStatus());

                //폴더 업데이트
                folderAdapter = new FolderAdapter(userData.getFolder());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
                folderView.setLayoutManager(gridLayoutManager);
                folderView.setAdapter(folderAdapter);

                //폴더 개수
                folderSize = userData.getFolderNum();

                folderAdapter.notifyDataSetChanged();

            }
        });

    }
}
