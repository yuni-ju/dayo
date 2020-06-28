package com.joo.dayo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.joo.dayo.R;

public class UpdatePwdActivity extends Activity {

    Button updateBtn;
    EditText updatePwdEdt,updatePwdEdt2;
    FirebaseUser user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        updateBtn = (Button)findViewById(R.id.UpdateBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();

        updatePwdEdt = (EditText) findViewById(R.id.updatePwdEdt);
        updatePwdEdt2 = (EditText) findViewById(R.id.updatePwdEdt2);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pwd =  updatePwdEdt.getText().toString();
                final String pwd2 = updatePwdEdt2.getText().toString();
                //Log.d("pwd","pwd : "+pwd +"pwd2 : "+pwd2);
                if(pwd.isEmpty()||pwd2.isEmpty()){
                    Toast.makeText(getApplicationContext(),"모든 내용을 입력하세요." ,Toast.LENGTH_SHORT).show();
                }else if(!pwd.equals(pwd2)){
                    Toast.makeText(getApplicationContext(),"입력하신 비밀번호가 다릅니다." ,Toast.LENGTH_SHORT).show();
                }else{
                    updatePwd(pwd);
                }
            }
        });

    }

    private void updatePwd(String pwd){
        user.updatePassword(pwd)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getApplicationContext(),"비밀번호는 6자 이상이어야 합니다." ,Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(task.isSuccessful()){
                            finish();
                        }
                    }
                });
    }
}
