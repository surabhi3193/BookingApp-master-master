package com.example.mind_android.bookingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;

public class HTmlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        TextView text = findViewById(R.id.headtv);
        Bundle bundle = getIntent().getExtras();

        if (bundle!=null)
        {
            String head = bundle.getString("name");
            text.setText(head);

        }
    }
}
