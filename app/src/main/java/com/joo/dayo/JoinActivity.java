package com.joo.dayo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class JoinActivity extends Activity {

    private FirebaseAuth mAuth;
    //private FirebaseUser currentUser;

    EditText emailEdt, pwdEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //회원가입 기능
        mAuth = FirebaseAuth.getInstance();
        Button joinBtn = (Button) findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailEdt = (EditText) findViewById(R.id.emailEdt);
                pwdEdt = (EditText) findViewById(R.id.pwdEdt);

                final String email = emailEdt.getText().toString();
                final String pwd =  pwdEdt.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(getApplicationContext(),"이메일을 입력하세요." ,Toast.LENGTH_SHORT).show();
                }else if(pwd.isEmpty()){
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력하세요." ,Toast.LENGTH_SHORT).show();
                }else{
                    join(email,pwd);
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    private void join(String email, String pwd){
        mAuth.createUserWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getApplicationContext(),"비밀번호는 6자 이상이어야 합니다." ,Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(),"email 형식에 맞지 않습니다." ,Toast.LENGTH_SHORT).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getApplicationContext(),"이미존재하는 email 입니다." ,Toast.LENGTH_SHORT).show();
                            } catch(Exception e) {
                                Toast.makeText(getApplicationContext(),"다시 확인해주세요" ,Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //currentUser = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "회원가입 성공!",Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }
                });
    }


}
