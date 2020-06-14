package com.joo.dayo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joo.dayo.R;

public class SettingPageActivity extends Activity {

    Button UpdateStatusBtn, UpdatePwBtn, logOutBtn;
    FirebaseFirestore firestore;
    FirebaseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        //상태메세지 수정 기능
        UpdateStatusBtn = (Button) findViewById(R.id.UpdateStatusBtn);
        UpdateStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력 창 띄우기
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingPageActivity.this);
                alert.setTitle("상태메세지 설정");
                final EditText statusTxt = new EditText(SettingPageActivity.this);
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(50); //글자수 제한
                statusTxt.setFilters(FilterArray);
                alert.setView(statusTxt);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) { //확인 버튼을 클릭했을때
                        String status = statusTxt.getText().toString();
                        //db에 저장

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        firestore = FirebaseFirestore.getInstance();
                        DocumentReference userRef = firestore.collection("user").document(user.getUid());
                        userRef
                                .update("status", status)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.w(TAG, "Error updating document", e);
                                    }
                                });

                    }
                });
                alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) { //취소 버튼을 클릭
                    }
                });
                alert.show();
            }
        });

        //회원정보 수정 기능
        UpdatePwBtn = (Button) findViewById(R.id.UpdatePwBtn);
        UpdatePwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdatePwdActivity.class);
                startActivity(intent);
            }
        });

        //로그아웃 후 종료하기
        logOutBtn = (Button) findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

    }

}
