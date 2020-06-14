package com.joo.dayo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joo.dayo.fragment.BestPostFragment;
import com.joo.dayo.fragment.MyPageFragment;
import com.joo.dayo.fragment.NewPostFragment;
import com.joo.dayo.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private BestPostFragment bestPostFragment = new BestPostFragment();
    private NewPostFragment newPostFragment = new NewPostFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, bestPostFragment).commitAllowingStateLoss();
        Log.d("Main의 뭐가 실행?","OnCreate");
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.bestPostItm:
                        transaction.replace(R.id.fragment_container, bestPostFragment).commitAllowingStateLoss();
                        setTitle("인기 글");
                        break;

                    case R.id.newPostItm:
                        transaction.replace(R.id.fragment_container, newPostFragment).commitAllowingStateLoss();
                        setTitle("최신 글");
                        break;

                    case R.id.myPageItm:
                        transaction.replace(R.id.fragment_container, myPageFragment).commitAllowingStateLoss();
                        setTitle("My Diary");
                        break;

                    case R.id.writePostItm:
                        Intent intent = new Intent(getApplicationContext(), WritePostActivity.class);
                        startActivity(intent);
                        transaction.addToBackStack(null);
                        break;
                }
                return true;
            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();
        }
    }



}
