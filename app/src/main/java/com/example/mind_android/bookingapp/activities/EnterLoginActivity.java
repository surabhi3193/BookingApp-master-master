package com.example.mind_android.bookingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mind_android.bookingapp.R;

import static com.example.mind_android.bookingapp.storage.MySharedPref.NullData;

public class EnterLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_login);

        NullData(getApplicationContext(),"buss_name");
        NullData(getApplicationContext(),"buss_type");
        NullData(getApplicationContext(),"city");
        NullData(getApplicationContext(),"country");
        Button signup_btn = findViewById(R.id.signup_btn);
        Button login_btn = findViewById(R.id.login_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        startActivity(new Intent(EnterLoginActivity.this,SignupActivity.class));
         finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnterLoginActivity.this,LoginActivity.class));
                finish();

            }
        });
    }
}
