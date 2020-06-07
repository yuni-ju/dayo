package com.joo.dayo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LogInActivity extends Activity {

    private FirebaseAuth mAuth;
    TextView joinText, pwdUpdateTxt;
    Button loginBtn;
    EditText emailTxt, pwdTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        //로그인 기능
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailTxt = (EditText) findViewById(R.id.emailTxt);
                pwdTxt = (EditText) findViewById(R.id.pwdTxt);
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
        joinText = (TextView) findViewById(R.id.joinText);
        joinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailTxt = (EditText) findViewById(R.id.emailTxt);
                pwdTxt = (EditText) findViewById(R.id.pwdTxt);
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivityForResult(intent, 0);
            }
        });
/*
        //비밀번호 재설정 기능
        pwdUpdateTxt = (TextView) findViewById(R.id.joinText);
        pwdTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailTxt = (EditText) findViewById(R.id.emailTxt);
                final String email = emailTxt.getText().toString();

                mAuth = FirebaseAuth.getInstance();
                String emailAddress = email;

                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                }
                            }
                });
            }
        });
*/

    }

    private void logIn(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
