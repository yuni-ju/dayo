package com.joo.dayo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.joo.dayo.R;

public class UpdatePwdActivity extends Activity {

    Button UpdateBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        UpdateBtn = (Button)findViewById(R.id.UpdateBtn);

    }
}
