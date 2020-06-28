package com.joo.dayo.activity;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joo.dayo.R;

public class LogInActivity extends Activity {

    private FirebaseAuth mAuth;
    TextView joinText, pwdUpdateTxt;
    Button loginBtn;
    FirebaseUser user;
    EditText emailTxt, pwdTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        loginBtn = (Button) findViewById(R.id.loginBtn);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        pwdTxt = (EditText) findViewById(R.id.pwdTxt);
        joinText = (TextView) findViewById(R.id.joinText);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        pwdTxt = (EditText) findViewById(R.id.pwdTxt);
        pwdUpdateTxt = (TextView) findViewById(R.id.pwdUpdateTxt);

        //로그인 기능
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTxt.getText().toString();
                final String pwd = pwdTxt.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (pwd.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    logIn(email, pwd);
                }
            }
        });

        //회원가입 버튼 클릭
        joinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //비밀번호 재설정 기능
        pwdUpdateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailTxt.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(LogInActivity.this) // TestActivity 부분에는 현재 Activity의 이름 입력.
                            .setMessage(email + " 로 비밀번호 재설정 메일을 전송하시겠습니까?")     // 제목 부분 (직접 작성)
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                                public void onClick(DialogInterface dialog, int which) {
                                    //재설정 메일 전송
                                    mAuth.sendPasswordResetEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "이메일을 전송했습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(), "해당 계정이 없습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show(); // 실행할 코드
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void logIn(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            String name = user.getDisplayName();
                            Toast.makeText(getApplicationContext(), name + "님 안녕하세요!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
