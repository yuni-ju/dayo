package com.joo.dayo;

import android.app.Activity;
import android.net.Uri;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class JoinActivity extends Activity {

    private FirebaseAuth mAuth;
    //private FirebaseUser currentUser;

    EditText emailEdt, pwdEdt, pwdEdt2, nameEdt;

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
                pwdEdt2 = (EditText) findViewById(R.id.pwdEdt2);
                nameEdt = (EditText) findViewById(R.id.nameEdt);

                final String email = emailEdt.getText().toString();
                final String pwd =  pwdEdt.getText().toString();
                final String pwd2 =  pwdEdt2.getText().toString();
                final String name =  nameEdt.getText().toString();

                if(email.isEmpty()||pwd.isEmpty()||name.isEmpty()){
                    Toast.makeText(getApplicationContext(),"모든 내용을 입력하세요." ,Toast.LENGTH_SHORT).show();
                }else if(!pwd.equals(pwd2)){
                    Toast.makeText(getApplicationContext(),"입력하신 비밀번호가 다릅니다." ,Toast.LENGTH_SHORT).show();
                }else{
                    join(email,pwd,name);
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    private void join(String email, String pwd, final String name){
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
                        } else{

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "회원가입 성공!",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            finish();
                        }

                    }
                });
    }


}
