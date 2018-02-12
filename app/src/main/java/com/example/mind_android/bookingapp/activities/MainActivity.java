package com.example.mind_android.bookingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mind_android.bookingapp.R;
import com.example.mind_android.bookingapp.activities.dashboard_part.StockActivity;

import static com.example.mind_android.bookingapp.storage.MySharedPref.saveData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView signout_btn = findViewById(R.id.signout_btn);
        ImageView stockLay = findViewById(R.id.stockLay);
        ImageView salesLay = findViewById(R.id.salesLay);
        ImageView expenseLay = findViewById(R.id.expenseLay);
        ImageView reportLay = findViewById(R.id.reportLay);
        ImageView loanLay = findViewById(R.id.loanLay);
        ImageView bankLay = findViewById(R.id.bankLay);


//        stockLay.setOnClickListener(this);

        salesLay.setOnClickListener(this);
        expenseLay.setOnClickListener(this);
        reportLay.setOnClickListener(this);
        loanLay.setOnClickListener(this);
        bankLay.setOnClickListener(this);

        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(getApplicationContext(),"login","0");

                startActivity(new Intent(MainActivity.this,EnterLoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.stockLay:
//                startActivity(new Intent(getApplicationContext(), StockActivity.class));
                break;
        }

    }
}
